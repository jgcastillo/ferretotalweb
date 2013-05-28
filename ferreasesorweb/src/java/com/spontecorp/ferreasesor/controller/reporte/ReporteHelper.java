package com.spontecorp.ferreasesor.controller.reporte;

/**
 *
 * @author jgcastillo
 */
public class ReporteHelper {
    
    private Object rango;
    private Number dominio;
    
    public static final int LLAMADAS_DISPOSITIVO = 1;
    public static final int LLAMADAS_ASESOR = 2;
    public static final int LLAMADAS_TURNO = 3;

    public Object getRango() {
        return rango;
    }

    public void setRango(Object rango) {
        this.rango = rango;
    }

    public Number getDominio() {
        return dominio;
    }

    public void setDominio(Number dominio) {
        this.dominio = dominio;
    }
    
}
