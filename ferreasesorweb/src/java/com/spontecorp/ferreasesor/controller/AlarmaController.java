package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.jpa.BotonFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

/**
 *
 * @author jgcastillo
 */
@Stateless
@ManagedBean(name = "alarmaBean")
@ApplicationScoped
public class AlarmaController implements Serializable{
    
    private int count;
    private String CHANNEL = "/counter";
    private String CHANNEL_BOTON = "/channelBoton";
    private Boton boton;
    @EJB
    private BotonFacade facade;
    private int botonId;
    private String ubicacion;
    
    private int tBueno;
    private int tRegular;
    
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boton getBoton() {
        return boton;
    }
    
    public int getBotonId(){
        return botonId;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public int gettBueno() {
        return tBueno;
    }

    public void settBueno(int tBueno) {
        this.tBueno = tBueno;
    }

    public int gettRegular() {
        return tRegular;
    }

    public void settRegular(int tRegular) {
        this.tRegular = tRegular;
    }
    
    public List<Boton> getBotones(){
        return facade.findAll();
    }
    
    public synchronized void increment() {
        count++;
        PushContext pushContext = PushContextFactory.getDefault().getPushContext();
        pushContext.push(CHANNEL, String.valueOf(count));
    }
    
    public synchronized void enviarBoton(int botonId){
        boton = facade.find(botonId);
        this.botonId = boton.getId();
        this.ubicacion = boton.getUbicacion();
        PushContext pushContext = PushContextFactory.getDefault().getPushContext();
        pushContext.push(CHANNEL_BOTON, new BotonIntermedia(ubicacion, this.botonId));
    }
}
