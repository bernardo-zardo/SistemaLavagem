package com.bernardo.beans.cadastros;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.bernardo.entidades.Responsavel;
import com.bernardo.services.BaseCrud;
import com.bernardo.services.ResponsavelService;
import com.bernardo.utils.JsfUtil;
import com.bernardo.utils.StringUtil;

/**
 *
 * @author Bernardo Zardo
 */
@Named
@ViewScoped
public class CadastroResponsavelBean extends BaseCrud<Responsavel> implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private ResponsavelService responsavelService;

	private boolean alterando = false;
	private List<Responsavel> responsaveis;

	@PostConstruct
	public void montaRegistros() {
		responsaveis = responsavelService.filtrar(new HashMap<>());
	}

	@Override
	public void criaObj() {
		crudObj = new Responsavel();
		alterando = false;
	}

	@Override
	public void salvar() {

		// validações
		if (StringUtil.isNullOrEmpty(crudObj.getResNome())) {
			JsfUtil.warn("Informe o nome.");
			return;
		}
		if (!StringUtil.isCPFValido(crudObj.getResCpf())) {
			JsfUtil.error("CPF inválido.");
			return;
		}
		if (StringUtil.isNullOrEmpty(crudObj.getResSenha())) {
			JsfUtil.warn("Informe a senha.");
			return;
		}

		// limpeza de máscara
		crudObj.setResCpf(StringUtil.getOnlyNumbers(crudObj.getResCpf()));
		crudObj.setResTelefone(StringUtil.getOnlyNumbers(crudObj.getResTelefone()));

		// hash da senha
		crudObj.setResSenha(StringUtil.gerarHashSHA256(crudObj.getResSenha()));

		if (alterando) {
			responsavelService.salvar(crudObj);
			JsfUtil.info("Responsável atualizado com sucesso!");

		} else {

			// valida duplicidade de CPF
			Map<String, Object> filtros = new HashMap<>();
			filtros.put("resCpf", crudObj.getResCpf());
			List<Responsavel> existentes = responsavelService.filtrar(filtros);

			if (existentes.isEmpty()) {
				responsavelService.salvar(crudObj);
				JsfUtil.info("Responsável salvo com sucesso!");
			} else {
				JsfUtil.warn("Já existe um responsável cadastrado com este CPF: " + existentes.get(0).getResNome());
				return;
			}
		}

		responsaveis = responsavelService.filtrar(new HashMap<>());
		criaObj();
	}

	@Override
	public void deletar() {
		boolean sucesso = responsavelService.deletar(crudObj);

		if (sucesso) {
			JsfUtil.info("Responsável excluído com sucesso!");
		}

		criaObj();
		responsaveis = responsavelService.filtrar(new HashMap<>());
	}

	public void selecionar(Responsavel r) {
		this.crudObj = r;
		this.alterando = true;
		JsfUtil.info("Responsável selecionado para edição.");
	}

	public void excluirResponsavel(Responsavel r) {
		boolean sucesso = responsavelService.deletar(r);

		if (sucesso) {
			JsfUtil.info("Responsável excluído com sucesso!");
		} else {
			JsfUtil.warn("Não é possível excluir este responsável. Há vínculos existentes.");
		}

		responsaveis = responsavelService.filtrar(new HashMap<>());
		criaObj();
	}

	public List<Responsavel> getResponsaveis() {
		return responsaveis;
	}

	public Responsavel getCrudObj() {
		return crudObj;
	}

	public boolean isAlterando() {
		return alterando;
	}
}
