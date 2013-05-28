package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.Asesor;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author jgcastillo
 */
@ManagedBean (name = "llamadasXasesorBean")
@SessionScoped
public class LlamadasXAsesorController extends LlamadaReporteAbstract implements Serializable{
    
    @Override
    public void populateLlamadas(ActionEvent actionEvent) {
        LlamadaFacadeExt facade = new LlamadaFacadeExt();

        getFechasVacias();
        int query = ReporteHelper.LLAMADAS_ASESOR;
        result = facade.findLlamadas(query, fechaInicio, fechaFin);
        reporteData = new ArrayList<>();
        for (Object[] array : result) {
            ReporteHelper helper = new ReporteHelper();
            Asesor asesor = (Asesor)array[0];
            helper.setRango(asesor.getNombre() + " " + asesor.getApellido());
            helper.setDominio(Integer.valueOf(String.valueOf(array[1])));
            reporteData.add(helper);
        }

        showTable = true;
        chartButtonDisable = false;
    }

}
