package com.bernardo.beans;

import java.io.Serializable;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.donut.DonutChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

@Named
@ViewScoped
public class DashboardBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DonutChartModel donutModel;
    private BarChartModel barModel;

    @PostConstruct
    public void init() {
        createDonutModel();
        createBarModel();
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

        BarChartDataSet ganhosDataSet = new BarChartDataSet();
        ganhosDataSet.setLabel("Ganhos (R$)");
        ganhosDataSet.setBackgroundColor("#42A5F5");
        ganhosDataSet.setData(Arrays.asList(1200, 1500, 800, 1800, 2000, 2200, 1700, 2500, 2300, 2100, 2600, 2800));

        BarChartDataSet servicosDataSet = new BarChartDataSet();
        servicosDataSet.setLabel("Serviços");
        servicosDataSet.setBackgroundColor("#66BB6A");
        servicosDataSet.setData(Arrays.asList(45, 55, 38, 60, 70, 65, 58, 75, 72, 68, 80, 90));

        data.addChartDataSet(ganhosDataSet);
        data.addChartDataSet(servicosDataSet);
        data.setLabels(Arrays.asList("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"));

        barModel.setData(data);

        // opções do gráfico de barras
        BarChartOptions options = new BarChartOptions();

        CartesianScales scales = new CartesianScales();
        CartesianLinearAxes yAxis = new CartesianLinearAxes();
        yAxis.setBeginAtZero(true);
        scales.addYAxesData(yAxis);
        options.setScales(scales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Ganhos e Serviços dos Últimos 12 Meses");
        options.setTitle(title);

        barModel.setOptions(options);
    }

    // getters
    public DonutChartModel getDonutModel() {
        return donutModel;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }
}
