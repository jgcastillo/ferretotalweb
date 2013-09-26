/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.Pregunta;
import com.spontecorp.ferreasesor.entity.RespuestaConf;
import com.spontecorp.ferreasesor.entity.RespuestaObtenida;
import com.spontecorp.ferreasesor.entity.Tienda;
import com.spontecorp.ferreasesor.jpa.EncuestaAuxFacade;
import com.spontecorp.ferreasesor.jpa.EncuestaFacade;
import com.spontecorp.ferreasesor.jpa.TiendaFacade;
import com.spontecorp.ferreasesor.utilities.WebServicesUtilities;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Response;

public class EncuestaServiceManager implements Serializable {

    private Response rsp;

    public Response eliminarEncuestaGlobal(int idGlobal) {
        try {
            InitialContext cont = new InitialContext();
            EncuestaAuxFacade encuestaFacadeAux = (EncuestaAuxFacade) cont.lookup("java:module/EncuestaAuxFacade");
            EncuestaFacade encuestaFacade = (EncuestaFacade) cont.lookup("java:module/EncuestaFacade");
            Encuesta encuesta = (Encuesta) encuestaFacadeAux.findEncuestasByIdGlobal(idGlobal);
            encuestaFacade.remove(encuesta);
            rsp = Response.status(200).entity("La encuesta fue eliminado " + encuesta.getTiendaId().getNombre()).build();

        } catch (NamingException ex) {
            Logger.getLogger(EncuestaServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsp;
    }

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
            Encuesta encuesta = (Encuesta) encuestaFacadeAux.findEncuestasByIdGlobal(enc.getId());
            if (encuesta == null) {
                encuesta = enc;
                encuesta.setFechaInicio(convertirFecha(encuesta.getFechaInicioString()));
                encuesta.setFechaFin(convertirFecha(encuesta.getFechaFinString()));
                encuesta.setGlobalId(encuesta.getId());
                Tienda tienda = tiendafacade.find(WebServicesUtilities.ID_TIENDA);
                encuesta.setTiendaId(tienda);
                encuesta.getPreguntaList();
                for (Pregunta pregunta : encuesta.getPreguntaList()) {
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
            Logger.getLogger(EncuestaServiceManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public String enviarResultadoEncuesta(int idGlobal) {
        String json = "";
        try {
            Encuesta encuesta = null;
            InitialContext cont = new InitialContext();
            EncuestaAuxFacade encuestaFacadeAux = (EncuestaAuxFacade) cont.lookup("java:module/EncuestaAuxFacade");
            if (encuesta == null) {
                encuesta = (Encuesta) encuestaFacadeAux.findEncuestasByIdGlobal(idGlobal);
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
                json = gson.toJson(encuesta);
            }
        } catch (NamingException ex) {
            Logger.getLogger(EncuestaServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }

    public String enviarResultadoEncuesta2(int idGlobal) {
        String jsonResultado = null;
        try {
            ResultadoEncuesta restultadoEncuesta = new ResultadoEncuesta();
            InitialContext cont = new InitialContext();
            EncuestaAuxFacade encuestaFacadeAux = (EncuestaAuxFacade) cont.lookup("java:module/EncuestaAuxFacade");
            Encuesta encuesta = (Encuesta) encuestaFacadeAux.findEncuestasByIdGlobal(idGlobal);
            List<Pregunta> listaPreguntas = encuesta.getPreguntaList();

            restultadoEncuesta.setIdGlobal(idGlobal);
            restultadoEncuesta.setNombreEncuesta(encuesta.getNombre());

            for (Pregunta pregunta : listaPreguntas) {
                if (pregunta.getTipo() == 1) {
                    Tipo1 t1 = new Tipo1();
                    List<String> respuestasT1 = new ArrayList<>();
                    t1.setPregunta(pregunta.getPregunta());
                    for (RespuestaObtenida respObt : pregunta.getRespuestaObtenidaList()) {
                        respuestasT1.add(respObt.getRespuesta());
                    }
                    t1.setRespuestas(respuestasT1);
                    restultadoEncuesta.getPreguntasT1().add(t1);

                } else if (pregunta.getTipo() == 2) {
                    Tipo2 t2 = new Tipo2();
                    List<Integer> respuestasT2 = new ArrayList<>();
                    t2.setPregunta(pregunta.getPregunta());
                    for (RespuestaObtenida respObt : pregunta.getRespuestaObtenidaList()) {
                        respuestasT2.add(Integer.parseInt(respObt.getRespuesta()));
                    }
                    t2.setRespuestas(respuestasT2);
                    restultadoEncuesta.getPreguntasT2().add(t2);

                } else if (pregunta.getTipo() == 3) {
                    Tipo3 t3 = new Tipo3();
                    t3.setPregunta(pregunta.getPregunta());

                    for (RespuestaConf respConf : pregunta.getRespuestaConfList()) {
                        Opcion opc = new Opcion();
                        opc.setOpcion(respConf.getOpcion());
                        opc.setCantidad(respConf.getRespuestaObtenidaList().size());
                        t3.getOpciones().add(opc);
                    }
                    restultadoEncuesta.getPreguntasT3().add(t3);
                } else if (pregunta.getTipo() == 4) {
                    Tipo4 t4 = new Tipo4();
                    List<Integer> respuestasT4 = new ArrayList<>();
                    t4.setPregunta(pregunta.getPregunta());
                    for (RespuestaObtenida respObt : pregunta.getRespuestaObtenidaList()) {
                        respuestasT4.add(Integer.parseInt(respObt.getRespuesta()));
                    }
                    t4.setRespuestas(respuestasT4);
                    restultadoEncuesta.getPreguntasT4().add(t4);
                }
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            jsonResultado = gson.toJson(restultadoEncuesta);
            System.out.println(jsonResultado);

        } catch (NamingException ex) {
            Logger.getLogger(EncuestaServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonResultado;
    }
}
