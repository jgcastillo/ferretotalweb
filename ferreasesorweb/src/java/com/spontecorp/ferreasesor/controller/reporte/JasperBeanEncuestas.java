/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.RespuestaObtenida;
import java.util.List;

/**
 *
 * @author sponte03
 */
public class JasperBeanEncuestas {
    
    private String pregunta;
    private List<RespuestaObtenida> respuesta;

    public JasperBeanEncuestas() {
    }

    public JasperBeanEncuestas(String pregunta, List<RespuestaObtenida> respuesta) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public List<RespuestaObtenida> getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(List<RespuestaObtenida> respuesta) {
        this.respuesta = respuesta;
    }

}
