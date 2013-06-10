package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.entity.Asesor;
import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.entity.Distribucion;
import com.spontecorp.ferreasesor.entity.Llamada;
import com.spontecorp.ferreasesor.entity.LlamadaAux;
import com.spontecorp.ferreasesor.entity.Turno;
import com.spontecorp.ferreasesor.jpa.AsesorFacade;
import com.spontecorp.ferreasesor.jpa.BotonFacade;
import com.spontecorp.ferreasesor.jpa.LlamadaFacade;
import com.spontecorp.ferreasesor.jpa.TurnoFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.Transient;


/**
 *
 * @author Casper
 */
@ManagedBean(name = "mainBean")
@ViewScoped
public class MainPageController implements Serializable {

    private List<LlamadaAux> llamadaList;
    @Transient
    private LlamadaAux aux;
    @EJB
    private LlamadaFacade llamadaFacade;
    @EJB
    private AsesorFacade asesorFacade;
    @EJB
    private BotonFacade botonFacade;
    @EJB
    private TurnoFacade turnoFacade;
    private List<Llamada> llamadas;
    
    public MainPageController() {
    }

    private LlamadaFacade getLlamadaFacade(){
        return llamadaFacade;
    }
    
    public List<LlamadaAux> getLlamadaList(){
        if(llamadaList == null){
            llamadaList = new ArrayList<>();
            llamadas = getLlamadaFacade().findLastCalls();
            Collections.reverse(llamadas);
            for(Llamada llamada : llamadas){
                Asesor asesor = asesorFacade.find(llamada.getDistribucionId().getAsesorId());
                Boton boton = botonFacade.find(llamada.getDistribucionId().getBotonId());
                Turno turno = turnoFacade.find(llamada.getDistribucionId().getTurnoId());
                aux = new LlamadaAux(llamada, asesor, boton, turno);
                llamadaList.add(aux);
            }
        }
        return llamadaList;
    }

}
