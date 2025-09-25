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

    @Column(name = "CLI_NOME", length = 50)
    private String cliNome;

    @Column(name = "CLI_ENDERECO", length = 150)
    private String cliEndereco;

    @Column(name = "CLI_TELEFONE", length = 20)
    private String cliTelefone;

    @Column(name = "CLI_EMAIL", length = 64)
    private String cliEmail;

    @Column(name = "CLI_CPF", length = 11, unique = true)
    private String cliCpf;

    public Cliente() {
    }

    public Cliente(String cliNome, String cliEndereco, String cliTelefone, String cliEmail, String cliCpf) {
        this.cliNome = cliNome;
        this.cliEndereco = cliEndereco;
        this.cliTelefone = cliTelefone;
        this.cliEmail = cliEmail;
        this.cliCpf = cliCpf;
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

    public String getCliEndereco() {
        return cliEndereco;
    }

    public void setCliEndereco(String cliEndereco) {
        this.cliEndereco = cliEndereco;
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
}
