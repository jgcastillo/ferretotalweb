package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.Turno;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "llamadasXturnoBean")
@SessionScoped
public class LlamadasXTurnoController extends LlamadaReporteAbstract implements Serializable {
    
    @Override
    public void populateLlamadas(ActionEvent actionEvent) {
        LlamadaFacadeExt facade = new LlamadaFacadeExt();

        getFechasVacias();
        int query = ReporteHelper.LLAMADAS_TURNO;
        result = facade.findLlamadas(query, fechaInicio, fechaFin);
        reporteData = new ArrayList<>();
        for (Object[] array : result) {
            ReporteHelper helper = new ReporteHelper();
            Turno turno = (Turno)array[0];
            helper.setRango(turno.getNombre());
            helper.setDominio(Integer.valueOf(String.valueOf(array[1])));
            reporteData.add(helper);
        }

        showTable = true;
        chartButtonDisable = false;
    }

}
