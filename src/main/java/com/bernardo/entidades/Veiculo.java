package com.bernardo.entidades;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
*
* @author Bernardo Zardo Mergen
*/
@Entity
@Table(name = "VEICULO")
public class Veiculo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VEI_ID_VEICULO", nullable = false)
    private Integer veiIdVeiculo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "VEI_ID_CLIENTE", referencedColumnName = "CLI_ID_CLIENTE", nullable = false)
    private Cliente veiCliente;

    @Column(name = "VEI_MARCA", length = 45)
    private String veiMarca;

    @Column(name = "VEI_MODELO", length = 45)
    private String veiModelo;

    @Column(name = "VEI_COR", length = 45)
    private String veiCor;

    @Column(name = "VEI_PLACA", length = 15, unique = true)
    private String veiPlaca;

    public Veiculo() {
    }

	public Integer getVeiIdVeiculo() {
		return veiIdVeiculo;
	}

	public void setVeiIdVeiculo(Integer veiIdVeiculo) {
		this.veiIdVeiculo = veiIdVeiculo;
	}

	public Cliente getVeiCliente() {
		return veiCliente;
	}

	public void setVeiCliente(Cliente veiCliente) {
		this.veiCliente = veiCliente;
	}

	public String getVeiMarca() {
		return veiMarca;
	}

	public void setVeiMarca(String veiMarca) {
		this.veiMarca = veiMarca;
	}

	public String getVeiModelo() {
		return veiModelo;
	}

	public void setVeiModelo(String veiModelo) {
		this.veiModelo = veiModelo;
	}

	public String getVeiCor() {
		return veiCor;
	}

	public void setVeiCor(String veiCor) {
		this.veiCor = veiCor;
	}

	public String getVeiPlaca() {
		return veiPlaca;
	}

	public void setVeiPlaca(String veiPlaca) {
		this.veiPlaca = veiPlaca;
	}

	@Override
	public int hashCode() {
		return Objects.hash(veiIdVeiculo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Veiculo other = (Veiculo) obj;
		return Objects.equals(veiIdVeiculo, other.veiIdVeiculo);
	}
}
