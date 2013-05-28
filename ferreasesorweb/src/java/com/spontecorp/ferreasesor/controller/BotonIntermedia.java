package com.spontecorp.ferreasesor.controller;

/**
 *
 * @author jgcastillo
 */
public class BotonIntermedia {
    
    private String ubicacion;
    private int botonId;

    public BotonIntermedia(String ubicacion, int botonId) {
        this.ubicacion = ubicacion;
        this.botonId = botonId;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getBotonId() {
        return botonId;
    }

    public void setBotonId(int botonId) {
        this.botonId = botonId;
    }
    
}
