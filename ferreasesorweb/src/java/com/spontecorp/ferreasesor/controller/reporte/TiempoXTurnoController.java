/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.Llamada;
import com.spontecorp.ferreasesor.entity.Turno;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "tiempoTurnoBean")
@SessionScoped
public class TiempoXTurnoController extends LlamadaReporteAbstract implements Serializable {

    private String nombreReporte = "Tiempos de Atención por Turno";
    private String nombreRango = "Turnos";
    private String nombreDominio = "segundos";

    @Override
    public void populateLlamadas(ActionEvent actionEvent) {
        LlamadaFacadeExt facade = new LlamadaFacadeExt();
        reporteData = new ArrayList<>();
        List<Llamada> llamadas = new ArrayList<>();
        Map<Turno, List<Llamada>> mapTurno = new HashMap<>();
        DecimalFormat df = new DecimalFormat("###0.0");
        Turno tmpTurno = new Turno();
        Object[][] valores = null;
        long total = 0L;
        double promedio;
        double min;
        double max;
        long time = 0L;

        //Verifico las fechas
        getFechasVacias();
        //Seteo los Datos del Reporte
        setNombreReporte(nombreReporte);
        setNombreRango(nombreRango);
        setNombreDominio(nombreDominio);

        //Seteo la busqueda (result)
        setResult(facade.findLlamadas(ReporteHelper.TIEMPOS_X_TURNO, fechaInicio, fechaFin));

        for (Object[] array : result) {
            Llamada llamada = (Llamada) array[0];
            Turno turno = (Turno) array[1];

            if (!tmpTurno.equals(turno)) {
                tmpTurno = turno;
                llamadas = new ArrayList<>();
                llamadas.add(llamada);
            } else {
                llamadas.add(llamada);
            }
            mapTurno.put(turno, llamadas);
        }

        valores = new Object[mapTurno.size()][4];
        int i = 0;

        for (Map.Entry<Turno, List<Llamada>> map : mapTurno.entrySet()) {
            ReporteHelper helper = new ReporteHelper();
            total = 0L;
            promedio = 0.0;
            max = 0.0;
            min = 99999999999.9;
            for (Llamada call : map.getValue()) {
                time = (call.getHoraClose().getTime() - call.getHoraOpen().getTime()) / 1000;
                total += time;
                if (time > max) {
                    max = (double) time;
                }

                if (time < min) {
                    min = (double) time;
                }
            }

            promedio = total / (double) map.getValue().size();
            valores[i][0] = map.getKey().getNombre();
            valores[i][1] = df.format(min);
            valores[i][2] = df.format(promedio);
            valores[i][3] = df.format(max);

//            dataset.addValue(min, "min", valores[i][0].toString());
//            dataset.addValue(promedio, "prom", valores[i][0].toString());
//            dataset.addValue(max, "max", valores[i][0].toString());
            
            //NO ESTOY SEGURA SI ESTA ES LA FORMA IDONEA DE SETEAR EL RANGO
            helper.setRango(map.getKey().getNombre());
            
            //CON RESPECTO AL DOMINIO DEBE SER UN ARREGLO CON MIN, PROMEDIO Y MAX
           // helper.setDominio(Integer.valueOf(String.valueOf(  )));
            reporteData.add(helper);
            
            i++;

        }
        
        showTable = true;
        chartButtonDisable = false;

    }

    @Override
    public StreamedContent getChart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
