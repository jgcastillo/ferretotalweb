package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.Llamada;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.event.ActionEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author jgcastillo
 */
public abstract class  LlamadaReporteAbstract {
    protected Date fechaInicio;
    protected Date fechaFin;
    protected List<Llamada> totalLlamadas;
    protected boolean showTable = false;
    protected boolean showChart = false;
    protected boolean chartButtonDisable = true;
    protected List<Object[]> result;
    protected List<ReporteHelper> reporteData;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected CartesianChartModel categoryModel;

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<Llamada> getTotalLlamadas() {
        return totalLlamadas;
    }

    public boolean isShowTable() {
        return showTable;
    }

    public boolean isShowChart() {
        return showChart;
    }

    public boolean isChartButtonDisable() {
        return chartButtonDisable;
    }

    public List<ReporteHelper> getReporteData() {
        return reporteData;
    }

    public CartesianChartModel getCategoryModel() {
        return categoryModel;
    }
    
    public void getFechasVacias(){
        if (fechaInicio == null || fechaFin == null) {
            Calendar cal = new GregorianCalendar();
            fechaFin = new Date();
            cal.setTime(fechaFin);
            int mesActual = cal.get(Calendar.MONTH);
            int yearActual = cal.get(Calendar.YEAR);
            cal.set(yearActual, mesActual, 1);
            fechaInicio = new Date(cal.getTimeInMillis());
        }
    }

    public abstract void populateLlamadas(ActionEvent actionEvent);

    public void muestraGrafico(ActionEvent event) {
        createCategoryModel();
        showChart = true;
    }

    private void createCategoryModel() {
        categoryModel = new CartesianChartModel();
        ChartSeries cant = new ChartSeries("Cantidad");

        for (ReporteHelper data : reporteData) {
            cant.set(data.getRango(), data.getDominio());
        }
        categoryModel.addSeries(cant);
    }
}
