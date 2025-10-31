package com.bernardo.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.bernardo.utils.StringUtil;

/**
*
* @author Bernardo Zardo Mergen
*/
@Entity
@Table(name = "CLIENTE")
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLI_ID_CLIENTE", nullable = false)
    private Integer cliIdCliente;

    @Column(name = "CLI_NOME", length = 50, nullable = false)
    private String cliNome;

    @Column(name = "CLI_TELEFONE", length = 20)
    private String cliTelefone;

    @Column(name = "CLI_EMAIL", length = 64)
    private String cliEmail;

    @Column(name = "CLI_CPF", length = 11, unique = true)
    private String cliCpf;
    
    @OneToMany(mappedBy = "endCliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EnderecoCliente> cliEnderecos = new ArrayList<>();

    public Cliente() {
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
    
	public List<EnderecoCliente> getCliEnderecos() {
		return cliEnderecos;
	}

	public void setCliEnderecos(List<EnderecoCliente> cliEnderecos) {
		this.cliEnderecos = cliEnderecos;
	}

	public String getCpfFormatado() {
		return StringUtil.getCpfFormatado(cliCpf);
	}
	
	public void adicionarEndereco(EnderecoCliente endereco) {
	    endereco.setEndCliente(this);
	    this.cliEnderecos.add(endereco);
	}

	public void removerEndereco(EnderecoCliente endereco) {
	    this.cliEnderecos.remove(endereco);
	    endereco.setEndCliente(null);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cliIdCliente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(cliIdCliente, other.cliIdCliente);
	}
}
