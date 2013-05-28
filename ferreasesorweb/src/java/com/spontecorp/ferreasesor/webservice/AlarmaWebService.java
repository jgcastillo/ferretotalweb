/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.webservice;

import com.spontecorp.ferreasesor.controller.AlarmaController;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.jws.Oneway;

/**
 *
 * @author jgcastillo
 */
@WebService(serviceName = "AlarmaWebService")
@Stateless
public class AlarmaWebService {

    @EJB
    private AlarmaController managedBean;
    /**
     * Web service operation
     */
    @WebMethod(operationName = "startAlarma")
    @Oneway
    public void startAlarma(@WebParam(name = "botonId") int botonId, @WebParam(name = "tBueno") int tBueno, @WebParam(name = "tRegular") int tRegular) {
         managedBean.enviarBoton(botonId, tBueno, tRegular);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "stopAlarma")
    @Oneway
    public void stopAlarma(@WebParam(name = "botonId") int botonId) {
        managedBean.detenerBoton(botonId);
    }
}
