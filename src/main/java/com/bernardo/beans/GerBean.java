package com.bernardo.beans;

import com.bernardo.utils.JsfUtil;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author Bernardo Zardo Mergen
 * 
 */
@Named
@SessionScoped
public class GerBean implements Serializable {

    private static final long serialVersionUID = 1L;
	@Inject
    private UsuarioLogadoBean usuarioLogadoBean;

    public String getStyleMenu() {
        return usuarioLogadoBean.getResponsavelLogado() != null ? "" : "display: none;";
    }

    public void logout() {
        JsfUtil.getCurrentInstance().getExternalContext().invalidateSession();
    	JsfUtil.redirect("/SistemaLavagem/landingPage.xhtml");
    }

    public boolean isResponsavelLogado() {
        return usuarioLogadoBean.getResponsavelLogado() != null;
    }
    
    public boolean isClienteLogado() {
        return usuarioLogadoBean.getClienteLogado() != null;
    }
}
