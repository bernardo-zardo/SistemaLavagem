package com.bernardo.entidades;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "ENDERECO_CLIENTE")
public class EnderecoCliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "END_ID_ENDERECO")
    private Integer endIdEndereco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "END_ID_CLIENTE", nullable = false)
    private Cliente endCliente;
    
    @Column(name = "END_DESCRICAO", length = 50)
    private String endDescricao;
    
    @Column(name = "END_CEP", length = 8)
    private String endCep;
    
    @Column(name = "END_RUA", length = 150)
    private String endRua;

    @Column(name = "END_NUMERO", length = 10)
    private String endNumero;

    @Column(name = "END_COMPLEMENTO", length = 50)
    private String endComplemento;

    @Column(name = "END_BAIRRO", length = 100)
    private String endBairro;

    @Column(name = "END_CIDADE", length = 100)
    private String endCidade;

    @Column(name = "END_UF", length = 2)
    private String endUf;

    @Column(name = "END_LATITUDE", precision = 10, scale = 8)
    private Double endLatitude;

    @Column(name = "END_LONGITUDE", precision = 11, scale = 8)
    private Double endLongitude;
    
	public EnderecoCliente() {
	}

	public Integer getEndIdEndereco() {
		return endIdEndereco;
	}

	public void setEndIdEndereco(Integer endIdEndereco) {
		this.endIdEndereco = endIdEndereco;
	}

	public Cliente getEndCliente() {
		return endCliente;
	}

	public void setEndCliente(Cliente endCliente) {
		this.endCliente = endCliente;
	}
	
	public String getEndDescricao() {
		return endDescricao;
	}

	public void setEndDescricao(String endDescricao) {
		this.endDescricao = endDescricao;
	}

	public String getEndCep() {
		return endCep;
	}

	public void setEndCep(String endCep) {
		this.endCep = endCep;
	}

	public String getEndRua() {
		return endRua;
	}

	public void setEndRua(String endRua) {
		this.endRua = endRua;
	}

	public String getEndNumero() {
		return endNumero;
	}

	public void setEndNumero(String endNumero) {
		this.endNumero = endNumero;
	}

	public String getEndComplemento() {
		return endComplemento;
	}

	public void setEndComplemento(String endComplemento) {
		this.endComplemento = endComplemento;
	}

	public String getEndBairro() {
		return endBairro;
	}

	public void setEndBairro(String endBairro) {
		this.endBairro = endBairro;
	}

	public String getEndCidade() {
		return endCidade;
	}

	public void setEndCidade(String endCidade) {
		this.endCidade = endCidade;
	}

	public String getEndUf() {
		return endUf;
	}

	public void setEndUf(String endUf) {
		this.endUf = endUf;
	}

	public Double getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(Double endLatitude) {
		this.endLatitude = endLatitude;
	}

	public Double getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(Double endLongitude) {
		this.endLongitude = endLongitude;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(endIdEndereco);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnderecoCliente other = (EnderecoCliente) obj;
		return Objects.equals(endIdEndereco, other.endIdEndereco);
	}
}
