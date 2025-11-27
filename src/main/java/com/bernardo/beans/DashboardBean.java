package com.bernardo.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.AxesGridLines;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.donut.DonutChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.title.Title;

import com.bernardo.services.AgendamentoService;
import com.bernardo.services.ServicoService;
import com.bernardo.services.TipoServicoService;
import com.bernardo.services.VeiculoService;
import com.bernardo.utils.AgendamentoResumoAux;
import com.bernardo.utils.EmailService;
import com.bernardo.utils.ServicoResumoAux;

/**
*
* @author Bernardo Zardo Mergen
*/
@Named
@ViewScoped
public class DashboardBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private ServicoService servicoService;
	@EJB
	private VeiculoService veiculoService;
	@EJB
	private AgendamentoService agendamentoService;
	@EJB
	private TipoServicoService tipoServicoService;

	private DonutChartModel donutModel;
	private BarChartModel barModel;
	private BarChartModel faturamentoDiarioModel;

	private int totalServicos;
	private double totalFinanceiro;
	private int totalVeiculos;
	private double crescimentoMes;
	
	private List<AgendamentoResumoAux> proximosAgendamentos = new ArrayList<>();

	@PostConstruct
	public void init() {
		createDonutModel();
		createBarModel();
		createFaturamentoDiarioModel();
		atualizarTotaisDashboard();
		proximosAgendamentos = agendamentoService.buscarProximosAgendamentos();
	}
	
	private void createDonutModel() {
	    List<ServicoResumoAux> servicos = tipoServicoService.buscarServicosPorTipoNoMes();

	    donutModel = new DonutChartModel();
	    ChartData data = new ChartData();
	    DonutChartDataSet dataSet = new DonutChartDataSet();

	    List<Number> valores = servicos.stream()
	        .map(ServicoResumoAux::getTotal)
	        .collect(Collectors.toList());

	    List<String> labels = servicos.stream()
	        .map(ServicoResumoAux::getTipoServico)
	        .collect(Collectors.toList());

	    List<String> cores = Arrays.asList(
	        "#007BFF", "#093e80", "#1a96cc", "#42D3F2", "#74D4FF", "#1447E6",
	        "#21BCFF", "#2984D1", "#015F78", "#4169E1", "#162456", "#2C92B8",
	        "#155DFC", "#A2F4FD", "#0096C7"
	    );

	    dataSet.setData(valores);
	    dataSet.setBackgroundColor(cores.subList(0, Math.min(cores.size(), valores.size())));
	    data.addChartDataSet(dataSet);
	    data.setLabels(labels);
	    donutModel.setData(data);

	    DonutChartOptions options = new DonutChartOptions();
	    Legend legend = new Legend();
	    legend.setDisplay(false);

	    options.setLegend(legend);
	    donutModel.setOptions(options);
	}


	private void createBarModel() {
		barModel = new BarChartModel();
		ChartData data = new ChartData();

		List<Object[]> resultados = servicoService.consultarTotaisUltimos12Meses();

		List<Number> listaServicos = new ArrayList<>();
		List<Number> listaGanhos = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		Map<String, Object[]> mapaMeses = new LinkedHashMap<>();
		LocalDate hoje = LocalDate.now();
		for (int i = 11; i >= 0; i--) {
			LocalDate dataMes = hoje.minusMonths(i);
			String chave = dataMes.getYear() + "-" + String.format("%02d", dataMes.getMonthValue());
			mapaMeses.put(chave, new Object[] { dataMes.getYear(), dataMes.getMonthValue(), 0L, BigDecimal.ZERO });
		}

		for (Object[] r : resultados) {
			int ano = ((Number) r[0]).intValue();
			int mes = ((Number) r[1]).intValue();
			long totalServicos = ((Number) r[2]).longValue();
			BigDecimal totalGanhos = (BigDecimal) r[3];
			String chave = ano + "-" + String.format("%02d", mes);
			mapaMeses.put(chave, new Object[] { ano, mes, totalServicos, totalGanhos });
		}

		for (Object[] v : mapaMeses.values()) {
			int mes = (int) v[1];
			listaServicos.add((Number) v[2]);
			listaGanhos.add((BigDecimal) v[3]);
			labels.add(getNomeMesCurto(mes));
		}

		BarChartDataSet ganhosDataSet = new BarChartDataSet();
		ganhosDataSet.setLabel("Ganhos (R$)");
		ganhosDataSet.setBackgroundColor("#1a96cc");
		ganhosDataSet.setData(listaGanhos);
		ganhosDataSet.setYaxisID("y1"); 

		BarChartDataSet servicosDataSet = new BarChartDataSet();
		servicosDataSet.setLabel("Serviços");
		servicosDataSet.setBackgroundColor("#093e80");
		servicosDataSet.setData(listaServicos);
		servicosDataSet.setYaxisID("y2"); 

		data.addChartDataSet(ganhosDataSet);
		data.addChartDataSet(servicosDataSet);
		data.setLabels(labels);
		barModel.setData(data);

		BarChartOptions options = new BarChartOptions();

		CartesianScales scales = new CartesianScales();

		CartesianLinearAxes yAxis1 = new CartesianLinearAxes();
		yAxis1.setId("y1");
		yAxis1.setBeginAtZero(true);
		yAxis1.setPosition("left");
		AxesGridLines grid1 = new AxesGridLines();
		grid1.setDrawOnChartArea(true);
		yAxis1.setGrid(grid1);

		CartesianLinearAxes yAxis2 = new CartesianLinearAxes();
		yAxis2.setId("y2");
		yAxis2.setBeginAtZero(true);
		yAxis2.setPosition("right");
		AxesGridLines grid2 = new AxesGridLines();
		grid2.setDrawOnChartArea(false);
		yAxis2.setGrid(grid2);

		scales.addYAxesData(yAxis1);
		scales.addYAxesData(yAxis2);
		options.setScales(scales);

		Legend legend = new Legend();
		legend.setDisplay(true);
		legend.setPosition("bottom");
		options.setLegend(legend);

		barModel.setOptions(options);
	}
	
	public void atualizarTotaisDashboard() {
	    try {
	        Long totalServicosMes = servicoService.contarServicosMesAtual();
	        totalServicos = totalServicosMes != null ? totalServicosMes.intValue() : 0;

	        BigDecimal receitaMesAtual = servicoService.consultarFaturamentoMesAtual();
	        totalFinanceiro = receitaMesAtual != null ? receitaMesAtual.doubleValue() : 0;

	        Long totalVeiculosCadastrados = veiculoService.contarVeiculosCadastrados();
	        totalVeiculos = totalVeiculosCadastrados != null ? totalVeiculosCadastrados.intValue() : 0;

	        BigDecimal receitaMesAnterior = servicoService.consultarFaturamentoMesAnterior();

	        if (receitaMesAnterior != null && receitaMesAnterior.compareTo(BigDecimal.ZERO) > 0) {
	            BigDecimal diferenca = receitaMesAtual.subtract(receitaMesAnterior);
	            BigDecimal crescimentoPercentual = diferenca
	                    .divide(receitaMesAnterior, 4, RoundingMode.HALF_UP)
	                    .multiply(BigDecimal.valueOf(100));

	            crescimentoMes = crescimentoPercentual.doubleValue();
	        } else {
	            crescimentoMes = 0.0;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        totalServicos = 0;
	        totalFinanceiro = 0;
	        totalVeiculos = 0;
	        crescimentoMes = 0;
	    }
	}

	private void createFaturamentoDiarioModel() {
	    faturamentoDiarioModel = new BarChartModel();
	    ChartData data = new ChartData();

	    BarChartDataSet dataSet = new BarChartDataSet();

	    List<String> dias = new ArrayList<>();
	    List<Number> valores = new ArrayList<>();

	    List<Object[]> resultados = servicoService.buscarFaturamentoDiarioMesAtual();
	    if (resultados != null && !resultados.isEmpty()) {
	        for (Object[] linha : resultados) {
	            String diaFormatado = String.valueOf(linha[0]);

	            Number valorNum = 0;
	            if (linha[1] instanceof Number) {
	                valorNum = (Number) linha[1];
	            } else if (linha[1] instanceof BigDecimal) {
	                valorNum = ((BigDecimal) linha[1]).doubleValue();
	            } else if (linha[1] != null) {
	                try {
	                    valorNum = Double.valueOf(String.valueOf(linha[1]));
	                } catch (NumberFormatException e) {
	                    valorNum = 0;
	                }
	            }

	            dias.add(diaFormatado);
	            valores.add(valorNum);
	        }
	    } else {
	        dias.add("Sem dados");
	        valores.add(0);
	    }

	    dataSet.setLabel("Faturamento Diário (R$)");
	    dataSet.setBackgroundColor("#093e80");

	    dataSet.setData(valores);

	    data.addChartDataSet(dataSet);
	    data.setLabels(dias);

	    faturamentoDiarioModel.setData(data);


	    CartesianScales scales = new CartesianScales();
	    CartesianLinearAxes yAxis = new CartesianLinearAxes();
	    yAxis.setBeginAtZero(true);
	    scales.addYAxesData(yAxis);
	    
	    BarChartOptions options = new BarChartOptions();
	    Legend legend = new Legend();
		legend.setDisplay(true);
		legend.setPosition("bottom");
		options.setLegend(legend);

		faturamentoDiarioModel.setOptions(options);
	}

	private String getNomeMesCurto(int mes) {
		String[] nomes = { "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez" };
		return nomes[mes - 1];
	}

	public BarChartModel getFaturamentoDiarioModel() {
		return faturamentoDiarioModel;
	}

	public DonutChartModel getDonutModel() {
		return donutModel;
	}

	public BarChartModel getBarModel() {
		return barModel;
	}

	public int getTotalServicos() {
		return totalServicos;
	}

	public void setTotalServicos(int totalServicos) {
		this.totalServicos = totalServicos;
	}

	public double getTotalFinanceiro() {
		return totalFinanceiro;
	}

	public void setTotalFinanceiro(double totalFinanceiro) {
		this.totalFinanceiro = totalFinanceiro;
	}
	
	public String getTotalFinanceiroFormat() {
    	NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
	    return nf.format(this.totalFinanceiro);
    }
	
	public int getTotalVeiculos() {
		return totalVeiculos;
	}

	public void setTotalVeiculos(int totalVeiculos) {
		this.totalVeiculos = totalVeiculos;
	}

	public double getCrescimentoMes() {
		return crescimentoMes;
	}

	public void setCrescimentoMes(double crescimentoMes) {
		this.crescimentoMes = crescimentoMes;
	}

	public List<AgendamentoResumoAux> getProximosAgendamentos() {
		return proximosAgendamentos;
	}

	public void setProximosAgendamentos(List<AgendamentoResumoAux> proximosAgendamentos) {
		this.proximosAgendamentos = proximosAgendamentos;
	}
}
