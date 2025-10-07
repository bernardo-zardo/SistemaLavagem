package com.bernardo.services;

import com.bernardo.beans.BuscaBean;
import com.bernardo.beans.ResponsavelLogadoBean;
import com.bernardo.utils.JsfUtil;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bernardo Zardo Mergen
 */
public abstract class BasePesquisa<T> implements Serializable {

    private static final long serialVersionUID = 1L;

	@Inject
    protected BuscaBean buscaBean;

    @Inject
    private ResponsavelLogadoBean usuarioLogadoBean;

    protected List<T> registros = new ArrayList<>();

    protected List<T> registrosSelecionados = new ArrayList<>();
    private boolean multiplaSelecao;

    private final int rowsPadraoDataTable = 10;

    @PostConstruct
    public void init() {
        setMultiplaSelecaoFromParametros();
        iniciaFiltros();
        pesquisar();
    }

    private void setMultiplaSelecaoFromParametros() {
        multiplaSelecao = Boolean.parseBoolean(JsfUtil.getParam("multiplaSelecao"));
    }

    public abstract void pesquisar();

    public abstract void limparFiltros();

    public void finalizarBuscaMultiplaSelecao() {
        if (!registrosSelecionados.isEmpty()) {
            buscaBean.setObjetosSelecionados((List<Object>) registrosSelecionados);
        }
        buscaBean.finalizarBusca();
    }

    protected abstract void iniciaFiltros();

    public List<T> getRegistros() {
        return registros;
    }

    public void setRegistros(List<T> registros) {
        this.registros = registros;
    }

    public boolean isMultiplaSelecao() {
        return multiplaSelecao;
    }

    public void setMultiplaSelecao(boolean multiplaSelecao) {
        this.multiplaSelecao = multiplaSelecao;
    }

    public List<T> getRegistrosSelecionados() {
        return registrosSelecionados;
    }

    public void setRegistrosSelecionados(List<T> registrosSelecionados) {
        this.registrosSelecionados = registrosSelecionados;
    }

    public int getRowsPadraoDataTable() {
        return rowsPadraoDataTable;
    }

    public ResponsavelLogadoBean getUsuarioLogadoBean() {
        return usuarioLogadoBean;
    }

    public void setUsuarioLogadoBean(ResponsavelLogadoBean usuarioLogadoBean) {
        this.usuarioLogadoBean = usuarioLogadoBean;
    }
}
