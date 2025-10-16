package com.bernardo.consultas;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.bernardo.entidades.Responsavel;
import com.bernardo.entidades.Servico;
import com.bernardo.entidades.TipoServico;
import com.bernardo.entidades.Veiculo;
import com.bernardo.services.ClienteService;
import com.bernardo.services.ResponsavelService;
import com.bernardo.services.ServicoService;
import com.bernardo.services.TipoServicoService;
import com.bernardo.services.VeiculoService;

/**
*
* @author Bernardo Zardo Mergen
*/
@Named
@ViewScoped
public class ConsultaServicoBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EJB
    private ServicoService servicoService;
    @EJB
    private VeiculoService veiculoService;
    @EJB
    private ClienteService clienteService;
    @EJB
    private ResponsavelService responsavelService;
    @EJB
    private TipoServicoService tipoServicoService;

    private List<Veiculo> veiculos;
    private List<Responsavel> responsaveis;
    private List<Servico> servicos;
    private List<TipoServico> tiposServico;
    
    private String filtroEntrega = "A";
    private Date filtroDataIni;
    private Date filtroDataFim;
    private List<TipoServico> filtroTiposServico;
    private List<Veiculo> filtroVeiculos;
    private List<Responsavel> filtroResponsaveis;
    
    private List<Servico> servicosFiltrados = new ArrayList<>();
    
    @PostConstruct
    public void montaRegistros() {
    	LocalDate hoje = LocalDate.now();
        LocalDate seteDiasAtras = hoje.minusDays(7);
        filtroDataIni = Date.from(seteDiasAtras.atStartOfDay(ZoneId.systemDefault()).toInstant());
        filtroDataFim = Date.from(hoje.atStartOfDay(ZoneId.systemDefault()).toInstant());
    	
    	veiculos = veiculoService.filtrar(new HashMap<>());
    	responsaveis = responsavelService.filtrar(new HashMap<>());
    	servicos = servicoService.filtrar(new HashMap<>());
    	tiposServico = tipoServicoService.filtrar(new HashMap<>());
    }
    
    public void filtrarServicos() {
    	servicosFiltrados = servicoService.consultarServicosFiltrados(filtroEntrega, filtroDataIni, filtroDataFim, filtroTiposServico, filtroVeiculos, filtroResponsaveis);
    	for (Servico serv : servicosFiltrados) {
			System.out.println(serv.getSerIdServico());
		}
    }
    
    public void limparFiltros () {
    	
    }
    
	public Date getFiltroDataIni() {
		return filtroDataIni;
	}

	public void setFiltroDataIni(Date filtroDataIni) {
		this.filtroDataIni = filtroDataIni;
	}

	public Date getFiltroDataFim() {
		return filtroDataFim;
	}

	public void setFiltroDataFim(Date filtroDataFim) {
		this.filtroDataFim = filtroDataFim;
	}

	public List<TipoServico> getFiltroTiposServico() {
		return filtroTiposServico;
	}

	public void setFiltroTiposServico(List<TipoServico> filtroTiposServico) {
		this.filtroTiposServico = filtroTiposServico;
	}

	public List<Veiculo> getFiltroVeiculos() {
		return filtroVeiculos;
	}

	public void setFiltroVeiculos(List<Veiculo> filtroVeiculos) {
		this.filtroVeiculos = filtroVeiculos;
	}
	
	public List<Responsavel> getFiltroResponsaveis() {
		return filtroResponsaveis;
	}

	public void setFiltroResponsaveis(List<Responsavel> filtroResponsaveis) {
		this.filtroResponsaveis = filtroResponsaveis;
	}
	
	public String getFiltroEntrega() {
		return filtroEntrega;
	}

	public void setFiltroEntrega(String filtroEntrega) {
		this.filtroEntrega = filtroEntrega;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public List<Responsavel> getResponsaveis() {
		return responsaveis;
	}

	public void setResponsaveis(List<Responsavel> responsaveis) {
		this.responsaveis = responsaveis;
	}

	public List<Servico> getServicos() {
		return servicos;
	}

	public void setServicos(List<Servico> servicos) {
		this.servicos = servicos;
	}

	public List<TipoServico> getTiposServico() {
		return tiposServico;
	}

	public void setTiposServico(List<TipoServico> tiposServico) {
		this.tiposServico = tiposServico;
	}

	public List<Servico> getServicosFiltrados() {
		return servicosFiltrados;
	}

	public void setServicosFiltrados(List<Servico> servicosFiltrados) {
		this.servicosFiltrados = servicosFiltrados;
	}
}
