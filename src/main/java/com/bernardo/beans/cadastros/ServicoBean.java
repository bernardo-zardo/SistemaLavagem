package com.bernardo.beans.cadastros;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.bernardo.entidades.Cliente;
import com.bernardo.entidades.Responsavel;
import com.bernardo.entidades.Servico;
import com.bernardo.entidades.TipoServico;
import com.bernardo.entidades.Veiculo;
import com.bernardo.services.BaseCrud;
import com.bernardo.services.ClienteService;
import com.bernardo.services.ResponsavelService;
import com.bernardo.services.ServicoService;
import com.bernardo.services.TipoServicoService;
import com.bernardo.services.VeiculoService;
import com.bernardo.utils.JsfUtil;

/**
 *
 * @author Bernardo Zardo Mergen
 */
@Named
@ViewScoped
public class ServicoBean extends BaseCrud<Servico> implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private ServicoService servicoService;
	@EJB
	private VeiculoService veiculoService;
	@EJB
	private ClienteService clienteService;
	@EJB
	private ResponsavelService responsavelService;
	@EJB
	private TipoServicoService tipoServicoService;

	private boolean alterando;
	private List<Veiculo> veiculos;
	private List<Cliente> clientes;
	private List<Responsavel> responsaveis;
	private List<Servico> servicos;
	private List<TipoServico> tiposServico;

	@PostConstruct
	public void montaRegistros() {

		String servicoIdParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("servicoId");
		if (servicoIdParam != null) {
			Long servicoId = Long.valueOf(servicoIdParam);
			List<Servico> servicoParam = servicoService.getServicoPorId(servicoId.toString());
			this.crudObj = servicoParam.get(0);
			this.alterando = true;
		}

		veiculos = veiculoService.filtrar(new HashMap<>());
		clientes = clienteService.filtrar(new HashMap<>());
		responsaveis = responsavelService.filtrar(new HashMap<>());
		servicos = servicoService.filtrar(new HashMap<>());

		servicos.sort(Comparator.comparing(Servico::getSerIdServico).reversed());

		tiposServico = tipoServicoService.filtrar(new HashMap<>());
	}

	@Override
	public void criaObj() {
		crudObj = new Servico();
		alterando = false;
	}

	@Override
	public void salvar() {
		Double valorTotal = 0.0;
		if (crudObj.getSerPrecoServicoExtra() != null) {
			valorTotal = crudObj.getSerTipoServico().getTsPreco() + crudObj.getSerPrecoServicoExtra();
		} else {
			valorTotal = crudObj.getSerTipoServico().getTsPreco();
		}
		crudObj.setSerPrecoTotal(valorTotal);
		if (alterando) {
			veiculoService.salvar(crudObj);
			JsfUtil.info("Serviço atualizado com sucesso!");
		} else {
			veiculoService.salvar(crudObj);
			JsfUtil.info("Serviço salvo com sucesso!");
		}
		servicos = servicoService.filtrar(new HashMap<>());
		servicos.sort(Comparator.comparing(Servico::getSerIdServico).reversed());
		criaObj();
	}

	@Override
	public void deletar() {
		boolean sucesso = servicoService.deletar(crudObj);
		criaObj();
		if (sucesso) {
			JsfUtil.info("Serviço excluído com sucesso!");
		}
		servicos = servicoService.filtrar(new HashMap<>());
		servicos.sort(Comparator.comparing(Servico::getSerIdServico).reversed());
	}

	public void selecionarServico(Servico servico) {
		this.crudObj = servico;
		this.alterando = true;
		JsfUtil.info("Serviço selecionado.");
	}

	public void excluirServico(Servico servico) {
		this.crudObj = servico;
		boolean sucesso = servicoService.deletar(servico);
		criaObj();
		if (sucesso) {
			JsfUtil.info("Serviço excluído com sucesso!");
		}
		servicos = servicoService.filtrar(new HashMap<>());
		servicos.sort(Comparator.comparing(Servico::getSerIdServico).reversed());
	}

	public Servico getCrudObj() {
		return crudObj;
	}

	public boolean isAlterando() {
		return alterando;
	}

	public void setAlterando(boolean alterando) {
		this.alterando = alterando;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Responsavel> getResponsaveis() {
		return responsaveis;
	}

	public void setResponsaveis(List<Responsavel> responsaveis) {
		this.responsaveis = responsaveis;
	}

	public List<Servico> getServicos() {
		return servicos;
	}

	public void setServicos(List<Servico> servicos) {
		this.servicos = servicos;
	}

	public List<TipoServico> getTiposServico() {
		return tiposServico;
	}

	public void setTiposServico(List<TipoServico> tiposServico) {
		this.tiposServico = tiposServico;
	}
}
