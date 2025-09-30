package com.bernardo.entidades;

import java.io.Serializable;

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
@Table(name = "CLIENTE")
public class Cliente implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLI_ID_CLIENTE", nullable = false)
    private Integer cliIdCliente;

    @Column(name = "CLI_NOME", length = 50, nullable = false)
    private String cliNome;

    @Column(name = "CLI_CEP", length = 8)
    private String cliCep;

    @Column(name = "CLI_RUA", length = 150)
    private String cliRua;

    @Column(name = "CLI_NUMERO", length = 10)
    private String cliNumero;

    @Column(name = "CLI_COMPLEMENTO", length = 50)
    private String cliComplemento;

    @Column(name = "CLI_BAIRRO", length = 100)
    private String cliBairro;

    @Column(name = "CLI_CIDADE", length = 100)
    private String cliCidade;

    @Column(name = "CLI_UF", length = 2)
    private String cliUf;

    @Column(name = "CLI_TELEFONE", length = 20)
    private String cliTelefone;

    @Column(name = "CLI_EMAIL", length = 64)
    private String cliEmail;

    @Column(name = "CLI_CPF", length = 11, unique = true)
    private String cliCpf;

    @Column(name = "CLI_LATITUDE", precision = 10, scale = 8)
    private Double cliLatitude;

    @Column(name = "CLI_LONGITUDE", precision = 11, scale = 8)
    private Double cliLongitude;

    public Cliente() {
    }

    public Cliente(Integer cliIdCliente, String cliNome, String cliCep, String cliRua, String cliNumero,
			String cliComplemento, String cliBairro, String cliCidade, String cliUf, String cliTelefone,
			String cliEmail, String cliCpf, Double cliLatitude, Double cliLongitude) {
		super();
		this.cliIdCliente = cliIdCliente;
		this.cliNome = cliNome;
		this.cliCep = cliCep;
		this.cliRua = cliRua;
		this.cliNumero = cliNumero;
		this.cliComplemento = cliComplemento;
		this.cliBairro = cliBairro;
		this.cliCidade = cliCidade;
		this.cliUf = cliUf;
		this.cliTelefone = cliTelefone;
		this.cliEmail = cliEmail;
		this.cliCpf = cliCpf;
		this.cliLatitude = cliLatitude;
		this.cliLongitude = cliLongitude;
	}

	public Integer getCliIdCliente() {
        return cliIdCliente;
    }

    public void setCliIdCliente(Integer cliIdCliente) {
        this.cliIdCliente = cliIdCliente;
    }

    public String getCliNome() {
        return cliNome;
    }

    public void setCliNome(String cliNome) {
        this.cliNome = cliNome;
    }

    public String getCliTelefone() {
        return cliTelefone;
    }

    public void setCliTelefone(String cliTelefone) {
        this.cliTelefone = cliTelefone;
    }

    public String getCliEmail() {
        return cliEmail;
    }

    public void setCliEmail(String cliEmail) {
        this.cliEmail = cliEmail;
    }

    public String getCliCpf() {
        return cliCpf;
    }

    public void setCliCpf(String cliCpf) {
        this.cliCpf = cliCpf;
    }

	public String getCliCep() {
		return cliCep;
	}

	public void setCliCep(String cliCep) {
		this.cliCep = cliCep;
	}

	public String getCliRua() {
		return cliRua;
	}

	public void setCliRua(String cliRua) {
		this.cliRua = cliRua;
	}

	public String getCliNumero() {
		return cliNumero;
	}

	public void setCliNumero(String cliNumero) {
		this.cliNumero = cliNumero;
	}

	public String getCliComplemento() {
		return cliComplemento;
	}

	public void setCliComplemento(String cliComplemento) {
		this.cliComplemento = cliComplemento;
	}

	public String getCliBairro() {
		return cliBairro;
	}

	public void setCliBairro(String cliBairro) {
		this.cliBairro = cliBairro;
	}

	public String getCliCidade() {
		return cliCidade;
	}

	public void setCliCidade(String cliCidade) {
		this.cliCidade = cliCidade;
	}

	public String getCliUf() {
		return cliUf;
	}

	public void setCliUf(String cliUf) {
		this.cliUf = cliUf;
	}

	public Double getCliLatitude() {
		return cliLatitude;
	}

	public void setCliLatitude(Double cliLatitude) {
		this.cliLatitude = cliLatitude;
	}

	public Double getCliLongitude() {
		return cliLongitude;
	}

	public void setCliLongitude(Double cliLongitude) {
		this.cliLongitude = cliLongitude;
	}
}
