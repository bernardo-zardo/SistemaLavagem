package com.bernardo.beans.cadastros;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.bernardo.entidades.TipoServico;
import com.bernardo.services.BaseCrud;
import com.bernardo.services.TipoServicoService;
import com.bernardo.utils.JsfUtil;

/**
*
* @author Bernardo Zardo Mergen
*/
@Named
@ViewScoped
public class TipoServicoBean extends BaseCrud<TipoServico> implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private TipoServicoService tipoServicoService;

    private boolean alterando;
    private List<TipoServico> tiposServico;
    
    @PostConstruct
    public void montaRegistros() {
    	tiposServico = tipoServicoService.filtrar(new HashMap<>());
    }
    
    @Override
    public void criaObj() {
        crudObj = new TipoServico();
        alterando = false;
    }

    @Override
    public void salvar() {
        if (alterando) {
        	tipoServicoService.salvar(crudObj);
            JsfUtil.info("Tipo de Serviço atualizado com sucesso!");
        } else {
        	tipoServicoService.salvar(crudObj);
                JsfUtil.info("Veículo salvo com sucesso!");
        }
        tiposServico = tipoServicoService.filtrar(new HashMap<>());
        criaObj();
    }
    
    @Override
    public void deletar() {
    	tipoServicoService.deletar(crudObj);
    	criaObj();
    	JsfUtil.info("Tipo de Serviço excluído com sucesso!");
    	tiposServico = tipoServicoService.filtrar(new HashMap<>());
    }

    public void selecionarTipoServico(TipoServico tipoServico) {
        this.crudObj = tipoServico;
        this.alterando = true;
        JsfUtil.info("Tipo de Serviço selecionado.");
    }
    
    public void excluirTipoServico(TipoServico tipoServico) {
        this.crudObj = tipoServico;
        tipoServicoService.deletar(tipoServico);
        criaObj();
        JsfUtil.info("Tipo de Serviço excluído com sucesso!");
        tiposServico = tipoServicoService.filtrar(new HashMap<>());
    }
    
    public TipoServico getCrudObj() {
        return crudObj;
    }

    public boolean isAlterando() {
        return alterando;
    }

    public void setAlterando(boolean alterando) {
        this.alterando = alterando;
    }

	public List<TipoServico> getTiposServico() {
		return tiposServico;
	}

	public void setTiposServico(List<TipoServico> tiposServico) {
		this.tiposServico = tiposServico;
	}
}
