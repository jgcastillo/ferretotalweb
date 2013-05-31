package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.controller.chart.BarChart;
import com.spontecorp.ferreasesor.entity.Turno;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author jgcastillo
 */
@ManagedBean (name = "promedioXturnoBean")
@SessionScoped
public class PromediosXTurnoController extends LlamadaReporteAbstract implements Serializable{

    private String nombreReporte = "Promedio de Llamadas";
    private String nombreRango = "Turno";
    private String nombreDominio = "Promedios";
    
    @Override
    public void populateLlamadas(ActionEvent actionEvent) {
        LlamadaFacadeExt facade = new LlamadaFacadeExt();
        reporteData = new ArrayList<>();
        
        Locale locale = new Locale("en", "US");
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyLocalizedPattern("##0.0");
        ReporteHelper helper;
        
        //Seteo promedio
        setPromedios(true);
        //Verifico las fechas
        getFechasVacias();
        //Seteo los Datos del Reporte
        setNombreReporte(nombreReporte);
        setNombreRango(nombreRango);
        setNombreDominio(nombreDominio);
               
        Long totalCalls = facade.getLlamadaCount(fechaInicio, fechaFin);
        Long diasEntreFechas = facade.getDiasEntreFechasCount(fechaInicio, fechaFin);
        double promedioDiario = ((double) totalCalls) / diasEntreFechas;

        //Seteo la busqueda
        setResult(facade.findLlamadas(ReporteHelper.LLAMADAS_TURNO, fechaInicio, fechaFin));

        helper = new ReporteHelper();
        helper.setRango("Diario");
        helper.setDominio(Double.valueOf(df.format(promedioDiario)));
        reporteData.add(helper);
        
        for(Object[] array : result){
            helper = new ReporteHelper();
            Turno turno = (Turno) array[0];
            helper.setRango(turno.getNombre());
            double llamadas = Double.valueOf(array[1].toString());
            double prom = (llamadas / diasEntreFechas);
            helper.setDominio(Double.valueOf(df.format(prom)));
            reporteData.add(helper);
        }
        
        showTable = true;
        chartButtonDisable = false;
    }

    @Override
    public StreamedContent getChart() {
        LlamadaFacadeExt facade = new LlamadaFacadeExt();
        BarChart barChart = new BarChart(nombreReporte, nombreRango, nombreDominio);
        barChart.setResult(getResult());
        barChart.createDataset();
        return barChart.getBarChart();
    }
    
}
