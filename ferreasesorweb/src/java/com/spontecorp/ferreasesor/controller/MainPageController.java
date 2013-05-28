package com.spontecorp.ferreasesor.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Casper
 */
@ManagedBean (name = "mainBean")
@SessionScoped
public class MainPageController {
    
   // private List
            
            /** Query para llenar la tabla
             * SELECT ll.fecha_Open, ll.hora_open, ll.fecha_close,
             * ll.hora_close, b.ubicacion, t.nombre, a.nombre, a.apellido FROM
             * Llamada ll JOIN Distribucion d, Asesor a, Boton b, Turno t WHERE
             * d.id = ll.distribucion_id AND d.asesor_id = a.id AND d.boton_id =
             * b.id AND d.turno_id = t.id;
             */
}
