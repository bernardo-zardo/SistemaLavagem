package com.bernardo.entidades;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
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

    @Column(name = "SER_PRECO_SERVICO_EXTRA")
    private Double serPrecoServicoExtra;
    
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
    
    @Column(name = "SER_POSSUI_BUSCA_VEICULO", nullable = false)
    private boolean serPossuiBuscaVeiculo;

    @Column(name = "SER_POSSUI_ENTREGA_VEICULO", nullable = false)
    private boolean serPossuiEntregaVeiculo;

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

	public Double getSerPrecoServicoExtra() {
		return serPrecoServicoExtra;
	}

	public void setSerPrecoServicoExtra(Double serPrecoServicoExtra) {
		this.serPrecoServicoExtra = serPrecoServicoExtra;
	}

	public boolean isSerPossuiBuscaVeiculo() {
		return serPossuiBuscaVeiculo;
	}

	public void setSerPossuiBuscaVeiculo(boolean serPossuiBuscaVeiculo) {
		this.serPossuiBuscaVeiculo = serPossuiBuscaVeiculo;
	}

	public boolean isSerPossuiEntregaVeiculo() {
		return serPossuiEntregaVeiculo;
	}

	public void setSerPossuiEntregaVeiculo(boolean serPossuiEntregaVeiculo) {
		this.serPossuiEntregaVeiculo = serPossuiEntregaVeiculo;
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
	
	public String getDescEntrega() {
	    if (this.serPrecoServicoExtra == null) {
	        return "Sem Entrega";
	    }

	    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
	    return "Entrega incluída: " + nf.format(this.serPrecoServicoExtra);
	}
	
	public String getPrecoTotalFormat() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
	    return nf.format(this.serPrecoTotal);
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
