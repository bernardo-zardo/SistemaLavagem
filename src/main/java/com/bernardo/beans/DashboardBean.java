package com.bernardo.beans;

import java.io.Serializable;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.donut.DonutChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.title.Title;

@Named
@ViewScoped
public class DashboardBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private BarChartModel barModel;
	private DonutChartModel donutModel;

	@PostConstruct
	public void init() {
		createBarModel();
		createDonutModel();
	}

	private void createBarModel() {
		barModel = new BarChartModel();
		ChartData data = new ChartData();

		BarChartDataSet ganhos = new BarChartDataSet();
		ganhos.setLabel("Ganhos Financeiros (R$)");
		ganhos.setBackgroundColor("rgba(54, 162, 235, 0.6)");
		ganhos.setBorderColor("rgb(54, 162, 235)");
		ganhos.setBorderWidth(1);
		ganhos.setData(Arrays.asList(1200, 900, 1500, 2000, 1800, 2100, 2500, 2300, 2600, 3000, 2800, 3200));

		BarChartDataSet servicos = new BarChartDataSet();
		servicos.setLabel("Serviços Realizados");
		servicos.setBackgroundColor("rgba(255, 99, 132, 0.6)");
		servicos.setBorderColor("rgb(255, 99, 132)");
		servicos.setBorderWidth(1);
		servicos.setData(Arrays.asList(40, 35, 45, 50, 48, 52, 60, 58, 63, 70, 68, 75));

		data.addChartDataSet(ganhos);
		data.addChartDataSet(servicos);
		data.setLabels(
				Arrays.asList("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"));
		barModel.setData(data);

		BarChartOptions options = new BarChartOptions();
		Title title = new Title();
		title.setDisplay(true);
		title.setText("Ganhos e Serviços - Últimos 12 Meses");
		options.setTitle(title);

		Legend legend = new Legend();
		legend.setDisplay(true);
		legend.setPosition("top");
		options.setLegend(legend);

		CartesianScales scales = new CartesianScales();
		CartesianLinearAxes yAxis = new CartesianLinearAxes();
		yAxis.setBeginAtZero(true);
		scales.addYAxesData(yAxis);
		options.setScales(scales);

		barModel.setOptions(options);
	}

	private void createDonutModel() {
		donutModel = new DonutChartModel();
		ChartData data = new ChartData();

		DonutChartDataSet dataSet = new DonutChartDataSet();
		dataSet.setData(Arrays.asList(120, 90, 60, 30));
		dataSet.setBackgroundColor(Arrays.asList("#36A2EB", "#FF6384", "#FFCE56", "#4BC0C0"));
		data.addChartDataSet(dataSet);

		data.setLabels(Arrays.asList("Lavagem Simples", "Lavagem Completa", "Polimento", "Higienização Interna"));
		donutModel.setData(data);

		DonutChartOptions options = new DonutChartOptions();
		Title title = new Title();
		title.setDisplay(true);
		title.setText("Serviços Realizados por Tipo");
		options.setTitle(title);

		donutModel.setOptions(options);
	}

	public BarChartModel getBarModel() {
		return barModel;
	}

	public DonutChartModel getDonutModel() {
		return donutModel;
	}
}
