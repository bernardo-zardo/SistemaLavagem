package com.bernardo.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.bernardo.entidades.Agendamento;
import com.bernardo.services.AgendamentoService;
import com.bernardo.utils.JsfUtil;

@Named
@ViewScoped
public class AgendamentoClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioLogadoBean usuarioLogado;
	@EJB
	private AgendamentoService agendamentoService;

	private List<Agendamento> agendamentosFiltrados = new ArrayList<>();
	private List<String> tiposServicoCliente;

	private Agendamento agendamento = new Agendamento();
	private List<Date> horariosDisponiveis;

	@PostConstruct
	public void init() {
		filtrarAgendamentos();
		tiposServicoCliente = Arrays.asList("Lavagem Completa (R$ 60,00 a R$ 80,00)",
				"Lavagem Completa + Cera (R$ 100,00 a R$ 120,00)", "Lavagem Externa (R$ 40,00 a R$ 50,00)",
				"Lavagem Interna (R$ 30,00)", "Lavagem Motor Completa (R$ 120,00)", "Lavagem Motor Básica (R$ 50,00)",
				"Lavagem Moto (R$ 20,00)");
	}

	public void cancelarAgendamento(Agendamento a) {
		a.setAgStatus("X");
		agendamentoService.salvar(a);
		JsfUtil.info("Agendamento cancelado com sucesso.");
		filtrarAgendamentos();
	}

	public void filtrarAgendamentos() {
		agendamentosFiltrados = agendamentoService
				.consultarAgendamentosCliente(usuarioLogado.getClienteLogado().getCliIdCliente());
		agendamentosFiltrados.sort(Comparator.comparing(Agendamento::getAgIdAgendamento).reversed());
	}

	public List<Date> gerarHorariosPadrao() {
		List<Date> horarios = new ArrayList<>();
		LocalTime[] horas = { LocalTime.of(7, 0), LocalTime.of(7, 40), LocalTime.of(8, 20), LocalTime.of(9, 0),
				LocalTime.of(9, 40), LocalTime.of(10, 20), LocalTime.of(11, 0), LocalTime.of(11, 40),
				LocalTime.of(13, 0), LocalTime.of(13, 40), LocalTime.of(14, 20), LocalTime.of(15, 0),
				LocalTime.of(15, 40), LocalTime.of(16, 20), LocalTime.of(17, 0), LocalTime.of(17, 40) };

		for (LocalTime lt : horas) {
			horarios.add(java.sql.Time.valueOf(lt));
		}

		return horarios;
	}

	public String formatarHora(Date hora) {
		if (hora == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(hora);
	}

	public void abrirNovoAgendamento() {
		agendamento = new Agendamento();
		agendamento.setAgStatus("P");
		agendamento.setAgPossuiBuscaVeiculo(false);
		agendamento.setAgPossuiEntregaVeiculo(false);
		agendamento.setAgPrecoServicoExtra(null);
		horariosDisponiveis = new ArrayList<>();
	}

	public void carregarHorariosDisponiveis() {
		if (agendamento.getAgData() == null) {
			horariosDisponiveis = new ArrayList<>();
			return;
		}

		List<Date> horariosPadrao = gerarHorariosPadrao();

		List<Date> horariosOcupados = agendamentoService.buscarHorariosOcupadosPorData(agendamento.getAgData());

		horariosDisponiveis = horariosPadrao.stream().filter(h -> horariosOcupados.stream().noneMatch(o -> o.equals(h)))
				.collect(Collectors.toList());
	}

	public void salvarNovoAgendamento() {
		if (agendamento.getAgData() == null) {
			JsfUtil.warn("Selecione uma data.");
			return;
		}

		if (agendamento.getAgHora() == null) {
			JsfUtil.warn("Selecione um horário.");
			return;
		}

		if (agendamento.getAgObservacao() == null || agendamento.getAgObservacao().isBlank()) {
			JsfUtil.warn("Selecione um tipo de serviço.");
			return;
		}

		if (agendamento.getAgVeiculo() == null) {
			JsfUtil.warn("Selecione um veículo.");
			return;
		}

		if (agendamento.isAgPossuiBuscaVeiculo() && agendamento.getAgEnderecoBusca() == null) {
			JsfUtil.warn("Selecione um endereço de busca.");
			return;
		}

		if (agendamento.isAgPossuiEntregaVeiculo() && agendamento.getAgEnderecoEntrega() == null) {
			JsfUtil.warn("Selecione um endereço de entrega.");
			return;
		}

		try {
			agendamentoService.salvar(agendamento);

			JsfUtil.info("Agendamento salvo com sucesso!");

			agendamento = new Agendamento();

		} catch (Exception e) {
			JsfUtil.error("Erro ao salvar agendamento.");
		}
		filtrarAgendamentos();
	}

	public List<Agendamento> getAgendamentosFiltrados() {
		return agendamentosFiltrados;
	}

	public void setAgendamentosFiltrados(List<Agendamento> agendamentosFiltrados) {
		this.agendamentosFiltrados = agendamentosFiltrados;
	}

	public Agendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(Agendamento agendamento) {
		this.agendamento = agendamento;
	}

	public List<Date> getHorariosDisponiveis() {
		return horariosDisponiveis;
	}

	public void setHorariosDisponiveis(List<Date> horariosDisponiveis) {
		this.horariosDisponiveis = horariosDisponiveis;
	}

	public List<String> getTiposServicoCliente() {
		return tiposServicoCliente;
	}

	public void setTiposServicoCliente(List<String> tiposServicoCliente) {
		this.tiposServicoCliente = tiposServicoCliente;
	}
}
