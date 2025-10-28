package com.bernardo.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.*;

/**
*
* @author Bernardo Zardo Mergen
*/
@Entity
@Table(name = "AGENDAMENTO_SERVICO")
public class Agendamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AG_ID_AGENDAMENTO")
    private Integer agIdAgendamento;

    @Temporal(TemporalType.DATE)
    @Column(name = "AG_DATA", nullable = false)
    private Date agData;

    @Temporal(TemporalType.TIME)
    @Column(name = "AG_HORA", nullable = false)
    private Date agHora;

    @ManyToOne(optional = false)
    @JoinColumn(name = "AG_ID_VEICULO", referencedColumnName = "VEI_ID_VEICULO", nullable = false)
    private Veiculo agVeiculo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "AG_ID_TIPO_SERVICO", referencedColumnName = "TS_ID_TIPO_SERVICO", nullable = false)
    private TipoServico agTipoServico;
    
    @Column(name = "AG_POSSUI_BUSCA_VEICULO", nullable = false)
    private boolean agPossuiBuscaVeiculo;

    @Column(name = "AG_POSSUI_ENTREGA_VEICULO", nullable = false)
    private boolean agPossuiEntregaVeiculo;
    
    @Column(name = "AG_PRECO_SERVICO_EXTRA")
    private Double agPrecoServicoExtra;
    
    @Column(name = "AG_STATUS", nullable = false, length = 1)
    private String agStatus = "P"; // P = Pendente, C = Concluído, X = Cancelado

    public Agendamento() {
    }

    public Integer getAgIdAgendamento() {
        return agIdAgendamento;
    }

    public void setAgIdAgendamento(Integer agIdAgendamento) {
        this.agIdAgendamento = agIdAgendamento;
    }

    public Date getAgData() {
        return agData;
    }

    public void setAgData(Date agData) {
        this.agData = agData;
    }

    public Date getAgHora() {
        return agHora;
    }

    public void setAgHora(Date agHora) {
        this.agHora = agHora;
    }

    public Veiculo getAgVeiculo() {
        return agVeiculo;
    }

    public void setAgVeiculo(Veiculo agVeiculo) {
        this.agVeiculo = agVeiculo;
    }

    public TipoServico getAgTipoServico() {
        return agTipoServico;
    }

    public void setAgTipoServico(TipoServico agTipoServico) {
        this.agTipoServico = agTipoServico;
    }
    
	public boolean isAgPossuiBuscaVeiculo() {
		return agPossuiBuscaVeiculo;
	}

	public void setAgPossuiBuscaVeiculo(boolean agPossuiBuscaVeiculo) {
		this.agPossuiBuscaVeiculo = agPossuiBuscaVeiculo;
	}

	public boolean isAgPossuiEntregaVeiculo() {
		return agPossuiEntregaVeiculo;
	}

	public void setAgPossuiEntregaVeiculo(boolean agPossuiEntregaVeiculo) {
		this.agPossuiEntregaVeiculo = agPossuiEntregaVeiculo;
	}

	public Double getAgPrecoServicoExtra() {
		return agPrecoServicoExtra;
	}

	public void setAgPrecoServicoExtra(Double agPrecoServicoExtra) {
		this.agPrecoServicoExtra = agPrecoServicoExtra;
	}

	public String getAgStatus() {
        return agStatus;
    }

    public void setAgStatus(String agStatus) {
        this.agStatus = agStatus;
    }

	public String getStatusDescricao() {
        if (this.agStatus.equals("P")) {
        	return "Pendente";
        } else if (this.agStatus.equals("C")) {
        	return "Concluído";
        } else if (this.agStatus.equals("X")) {
        	return "Cancelado";
        } else {
        	return "Desconhecido";
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(agIdAgendamento);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Agendamento other = (Agendamento) obj;
        return Objects.equals(agIdAgendamento, other.agIdAgendamento);
    }
}
