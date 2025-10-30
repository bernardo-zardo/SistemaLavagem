package com.bernardo.consultas;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.bernardo.entidades.Agendamento;
import com.bernardo.services.AgendamentoService;

/**
 *
 * @author Bernardo Zardo Mergen
 */
@Named
@ViewScoped
public class ConsultaAgendamentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private AgendamentoService agendamentoService;

	private ScheduleModel eventModel;
	private ScheduleEvent<?> eventoSelecionado;
	private Agendamento agendamentoSelecionado;

	@PostConstruct
	public void init() {
		eventModel = new DefaultScheduleModel();

		Map<String, Object> filtros = new HashMap<>();
		filtros.put("agNaoCancelado", true);
		List<Agendamento> agendamentos = agendamentoService.filtrar(filtros);

		for (Agendamento a : agendamentos) {
			LocalDateTime inicio = combinarDataHora(a.getAgData(), a.getAgHora());
			LocalDateTime fim = inicio.plusMinutes(40);

			    String classeCor;
			    String corFundo;
		        String corBorda;
		        if ("C".equalsIgnoreCase(a.getAgStatus())) {
		        	classeCor = "evento-concluido";
		            corFundo = "#1E3A8A"; 
		            corBorda = "#1E3A8A";
		        } else if ("P".equalsIgnoreCase(a.getAgStatus())) {
		        	classeCor = "evento-pendente";
		            corFundo = "#6299DE"; 
		            corBorda = "#6299DE";
		        } else {
		        	classeCor = "evento-outro";
		            corFundo = "#9CA3AF"; 
		            corBorda = "#9CA3AF";
		        }

		        DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
		            .title(a.getAgVeiculo().getVeiModelo() + " - " + a.getAgVeiculo().getVeiCliente().getCliNome())
		            .startDate(inicio)
		            .endDate(fim)
		            .data(a)
		            .styleClass(classeCor)
		            .borderColor(corBorda)
		            .backgroundColor(corFundo)
		            .build();

			eventModel.addEvent(event);
		}
	}

	public void onEventSelect(org.primefaces.event.SelectEvent<ScheduleEvent<?>> selectEvent) {
		eventoSelecionado = selectEvent.getObject();
		agendamentoSelecionado = (Agendamento) eventoSelecionado.getData();
	}

	private LocalDateTime combinarDataHora(Date data, Date hora) {
		if (data == null || hora == null)
			return null;

		ZoneId zone = ZoneId.of("America/Sao_Paulo");

		LocalDate localDate = data.toInstant().atZone(zone).toLocalDate();

		LocalTime localTime = hora.toInstant().atZone(zone).toLocalTime();

		return LocalDateTime.of(localDate, localTime);
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public Agendamento getAgendamentoSelecionado() {
		return agendamentoSelecionado;
	}
}
