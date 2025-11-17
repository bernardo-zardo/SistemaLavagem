package com.bernardo.entidades;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*
* @author Bernardo Zardo Mergen
*/
@Entity
@Table(name = "TIPO_SERVICO")
public class TipoServico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TS_ID_TIPO_SERVICO")
    private Integer tsIdTipoServico;

    @Column(name = "TS_NOME", length = 45)
    private String tsNome;

    @Column(name = "TS_PRECO", precision = 10, scale = 2)
    private Double tsPreco;

    public TipoServico() {
    }

	public Integer getTsIdTipoServico() {
		return tsIdTipoServico;
	}

	public void setTsIdTipoServico(Integer tsIdTipoServico) {
		this.tsIdTipoServico = tsIdTipoServico;
	}

	public String getTsNome() {
		return tsNome;
	}

	public void setTsNome(String tsNome) {
		this.tsNome = tsNome;
	}

	public Double getTsPreco() {
		return tsPreco;
	}

	public void setTsPreco(Double tsPreco) {
		this.tsPreco = tsPreco;
	}
	
	public String getTipoServicoDesc() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
	    return this.tsNome + " - " + nf.format(this.tsPreco);
	}
	
	public String getTipoServicoPrecoFormat() {
	    return "R$" + this.tsPreco;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(tsIdTipoServico);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoServico other = (TipoServico) obj;
		return Objects.equals(tsIdTipoServico, other.tsIdTipoServico);
	}
}
