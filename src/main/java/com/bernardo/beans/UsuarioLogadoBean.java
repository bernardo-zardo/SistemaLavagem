package com.bernardo.beans;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.bernardo.entidades.Cliente;
import com.bernardo.entidades.Responsavel;

import java.io.Serializable;

/**
 * @author Bernardo Zardo Mergen
 */
@Named
@SessionScoped
public class UsuarioLogadoBean implements Serializable {

    private static final long serialVersionUID = 1L;
	private Responsavel responsavelLogado;
	private Cliente clienteLogado;
	
	public Responsavel getResponsavelLogado() {
		return responsavelLogado;
	}
	
	public void setResponsavelLogado(Responsavel responsavelLogado) {
		this.responsavelLogado = responsavelLogado;
	}
	
	public Cliente getClienteLogado() {
        return clienteLogado;
    }

    public void setClienteLogado(Cliente clienteLogado) {
        this.clienteLogado = clienteLogado;
    }
}
