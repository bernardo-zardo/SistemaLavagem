package com.bernardo.utils;

/**
 *
 * @author Bernardo Zardo Mergen
 */
public class ServicoResumoAux {
	
	private String tipoServico;
    private Long total;
    
	public ServicoResumoAux(String tipoServico, Long total) {
		this.tipoServico = tipoServico;
		this.total = total;
	}

	public String getTipoServico() {
		return tipoServico;
	}
	
	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}
	
	public Long getTotal() {
		return total;
	}
	
	public void setTotal(Long total) {
		this.total = total;
	}
}
