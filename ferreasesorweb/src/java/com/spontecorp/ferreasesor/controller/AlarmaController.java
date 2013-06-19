package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.jpa.BotonFacade;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
public class AlarmaController implements Serializable {

    private int count;
    private ThreadOnButton hilo;
    public static final String CHANNEL_BOTON1 = "/channelBoton1";
    private String CHANNEL_BOTON2 = "/channelBoton2";
    private String CHANNEL_BOTON3 = "/channelBoton3";
    private Boton boton;
    @EJB
    private BotonFacade facade;
    private int botonId;
    private String ubicacion;
    private int tiempoBueno;
    private int tiempoRegular;
    private int cerrarLlamada;
    private ExecutorService executor = Executors.newCachedThreadPool();
    // Mapa con los hilos que se están ejecutando
    private Map<String, Runnable> llamados = new HashMap<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boton getBoton() {
        return boton;
    }

    public int getBotonId() {
        return botonId;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public int getTiempoBueno() {
        return tiempoBueno;
    }

    public int getTiempoRegular() {
        return tiempoRegular;
    }

    public int getCerrarLlamada() {
        return cerrarLlamada;
    }

    public void setCerrarLlamada(int cerrarLlamada) {
        this.cerrarLlamada = cerrarLlamada;
    }

    public List<Boton> getBotones() {
        return facade.findAll();
    }

    public void startThread(PushContext pushContext, Boton boton, int tiempoBueno, int tiempoRegular) {
        String buttonSelected = boton.getUbicacion();

        if (!llamados.containsKey(buttonSelected)) {

            hilo = new ThreadOnButton(buttonSelected, pushContext, boton, tiempoBueno, tiempoRegular);
            llamados.put(buttonSelected, hilo);
            hilo.setArrancar();
            //hilo.run();
            executor.execute(hilo);
        }
    }

    public void startThread(PushContext pushContext, Boton boton, int tiempoBueno, int tiempoRegular, int tCierre) {
        String buttonSelected = boton.getUbicacion();

        if (!llamados.containsKey(buttonSelected)) {
            hilo = new ThreadOnButton(buttonSelected, pushContext, boton, tiempoBueno, tiempoRegular, tCierre);
            llamados.put(buttonSelected, hilo);
            hilo.setArrancar();
            executor.execute(hilo);
        }
    }

    public void stopThread(Boton boton) {
        String buttonSelected = boton.getUbicacion();

        if (llamados.containsKey(buttonSelected)) {
            hilo = (ThreadOnButton) llamados.get(buttonSelected);
            hilo.setTerminar();
            llamados.remove(buttonSelected);
        }

    }

    public void enviarBoton(int botonId, int tBueno, int tRegular) {
        boton = facade.find(botonId);
        this.botonId = boton.getId();
        this.ubicacion = boton.getUbicacion();
        this.tiempoBueno = tBueno;
        this.tiempoRegular = tRegular;

        PushContext pushContext = PushContextFactory.getDefault().getPushContext();
        startThread(pushContext, boton, tiempoBueno, tiempoRegular);
    }

    public void enviarBoton(int botonId, int tBueno, int tRegular, int tCierre) {
        boton = facade.find(botonId);
        this.botonId = boton.getId();
        this.ubicacion = boton.getUbicacion();
        this.tiempoBueno = tBueno;
        this.tiempoRegular = tRegular;
        this.cerrarLlamada = tCierre;

        PushContext pushContext = PushContextFactory.getDefault().getPushContext();
        startThread(pushContext, boton, tiempoBueno, tiempoRegular, cerrarLlamada);
    }

    public void detenerBoton(int botonId) {
        boton = facade.find(botonId);
        stopThread(boton);
    }

    public Map<String, Runnable> getLlamados() {
        return llamados;
    }

    public void setLlamados(Map<String, Runnable> llamados) {
        this.llamados = llamados;
    }
}
