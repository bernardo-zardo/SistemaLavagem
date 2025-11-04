package com.bernardo.consultas;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.bernardo.entidades.Agendamento;
import com.bernardo.entidades.Responsavel;
import com.bernardo.entidades.Servico;
import com.bernardo.entidades.TipoServico;
import com.bernardo.entidades.Veiculo;
import com.bernardo.services.AgendamentoService;
import com.bernardo.services.ResponsavelService;
import com.bernardo.services.ServicoService;
import com.bernardo.services.TipoServicoService;
import com.bernardo.services.VeiculoService;
import com.bernardo.utils.JsfUtil;

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
	@EJB
	private VeiculoService veiculoService;
	@EJB
	private TipoServicoService tipoServicoService;
	@EJB
	private ResponsavelService responsavelService;
	@EJB
	private ServicoService servicoService;

	private List<Veiculo> veiculos;
	private List<TipoServico> tiposServico;
	private List<Responsavel> responsaveis;

	private ScheduleModel eventModel;
	private ScheduleEvent<?> eventoSelecionado;
	private Agendamento agendamentoSelecionado;

	private Date filtroDataIni;
	private Date filtroDataFim;
	private List<TipoServico> filtroTiposServico;
	private List<Veiculo> filtroVeiculos;
	private String filtroStatus = "P";

	private Responsavel responsavelSelecionado;

	private List<Agendamento> agendamentosFiltrados = new ArrayList<>();

	@PostConstruct
	public void init() {
		veiculos = veiculoService.filtrar(new HashMap<>());
		tiposServico = tipoServicoService.filtrar(new HashMap<>());
		responsaveis = responsavelService.filtrar(new HashMap<>());

		LocalDate hoje = LocalDate.now();
		LocalDate seteDiasAtras = hoje.minusDays(7);
		filtroDataIni = Date.from(seteDiasAtras.atStartOfDay(ZoneId.systemDefault()).toInstant());
		filtroDataFim = Date.from(hoje.atStartOfDay(ZoneId.systemDefault()).toInstant());

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
					.startDate(inicio).endDate(fim).data(a).styleClass(classeCor).borderColor(corBorda)
					.backgroundColor(corFundo).build();

			eventModel.addEvent(event);
		}
	}

	public void filtrarAgendamentos() {
		if (filtroDataIni.after(filtroDataFim)) {
			JsfUtil.warn("A data de início não pode ser maior que a data fim.");
			return;
		}
		agendamentosFiltrados = agendamentoService.consultarAgendamentosFiltrados(filtroStatus, filtroDataIni,
				filtroDataFim, filtroTiposServico, filtroVeiculos);
		agendamentosFiltrados.sort(Comparator.comparing(Agendamento::getAgIdAgendamento).reversed());
	}

	public void limparFiltros() {
		LocalDate hoje = LocalDate.now();
		LocalDate seteDiasAtras = hoje.minusDays(7);
		filtroDataIni = Date.from(seteDiasAtras.atStartOfDay(ZoneId.systemDefault()).toInstant());
		filtroDataFim = Date.from(hoje.atStartOfDay(ZoneId.systemDefault()).toInstant());
		filtroTiposServico = new ArrayList<>();
		filtroVeiculos = new ArrayList<>();
		filtroStatus = "P";
	}

	public void onEventSelect(org.primefaces.event.SelectEvent<ScheduleEvent<?>> selectEvent) {
		eventoSelecionado = selectEvent.getObject();
		agendamentoSelecionado = (Agendamento) eventoSelecionado.getData();

		PrimeFaces.current().ajax().addCallbackParam("temCoordenadas", true);

		if (agendamentoSelecionado != null) {
			if (agendamentoSelecionado.latBusca() != null && agendamentoSelecionado.lngBusca() != null) {
				PrimeFaces.current().ajax().addCallbackParam("latBusca", agendamentoSelecionado.latBusca().toString());
				PrimeFaces.current().ajax().addCallbackParam("lngBusca", agendamentoSelecionado.lngBusca().toString());
			}

			if (agendamentoSelecionado.latEntrega() != null && agendamentoSelecionado.lngEntrega() != null) {
				PrimeFaces.current().ajax().addCallbackParam("latEntrega",
						agendamentoSelecionado.latEntrega().toString());
				PrimeFaces.current().ajax().addCallbackParam("lngEntrega",
						agendamentoSelecionado.lngEntrega().toString());
			}
		}
	}

	private LocalDateTime combinarDataHora(Date data, Date hora) {
		if (data == null || hora == null)
			return null;

		ZoneId zone = ZoneId.of("America/Sao_Paulo");

		LocalDate localDate = data.toInstant().atZone(zone).toLocalDate();

		LocalTime localTime = hora.toInstant().atZone(zone).toLocalTime();

		return LocalDateTime.of(localDate, localTime);
	}

	public void prepararConclusao(Agendamento a) {
		this.agendamentoSelecionado = a;
	}

	public void cancelarAgendamento(Agendamento a) {
		a.setAgStatus("X");
		agendamentoService.salvar(a);
		JsfUtil.info("Agendamento cancelado com sucesso.");
		filtrarAgendamentos();
	}

	public void concluirServico() {
		if (agendamentoSelecionado == null || responsavelSelecionado == null) {
			JsfUtil.warn("Selecione um responsável antes de concluir.");
			return;
		}

		Servico s = new Servico();
		s.setSerData(agendamentoSelecionado.getAgData());
		s.setSerTipoServico(agendamentoSelecionado.getAgTipoServico());
		s.setSerVeiculo(agendamentoSelecionado.getAgVeiculo());
		s.setSerResponsavel(responsavelSelecionado);
		s.setSerEnderecoBusca(agendamentoSelecionado.getAgEnderecoBusca());
		s.setSerEnderecoEntrega(agendamentoSelecionado.getAgEnderecoEntrega());
		s.setSerPossuiBuscaVeiculo(agendamentoSelecionado.isAgPossuiBuscaVeiculo());
		s.setSerPossuiEntregaVeiculo(agendamentoSelecionado.isAgPossuiEntregaVeiculo());
		s.setSerPrecoServicoExtra(agendamentoSelecionado.getAgPrecoServicoExtra());
		s.setSerPrecoTotal(agendamentoSelecionado.getAgTipoServico().getTsPreco()
				+ agendamentoSelecionado.getAgPrecoServicoExtra());

		servicoService.salvar(s);

		agendamentoSelecionado.setAgStatus("C");
		agendamentoService.salvar(agendamentoSelecionado);

		JsfUtil.info("Serviço concluído e registrado com sucesso.");
		filtrarAgendamentos();
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public Agendamento getAgendamentoSelecionado() {
		return agendamentoSelecionado;
	}

	public String getFiltroStatus() {
		return filtroStatus;
	}

	public void setFiltroStatus(String filtroStatus) {
		this.filtroStatus = filtroStatus;
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

	public List<Agendamento> getAgendamentosFiltrados() {
		return agendamentosFiltrados;
	}

	public void setAgendamentosFiltrados(List<Agendamento> agendamentosFiltrados) {
		this.agendamentosFiltrados = agendamentosFiltrados;
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

	public List<Responsavel> getResponsaveis() {
		return responsaveis;
	}

	public void setResponsaveis(List<Responsavel> responsaveis) {
		this.responsaveis = responsaveis;
	}

	public Responsavel getResponsavelSelecionado() {
		return responsavelSelecionado;
	}

	public void setResponsavelSelecionado(Responsavel responsavelSelecionado) {
		this.responsavelSelecionado = responsavelSelecionado;
	}
}
