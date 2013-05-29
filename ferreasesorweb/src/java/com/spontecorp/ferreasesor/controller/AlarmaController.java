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
    private String CHANNEL = "/counter";
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

    public List<Boton> getBotones() {
        return facade.findAll();
    }

    public synchronized void increment() {
        count++;
        PushContext pushContext = PushContextFactory.getDefault().getPushContext();
        pushContext.push(CHANNEL, String.valueOf(count));
    }

    public void startThread(PushContext pushContext, Boton boton, int tiempoBueno, int tiempoRegular) {
        String buttonSelected = boton.getUbicacion();
        System.out.println("1.- Ubicacion startThread: " + buttonSelected);
        System.out.println("2.- llamados.size(): " + llamados.size());

        if (llamados.containsKey(buttonSelected)) {
            System.out.println("3.- Ya existe un hilo asociado a este Botón");
        }

        if (!llamados.containsKey(buttonSelected)) {
            
            hilo = new ThreadOnButton(boton.getUbicacion(), pushContext, boton, tiempoBueno, tiempoRegular);
            llamados.put(buttonSelected, hilo);
            System.out.println("4.- Creando hilo " + buttonSelected + " asociado a este Botón");
            System.out.println("5.- llamados.size(): " + llamados.size());
            hilo.setArrancar();
            //hilo.run();
            executor.execute(hilo);
        }
    }

    public void stopThread(Boton boton) {
        String buttonSelected = boton.getUbicacion();

        System.out.println("6.- Ubicacion stopThread: " + buttonSelected);
        System.out.println("7.- llamados.size(): " + llamados.size());

        if (!llamados.containsKey(buttonSelected)) {
            System.out.println("8.- No existe un hilo asociado a este Botón");
        }

        if (llamados.containsKey(buttonSelected)) {
            System.out.println("9.- Eliminando el hilo asociado a " + buttonSelected);
            hilo = (ThreadOnButton) llamados.get(buttonSelected);
            hilo.setTerminar();
            executor.shutdown();
            llamados.remove(buttonSelected);
            System.out.println("10.- llamados.size(): " + llamados.size());
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
