package com.bernardo.beans;

import java.io.IOException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.bernardo.entidades.Cliente;
import com.bernardo.entidades.Responsavel;
import com.bernardo.services.ClienteService;
import com.bernardo.services.ResponsavelService;
import com.bernardo.utils.JsfUtil;
import com.bernardo.utils.StringUtil;

/**
 * @author Bernardo Zardo Mergen
 */
@Named
@ViewScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;
	@EJB
    private ResponsavelService responsavelService;
	@EJB
    private ClienteService clienteService;
    @Inject
    private UsuarioLogadoBean usuarioLogadoBean;

    private String senha;
    private String cpf;
    private List<Responsavel> usuariosEncontrados = new ArrayList<>();
    
    private String tipoLogin = "cliente";
    
    @PostConstruct
    private void init() {
    }

    public void doLogin() {
    	usuarioLogadoBean.setClienteLogado(null);
    	usuarioLogadoBean.setResponsavelLogado(null);
    	
        if (StringUtil.isNullOrEmpty(cpf)) {
            JsfUtil.warn("É necessário informar seu CPF");
            return;
        }

        if (!StringUtil.isCPFValido(cpf)) {
            JsfUtil.warn("CPF inválido");
            return;
        }

        if (StringUtil.isNullOrEmpty(senha)) {
            JsfUtil.warn("É necessário informar sua Senha");
            return;
        }

        String cpfOnlyNumbers = StringUtil.getOnlyNumbers(cpf);

        if ("admin".equalsIgnoreCase(tipoLogin)) {
            List<Responsavel> responsavelList = responsavelService.getPessoaPorCpf(cpfOnlyNumbers);

            if (responsavelList.isEmpty()) {
                JsfUtil.warn("Nenhum responsável encontrado com o CPF informado");
                return;
            }

            Responsavel usuario = responsavelList.get(0);
            if (!senha.equals(usuario.getResSenha())) {
                JsfUtil.warn("Senha inválida");
                return;
            }

            usuarioLogadoBean.setResponsavelLogado(usuario);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                    .put("usuarioLogadoBean", usuarioLogadoBean);

            JsfUtil.redirect("/SistemaLavagem/restrito/indexAdmin.xhtml");
        } else {
            List<Cliente> clienteList = clienteService.getPessoaPorCpf(cpfOnlyNumbers);

            if (clienteList.isEmpty()) {
                JsfUtil.warn("Nenhum cliente encontrado com o CPF informado");
                return;
            }

            Cliente cliente = clienteList.get(0);
            String senhaCriptografada = StringUtil.gerarHashSHA256(senha);

            if (!senhaCriptografada.equals(cliente.getCliSenha())) {
                JsfUtil.warn("Senha inválida");
                return;
            }

            usuarioLogadoBean.setClienteLogado(cliente);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                    .put("usuarioLogadoBean", usuarioLogadoBean);

            JsfUtil.redirect("/SistemaLavagem/restritoCliente/indexCliente.xhtml");
        }
    }

    
    public void irParaCadastroCliente() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext()
            .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/cadastroCliente.xhtml");
    }

    public void redirecionarParaLogin() {
        JsfUtil.redirect("/SistemaLavagem/login.xhtml");
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

	public List<Responsavel> getUsuariosEncontrados() {
		return usuariosEncontrados;
	}

	public void setUsuariosEncontrados(List<Responsavel> usuariosEncontrados) {
		this.usuariosEncontrados = usuariosEncontrados;
	}

	public String getTipoLogin() {
		return tipoLogin;
	}

	public void setTipoLogin(String tipoLogin) {
		this.tipoLogin = tipoLogin;
	}
}
