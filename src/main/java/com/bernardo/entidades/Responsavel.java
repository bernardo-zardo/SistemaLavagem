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
@Table(name = "RESPONSAVEL")
public class Responsavel implements Serializable {

    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RES_ID_RESPONSAVEL", nullable = false)
    private Integer resIdResponsavel;

    @Column(name = "RES_NOME", length = 50)
    private String resNome;

    @Column(name = "RES_TELEFONE", length = 20)
    private String resTelefone;

    @Column(name = "RES_CPF", length = 11, unique = true)
    private String resCpf;

    @Column(name = "RES_SENHA", length = 255, nullable = false)
    private String resSenha;

    public Responsavel() {}

    public Responsavel(String resNome, String resTelefone, String resCpf, String resSenha) {
        this.resNome = resNome;
        this.resTelefone = resTelefone;
        this.resCpf = resCpf;
        this.resSenha = resSenha;
    }

    public Integer getResIdResponsavel() {
        return resIdResponsavel;
    }

    public void setResIdResponsavel(Integer resIdResponsavel) {
        this.resIdResponsavel = resIdResponsavel;
    }

    public String getResNome() {
        return resNome;
    }

    public void setResNome(String resNome) {
        this.resNome = resNome;
    }

    public String getResTelefone() {
        return resTelefone;
    }

    public void setResTelefone(String resTelefone) {
        this.resTelefone = resTelefone;
    }

    public String getResCpf() {
        return resCpf;
    }

    public void setResCpf(String resCpf) {
        this.resCpf = resCpf;
    }

    public String getResSenha() {
        return resSenha;
    }

    public void setResSenha(String resSenha) {
        this.resSenha = resSenha;
    }
}
