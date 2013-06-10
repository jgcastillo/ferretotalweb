package com.spontecorp.ferreasesor.entity;

import java.io.Serializable;

/**
 *
 * @author Casper
 */
public class EncuestaAux implements Serializable{
    
    private Encuesta encuesta;
    private Pregunta pregunta;
    private RespuestaConf configura;
    private RespuestaObtenida respuesta;
    
    public EncuestaAux(){
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public RespuestaConf getConfigura() {
        return configura;
    }

    public void setConfigura(RespuestaConf configura) {
        this.configura = configura;
    }

    public RespuestaObtenida getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(RespuestaObtenida respuesta) {
        this.respuesta = respuesta;
    }
    
    
}
