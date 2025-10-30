package com.bernardo.beans.cadastros;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.bernardo.entidades.Agendamento;
import com.bernardo.entidades.TipoServico;
import com.bernardo.entidades.Veiculo;
import com.bernardo.services.AgendamentoService;
import com.bernardo.services.BaseCrud;
import com.bernardo.services.TipoServicoService;
import com.bernardo.services.VeiculoService;
import com.bernardo.utils.JsfUtil;

/**
 *
 * @author Bernardo Zardo Mergen
 */
@Named
@ViewScoped
public class AgendamentoBean extends BaseCrud<Agendamento> implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private VeiculoService veiculoService;
	@EJB
	private TipoServicoService tipoServicoService;
	@EJB
	private AgendamentoService agendamentoService;

	private boolean alterando;
	private List<Veiculo> veiculos;
	private List<TipoServico> tiposServico;
	private List<Agendamento> agendamentos;
	private List<Date> horariosDisponiveis;
	
	@PostConstruct
	public void montaRegistros() {
		agendamentos = agendamentoService.filtrar(new HashMap<>());
		veiculos = veiculoService.filtrar(new HashMap<>());
		tiposServico = tipoServicoService.filtrar(new HashMap<>());
	}

	@Override
	public void criaObj() {
		crudObj = new Agendamento();
		alterando = false;
	}

	@Override
	public void salvar() {
		if (alterando) {
			agendamentoService.salvar(crudObj);
			JsfUtil.info("Agendamento atualizado com sucesso!");
		} else {
			agendamentoService.salvar(crudObj);
			JsfUtil.info("Agendamento salvo com sucesso!");
		}
		agendamentos = agendamentoService.filtrar(new HashMap<>());
		criaObj();
	}

	@Override
	public void deletar() {
		agendamentoService.deletar(crudObj);
		criaObj();
		JsfUtil.info("Agendamento excluído com sucesso!");
		agendamentos = agendamentoService.filtrar(new HashMap<>());
	}

	public void selecionarAgendamento(Agendamento agendamento) {
		this.crudObj = agendamento;
		this.alterando = true;
		JsfUtil.info("Agendamento selecionado.");
	}

	public void excluirAgendamento(Agendamento agendamento) {
		this.crudObj = agendamento;
		agendamentoService.deletar(agendamento);
		criaObj();
		JsfUtil.info("Agendamento excluído com sucesso!");
		agendamentos = agendamentoService.filtrar(new HashMap<>());
	}
	
	public List<Date> gerarHorariosPadrao() {
	    List<Date> horarios = new ArrayList<>();
	    LocalTime[] horas = {
	        LocalTime.of(7, 0), LocalTime.of(7, 40), LocalTime.of(8, 20),
	        LocalTime.of(9, 0), LocalTime.of(9, 40), LocalTime.of(10, 20),
	        LocalTime.of(11, 0), LocalTime.of(11, 40),
	        LocalTime.of(13, 0), LocalTime.of(13, 40), LocalTime.of(14, 20),
	        LocalTime.of(15, 0), LocalTime.of(15, 40), LocalTime.of(16, 20),
	        LocalTime.of(17, 0), LocalTime.of(17, 40)
	    };

	    for (LocalTime lt : horas) {
	        horarios.add(java.sql.Time.valueOf(lt));
	    }

	    return horarios;
	}
	
	public String formatarHora(Date hora) {
	    if (hora == null) return "";
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	    return sdf.format(hora);
	}
	
	public void carregarHorariosDisponiveis() {
	    if (crudObj.getAgData() == null) {
	        horariosDisponiveis = new ArrayList<>();
	        return;
	    }

	    List<Date> horariosPadrao = gerarHorariosPadrao();

	    List<Date> horariosOcupados = agendamentoService.buscarHorariosOcupadosPorData(crudObj.getAgData());

	    horariosDisponiveis = horariosPadrao.stream()
	            .filter(h -> horariosOcupados.stream().noneMatch(o -> o.equals(h)))
	            .collect(Collectors.toList());
	}


	public Agendamento getCrudObj() {
		return crudObj;
	}

	public boolean isAlterando() {
		return alterando;
	}

	public void setAlterando(boolean alterando) {
		this.alterando = alterando;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public List<TipoServico> getTiposServico() {
		return tiposServico;
	}

	public void setTiposServico(List<TipoServico> tiposServico) {
		this.tiposServico = tiposServico;
	}

	public List<Agendamento> getAgendamentos() {
		return agendamentos;
	}

	public void setAgendamentos(List<Agendamento> agendamentos) {
		this.agendamentos = agendamentos;
	}

	public List<Date> getHorariosDisponiveis() {
		return horariosDisponiveis;
	}

	public void setHorariosDisponiveis(List<Date> horariosDisponiveis) {
		this.horariosDisponiveis = horariosDisponiveis;
	}
}
