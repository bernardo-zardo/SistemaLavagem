package com.bernardo.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import com.bernardo.services.ServicoService;

@Named
@ViewScoped
public class DashboardBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private ServicoService servicoService;

	private DonutChartModel donutModel;
	private BarChartModel barModel;
	private BarChartModel faturamentoDiarioModel;

	private String crescimentoMensalCor;

	@PostConstruct
	public void init() {
		createDonutModel();
		createBarModel();
		createFaturamentoDiarioModel();
	}

	private void createDonutModel() {
		donutModel = new DonutChartModel();
		ChartData data = new ChartData();

		DonutChartDataSet dataSet = new DonutChartDataSet();
		dataSet.setData(Arrays.asList(120, 90, 50));
		dataSet.setBackgroundColor(Arrays.asList("#42A5F5", "#66BB6A", "#FFA726"));
		data.addChartDataSet(dataSet);

		data.setLabels(Arrays.asList("Lavagem simples", "Lavagem completa", "Polimento"));
		donutModel.setData(data);

		DonutChartOptions options = new DonutChartOptions();

		Title title = new Title();
		title.setDisplay(true);
		title.setText("Serviços por Tipo");
		options.setTitle(title);

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

	    dataSet.setLabel("Faturamento Diário");
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

	public String getCrescimentoMensalCor() {
		return crescimentoMensalCor;
	}

	public DonutChartModel getDonutModel() {
		return donutModel;
	}

	public BarChartModel getBarModel() {
		return barModel;
	}
}
