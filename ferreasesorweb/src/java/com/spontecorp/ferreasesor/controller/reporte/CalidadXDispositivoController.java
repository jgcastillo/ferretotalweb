/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.entity.Llamada;
import com.spontecorp.ferreasesor.entity.Tiempo;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "calidadDispositivoBean")
@SessionScoped
public class CalidadXDispositivoController extends LlamadaReporteAbstract implements Serializable {

    private String nombreReporte = "Calidad de Atención por Dispositivo";
    private String nombreRango = "Dispositivos";
    private String nombreDominio = "atenciones";

    @Override
    public void populateLlamadas(ActionEvent actionEvent) {
        LlamadaFacadeExt facade = new LlamadaFacadeExt();
        reporteData = new ArrayList<>();
        long atencion;
        int atencionBuena;
        int atencionRegular;
        Object[][] valores = null;

        //Verifico las fechas
        getFechasVacias();
        //Seteo los Datos del Reporte
        setNombreReporte(nombreReporte);
        setNombreRango(nombreRango);
        setNombreDominio(nombreDominio);

        //Seteo la busqueda (result)
        setResult(facade.findLlamadas(ReporteHelper.CALIDAD_X_DISPOSITIVO, fechaInicio, fechaFin));

        Map<Boton, int[]> atencionesBotMap = new HashMap<>();
        for (Object[] array : result) {
            atencionesBotMap.put((Boton) array[1], new int[3]); // indice 0 buena, 1 regular, 2 mala
        }

        for (Object[] array : result) {
            Llamada llamada = (Llamada) array[0];
            Boton boton = (Boton) array[1];
            Tiempo tiempo = (Tiempo) array[2];

            atencion = (llamada.getHoraClose().getTime() - llamada.getHoraOpen().getTime()) / 1000;
            atencionBuena = tiempo.getAtencionBuena();
            atencionRegular = tiempo.getAtencionRegular();

            int[] temp = atencionesBotMap.get(boton);
            if (atencion <= atencionBuena) {
                temp[0]++;
            }
            if (atencion > atencionBuena && atencion <= atencionRegular) {
                temp[1]++;
            } else if (atencion > atencionRegular) {
                temp[2]++;
            }
            atencionesBotMap.put(boton, temp);
        }

        valores = new Object[atencionesBotMap.size()][4];
        int i = 0;

        for (Map.Entry<Boton, int[]> mapIterator : atencionesBotMap.entrySet()) {
            ReporteHelper helper = new ReporteHelper();
            
            valores[i][0] = mapIterator.getKey().getUbicacion();
            valores[i][1] = mapIterator.getValue()[0];
            valores[i][2] = mapIterator.getValue()[1];
            valores[i][3] = mapIterator.getValue()[2];

//            dataset.addValue(Integer.valueOf(valores[i][1].toString()), "Buenas", valores[i][0].toString());
//            dataset.addValue(Integer.valueOf(valores[i][2].toString()), "Regulares", valores[i][0].toString());
//            dataset.addValue(Integer.valueOf(valores[i][3].toString()), "Malas", valores[i][0].toString());

            //NO ESTOY SEGURA SI ESTA ES LA FORMA IDONEA DE SETEAR EL RANGO
            helper.setRango(mapIterator.getKey().getUbicacion());

            //CON RESPECTO AL DOMINIO DEBE SER UN ARREGLO CON Buenas, Regulares y Malas
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
