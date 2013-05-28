/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.entity.Boton;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.primefaces.push.PushContext;

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
    private PushContext pushContext;
    private String showCounter;
    private boolean terminar;

    ThreadOnButton(PushContext pushContext, Boton boton, int tiempoBueno, int tiempoRegular) {
        this.pushContext = pushContext;
        this.boton = boton;
        this.tiempoBueno = tiempoBueno;
        this.tiempoRegular = tiempoRegular;
    }

//    public void setTerminar(boolean terminar) {
//        System.out.println("Entro a setTerminar: " + terminar);
//        this.terminar = terminar;
//    }
    
    public void setTerminar(){
        this.terminar = true;
    }
    
    public void setArrancar(){
        this.terminar = false;
    }

    @Override
    public void run() {

        System.out.println("1.- Inicio el hilo: " + terminar);

        while (!terminar) {
            try {
                counter++;
                sec.setTime(counter * 1000);
                this.showCounter = secFormat.format(sec);
                System.out.println("counter: " + counter);
                if (counter > 0 && counter <= tiempoBueno) {
                    pushContext.push(AlarmaController.CHANNEL_BOTON1, new BotonIntermedia(boton.getUbicacion(), boton.getId(), this.tiempoBueno, this.tiempoRegular, 1, this.showCounter));
                } else if (counter > tiempoBueno && counter <= tiempoRegular) {
                    pushContext.push(AlarmaController.CHANNEL_BOTON1, new BotonIntermedia(boton.getUbicacion(), boton.getId(), this.tiempoBueno, this.tiempoRegular, 2, this.showCounter));
                } else if (counter > tiempoRegular) {
                    pushContext.push(AlarmaController.CHANNEL_BOTON1, new BotonIntermedia(boton.getUbicacion(), boton.getId(), this.tiempoBueno, this.tiempoRegular, 3, this.showCounter));
                }
                Thread.sleep(1000);
                if (counter == 20) {
                    terminar = true;
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        if (terminar) {
            System.out.println("2.- Detengo el Hilo: " + counter);
            pushContext.push(AlarmaController.CHANNEL_BOTON1, new BotonIntermedia(boton.getUbicacion(), boton.getId(), this.tiempoBueno, this.tiempoRegular, 4, this.showCounter));
        }
        
    }
    
    //     public void stop() {
//        System.out.println("4.- ENTRO A stop");
//        boolean valor = Thread.interrupted();
//        System.out.println("Valor: "+valor);
//        System.out.println("2.- Detengo el Hilo: " + counter);
//        int status = 4;
//        pushContext.push(AlarmaController.CHANNEL_BOTON1, String.valueOf(status));
//    }
}
