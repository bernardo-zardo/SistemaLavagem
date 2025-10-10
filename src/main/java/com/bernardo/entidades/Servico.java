package com.bernardo.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
*
* @author Bernardo Zardo Mergen
*/
@Entity
@Table(name = "SERVICO")
public class Servico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SER_ID_SERVICO")
    private Integer serIdServico;

    @Temporal(TemporalType.DATE)
    @Column(name = "SER_DATA")
    private Date serData;

    @Column(name = "SER_PRECO_ENTREGA")
    private Double serPrecoEntrega;
    
    @Column(name = "SER_PRECO_TOTAL")
    private Double serPrecoTotal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "SER_ID_TIPO_SERVICO", referencedColumnName = "TS_ID_TIPO_SERVICO", nullable = false)
    private TipoServico serTipoServico;

    @ManyToOne(optional = false)
    @JoinColumn(name = "SER_ID_VEICULO", referencedColumnName = "VEI_ID_VEICULO", nullable = false)
    private Veiculo serVeiculo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "SER_ID_RESPONSAVEL", referencedColumnName = "RES_ID_RESPONSAVEL", nullable = false)
    private Responsavel serResponsavel;

	public Servico() {
	}

	public Integer getSerIdServico() {
		return serIdServico;
	}

	public void setSerIdServico(Integer serIdServico) {
		this.serIdServico = serIdServico;
	}

	public Date getSerData() {
		return serData;
	}

	public void setSerData(Date serData) {
		this.serData = serData;
	}

	public Double getSerPrecoEntrega() {
		return serPrecoEntrega;
	}

	public void setSerPrecoEntrega(Double serPrecoEntrega) {
		this.serPrecoEntrega = serPrecoEntrega;
	}
	
	public Double getSerPrecoTotal() {
		return serPrecoTotal;
	}

	public void setSerPrecoTotal(Double serPrecoTotal) {
		this.serPrecoTotal = serPrecoTotal;
	}

	public TipoServico getSerTipoServico() {
		return serTipoServico;
	}

	public void setSerTipoServico(TipoServico serTipoServico) {
		this.serTipoServico = serTipoServico;
	}

	public Veiculo getSerVeiculo() {
		return serVeiculo;
	}

	public void setSerVeiculo(Veiculo serVeiculo) {
		this.serVeiculo = serVeiculo;
	}

	public Responsavel getSerResponsavel() {
		return serResponsavel;
	}

	public void setSerResponsavel(Responsavel serResponsavel) {
		this.serResponsavel = serResponsavel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(serIdServico);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Servico other = (Servico) obj;
		return Objects.equals(serIdServico, other.serIdServico);
	}
}
