package com.bernardo.services;

import com.bernardo.beans.BuscaBean;
import com.bernardo.beans.GerBean;
import com.bernardo.beans.ResponsavelLogadoBean;
import com.bernardo.utils.JsfUtil;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import java.io.Serializable;

/**
 *
 * @author Bernardo Zardo Mergen
 */
public abstract class BaseCrud<T> implements Serializable {

    @EJB
    protected CustomEntityManager customEntityManager;

    @Inject
    private GerBean gerBean;
    @Inject
    private BuscaBean buscaBean;
    @Inject
    private ResponsavelLogadoBean usuarioLogadoBean;

    protected T crudObj;

    @PostConstruct
    public void init() {
        criaObj();
    }

    public abstract void criaObj();

    public void salvar() {
        customEntityManager.salvar(crudObj);
        criaObj();
        JsfUtil.info("Registro salvo com sucesso");
    }

    public void cancelar() {
        JsfUtil.refresh();
    }

    public void deletar() {
        try {
            customEntityManager.deletar(crudObj);
            criaObj();
            JsfUtil.info("Registro excluido com sucesso");
        } catch (Exception e) {
        	e.printStackTrace();
            JsfUtil.error("Não foi possível excluir o registro");
        }
    }

    public abstract void setObjetoCrudPesquisa();

    public GerBean getGerBean() {
        return gerBean;
    }

    public void setGerBean(GerBean gerBean) {
        this.gerBean = gerBean;
    }

    public BuscaBean getBuscaBean() {
        return buscaBean;
    }

    public void setBuscaBean(BuscaBean buscaBean) {
        this.buscaBean = buscaBean;
    }

    public ResponsavelLogadoBean getUsuarioLogadoBean() {
        return usuarioLogadoBean;
    }

    public void setUsuarioLogadoBean(ResponsavelLogadoBean usuarioLogadoBean) {
        this.usuarioLogadoBean = usuarioLogadoBean;
    }
}
