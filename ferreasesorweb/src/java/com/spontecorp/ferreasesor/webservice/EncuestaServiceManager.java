/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.webservice;

import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.Pregunta;
import com.spontecorp.ferreasesor.entity.RespuestaConf;
import com.spontecorp.ferreasesor.entity.Tienda;
import com.spontecorp.ferreasesor.jpa.EncuestaAuxFacade;
import com.spontecorp.ferreasesor.jpa.EncuestaFacade;
import com.spontecorp.ferreasesor.jpa.TiendaFacade;
import com.spontecorp.ferreasesor.utilities.WebServicesUtilities;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Response;

@RequestScoped
public class EncuestaServiceManager implements Serializable {

    private Encuesta encuesta;
    private Tienda tienda;
    private Response rsp;

    /**
     * Método para crear una encuesta global recibida en un "POST request" Se
     * guarda la misma con estatus inactivo en la base de datos
     *
     * @param enc
     * @return rsp
     */
    public Response crearEncuestaGlobal(Encuesta enc) {

        try {
            InitialContext cont = new InitialContext();
            EncuestaFacade encuestaFacade = (EncuestaFacade) cont.lookup("java:module/EncuestaFacade");
            TiendaFacade tiendafacade = (TiendaFacade) cont.lookup("java:module/TiendaFacade");
            EncuestaAuxFacade encuestaFacadeAux = (EncuestaAuxFacade) cont.lookup("java:module/EncuestaAuxFacade");
            encuesta = (Encuesta) encuestaFacadeAux.findEncuestasByIdGlobal(enc.getId());
            if (encuesta == null) {
                encuesta = enc;
                encuesta.setFechaInicio(convertirFecha(encuesta.getFechaInicioString()));
                encuesta.setFechaFin(convertirFecha(encuesta.getFechaFinString()));
                encuesta.setGlobalId(encuesta.getId());
                tienda = tiendafacade.find(WebServicesUtilities.ID_TIENDA);
                encuesta.setTiendaId(tienda);
                encuesta.setGlobal(WebServicesUtilities.ENCUESTA_GLOBAL);
                List<Pregunta> listaPreguntas = encuesta.getPreguntaList();
                for (Pregunta pregunta : listaPreguntas) {
                    pregunta.setEncuestaId(encuesta);
                    List<RespuestaConf> listaRespConf = pregunta.getRespuestaConfList();
                    for (RespuestaConf respuestaConf : listaRespConf) {
                        respuestaConf.setPreguntaId(pregunta);
                    }
                }
                encuestaFacade.create(encuesta);
                rsp = Response.status(200).entity("La encuesta fue creada exitosamente en la tienda " + encuesta.getTiendaId().getNombre()).build();
            } else {
                rsp = Response.status(200).entity("La encuesta ya fue enviada anteriormente a la tienda " + encuesta.getTiendaId().getNombre()).build();
            }
        } catch (NamingException ex) {
            Logger.getLogger(EncuestaServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsp;
    }

    public Date convertirFecha(String fecha) {
        Date date = null;
        try {
            SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
            date = formateador.parse(fecha);

        } catch (ParseException ex) {
            Logger.getLogger(LlamadaServiceManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }
}
