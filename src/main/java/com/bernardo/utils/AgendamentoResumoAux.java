package com.bernardo.utils;

import java.util.Date;

/**
 *
 * @author Bernardo Zardo Mergen
 */
public class AgendamentoResumoAux {
	
    private int id;
    private String cliente;
    private String veiculo;
    private Date data;
    private String hora;
    private String servico;
    
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCliente() {
		return cliente;
	}
	
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	
	public String getVeiculo() {
		return veiculo;
	}
	
	public void setVeiculo(String veiculo) {
		this.veiculo = veiculo;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public String getHora() {
		return hora;
	}
	
	public void setHora(String hora) {
		this.hora = hora;
	}
	
	public String getServico() {
		return servico;
	}
	
	public void setServico(String servico) {
		this.servico = servico;
	}
}
