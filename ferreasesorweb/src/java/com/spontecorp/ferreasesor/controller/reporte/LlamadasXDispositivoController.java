package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.Boton;
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
@ManagedBean(name = "llamadasXdispBean")
@SessionScoped
public class LlamadasXDispositivoController extends LlamadaReporteAbstract implements Serializable {

    @Override
    public void populateLlamadas(ActionEvent actionEvent) {
        LlamadaFacadeExt facade = new LlamadaFacadeExt();

        getFechasVacias();
        int query = ReporteHelper.LLAMADAS_DISPOSITIVO;
        result = facade.findLlamadas(query, fechaInicio, fechaFin);
        reporteData = new ArrayList<>();
        for (Object[] array : result) {
            ReporteHelper helper = new ReporteHelper();
            helper.setRango(((Boton) array[0]).getUbicacion());
            helper.setDominio(Integer.valueOf(String.valueOf(array[1])));
            reporteData.add(helper);
        }

        showTable = true;
        chartButtonDisable = false;
    }
}
