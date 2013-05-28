package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.controller.chart.BarChart;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author jgcastillo
 */
@ManagedBean (name = "totalLlamadasBean")
@SessionScoped
public class TotalLLamadasController extends LlamadaReporteAbstract implements Serializable{
    
    
    @Override
    public void populateLlamadas(ActionEvent actionEvent){
        reporteData = new ArrayList<>();
        LlamadaFacadeExt facade = new LlamadaFacadeExt();
        getFechasVacias();
        result = facade.findLlamadas(fechaInicio, fechaFin);
        for(Object[] array : result){
            ReporteHelper helper = new ReporteHelper();
            helper.setRango(sdf.format((Date)array[0]));
            helper.setDominio(Integer.valueOf(String.valueOf(array[1])));
            reporteData.add(helper);
        }
        
        showTable = true;
        chartButtonDisable = false;
    }
    
    public StreamedContent getChart(){
        LlamadaFacadeExt facade = new LlamadaFacadeExt();
        result = facade.findLlamadas(fechaInicio, fechaFin);
        
        BarChart barChart = new BarChart("Llamadas Totales", "Días", "Llamdas");
        barChart.setResult(result);
        barChart.createDataset();
        return barChart.getBarChart();
    }

}
