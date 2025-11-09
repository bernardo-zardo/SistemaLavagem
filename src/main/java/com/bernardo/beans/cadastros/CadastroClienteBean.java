package com.bernardo.beans.cadastros;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import com.bernardo.entidades.Cliente;
import com.bernardo.entidades.EnderecoCliente;
import com.bernardo.services.BaseCrud;
import com.bernardo.services.ClienteService;
import com.bernardo.utils.JsfUtil;
import com.bernardo.utils.StringUtil;

/**
 *
 * @author Bernardo Zardo Mergen
 */
@Named
@ViewScoped
public class CadastroClienteBean extends BaseCrud<Cliente> implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private ClienteService clienteService;

	private EnderecoCliente novoEndereco = new EnderecoCliente();
	private boolean editandoEndereco = false;
			
    private String cpfDigitado;
    private boolean exibirPainelCPF = true;
    private boolean exibirPainelCadastro = false;
    private boolean exibirPainelSenha = false;
    private boolean cpfJaCadastradoComSenha = false;
    
    
    @Override
	public void criaObj() {
		crudObj = new Cliente();
	}

    public void verificarCpf() {
        if (StringUtil.isNullOrEmpty(cpfDigitado)) {
            JsfUtil.error("Informe o CPF.");
            return;
        }

        String cpfLimpo = StringUtil.getOnlyNumbers(cpfDigitado);
        if (!StringUtil.isCPFValido(cpfLimpo)) {
            JsfUtil.error("CPF inválido.");
            return;
        }

        Map<String, Object> filtros = new HashMap<>();
        filtros.put("cliCpf", cpfLimpo);
        List<Cliente> encontrados = clienteService.filtrar(filtros);

        if (encontrados.isEmpty()) {
            // CPF não existe → mostrar cadastro completo
            this.crudObj = new Cliente();
            crudObj.setCliCpf(cpfLimpo);
            exibirPainelCadastro = true;
        } else {
            Cliente cliente = encontrados.get(0);
            this.crudObj = cliente;

            if (cliente.getCliSenha() == null || cliente.getCliSenha().isEmpty()) {
                // Existe CPF mas sem senha → pedir para criar
                exibirPainelSenha = true;
            } else {
                // CPF e senha já cadastrados → mostrar mensagem
                cpfJaCadastradoComSenha = true;
            }
        }

        exibirPainelCPF = false;
    }

    public void salvarNovoCliente() throws InterruptedException {
        if (StringUtil.isNullOrEmpty(crudObj.getCliNome()) ||
            StringUtil.isNullOrEmpty(crudObj.getCliEmail()) ||
            StringUtil.isNullOrEmpty(crudObj.getCliTelefone()) ||
            StringUtil.isNullOrEmpty(crudObj.getCliSenha())) {
            JsfUtil.error("Preencha todos os campos obrigatórios.");
            return;
        }

        crudObj.setCliCpf(StringUtil.getOnlyNumbers(crudObj.getCliCpf()));
        crudObj.setCliTelefone(StringUtil.getOnlyNumbers(crudObj.getCliTelefone()));

        crudObj.setCliSenha(StringUtil.gerarHashSHA256(crudObj.getCliSenha()));

        clienteService.salvar(crudObj);
        
        JsfUtil.info("Cadastros realizado com sucesso!");

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/login.xhtml";
        PrimeFaces.current().executeScript("setTimeout(function(){ window.location = '" + url + "'; }, 1000);");
    }
    public void criarSenhaParaCliente() throws InterruptedException {
        if (StringUtil.isNullOrEmpty(crudObj.getCliSenha())) {
            JsfUtil.error("Informe uma senha.");
            return;
        }

        crudObj.setCliSenha(StringUtil.gerarHashSHA256(crudObj.getCliSenha()));
        clienteService.salvar(crudObj);
        
        JsfUtil.info("Senha cadastrada com sucesso!");
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        String url = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/login.xhtml";
        PrimeFaces.current().executeScript("setTimeout(function(){ window.location = '" + url + "'; }, 1000);");
    }

    public void resetarFluxo() {
        exibirPainelCPF = true;
        exibirPainelCadastro = false;
        exibirPainelSenha = false;
        cpfJaCadastradoComSenha = false;
        cpfDigitado = null;
        criaObj();
    }

	public Cliente getCrudObj() {
		return crudObj;
	}

	public EnderecoCliente getNovoEndereco() {
		return novoEndereco;
	}

	public void setNovoEndereco(EnderecoCliente novoEndereco) {
		this.novoEndereco = novoEndereco;
	}

	public boolean isEditandoEndereco() {
		return editandoEndereco;
	}

	public void setEditandoEndereco(boolean editandoEndereco) {
		this.editandoEndereco = editandoEndereco;
	}

	public String getCpfDigitado() {
		return cpfDigitado;
	}

	public void setCpfDigitado(String cpfDigitado) {
		this.cpfDigitado = cpfDigitado;
	}

	public boolean isExibirPainelCPF() {
		return exibirPainelCPF;
	}

	public void setExibirPainelCPF(boolean exibirPainelCPF) {
		this.exibirPainelCPF = exibirPainelCPF;
	}

	public boolean isExibirPainelCadastro() {
		return exibirPainelCadastro;
	}

	public void setExibirPainelCadastro(boolean exibirPainelCadastro) {
		this.exibirPainelCadastro = exibirPainelCadastro;
	}

	public boolean isExibirPainelSenha() {
		return exibirPainelSenha;
	}

	public void setExibirPainelSenha(boolean exibirPainelSenha) {
		this.exibirPainelSenha = exibirPainelSenha;
	}

	public boolean isCpfJaCadastradoComSenha() {
		return cpfJaCadastradoComSenha;
	}

	public void setCpfJaCadastradoComSenha(boolean cpfJaCadastradoComSenha) {
		this.cpfJaCadastradoComSenha = cpfJaCadastradoComSenha;
	}
}
