package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.entity.Boton;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.primefaces.push.PushContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sponte03
 */

public class ThreadOnButton implements Runnable {

    private Date sec = new Date();
    private SimpleDateFormat secFormat = new SimpleDateFormat("mm:ss");
    private long counter = 0;
    private int tiempoBueno;
    private int tiempoRegular;
    private Boton boton;
    private volatile PushContext pushContext;
    private String showCounter;
    private volatile boolean terminar;
    private final String nombreHilo;
    private final static Logger logger = LoggerFactory.getLogger(ThreadOnButton.class);
    private String CHANNEL;
    private int tiempoCierre;

    ThreadOnButton(String nombreHilo, PushContext pushContext, Boton boton, int tiempoBueno, int tiempoRegular) {
        this.nombreHilo = nombreHilo;
        this.pushContext = pushContext;
        this.boton = boton;
        this.tiempoBueno = tiempoBueno;
        this.tiempoRegular = tiempoRegular;
    }
    
    ThreadOnButton(String nombreHilo, PushContext pushContext, Boton boton, int tiempoBueno, int tiempoRegular, int tCierre) {
        this.nombreHilo = nombreHilo;
        this.pushContext = pushContext;
        this.boton = boton;
        this.tiempoBueno = tiempoBueno;
        this.tiempoRegular = tiempoRegular;
        this.tiempoCierre = tCierre;
    }
    
    public void setTerminar(){
        this.terminar = true;
    }
    
    public void setArrancar(){
        this.terminar = false;
    }

    public String getCHANNEL() {
        return CHANNEL;
    }

    public void setCHANNEL(String CHANNEL) {
        this.CHANNEL = CHANNEL;
    }

    @Override
    public void run() {
        setCHANNEL("/channel" + boton.getId());
        while (!terminar) {
            try {
                counter++;
                sec.setTime(counter * 1000);
                this.showCounter = secFormat.format(sec);
                if (counter > 0 && counter <= tiempoBueno) {
                    pushContext.push(getCHANNEL(), new BotonIntermedia(boton.getUbicacion(), boton.getId(), this.tiempoBueno, this.tiempoRegular, 1, this.showCounter));
                } else if (counter > tiempoBueno && counter <= tiempoRegular) {
                    pushContext.push(getCHANNEL(), new BotonIntermedia(boton.getUbicacion(), boton.getId(), this.tiempoBueno, this.tiempoRegular, 2, this.showCounter));
                } else if (counter > tiempoRegular && counter < tiempoCierre) {
                    pushContext.push(getCHANNEL(), new BotonIntermedia(boton.getUbicacion(), boton.getId(), this.tiempoBueno, this.tiempoRegular, 3, this.showCounter));
                } else if (counter >= tiempoCierre){
                    pushContext.push(getCHANNEL(), new BotonIntermedia(boton.getUbicacion(), boton.getId(), this.tiempoBueno, this.tiempoRegular, 4, this.showCounter));
                }
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                logger.error("Error de interrupción: " + e.getMessage());
            }
        }
        if (terminar) {
            pushContext.push(getCHANNEL(), new BotonIntermedia(boton.getUbicacion(), boton.getId(), this.tiempoBueno, this.tiempoRegular, 4, this.showCounter));
        }
        
    }
        
}
