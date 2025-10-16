package com.bernardo.beans;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.bernardo.entidades.Responsavel;

import java.io.Serializable;

/**
 * @author Bernardo Zardo Mergen
 */
@Named
@SessionScoped
public class ResponsavelLogadoBean implements Serializable {

    private static final long serialVersionUID = 1L;
	private Responsavel responsavelLogado;
	
	public Responsavel getResponsavelLogado() {
		return responsavelLogado;
	}
	
	public void setResponsavelLogado(Responsavel responsavelLogado) {
		this.responsavelLogado = responsavelLogado;
	}
}
