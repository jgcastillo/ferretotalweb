package com.spontecorp.ferreasesor.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.LlamadaServer;
import com.spontecorp.ferreasesor.jpa.EncuestaFacade;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.enterprise.context.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.POST;

/**
 * REST Web Service
 *
 * @author sponte07
 */
@Path("llamadaService")
@RequestScoped
public class LlamadaService {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of LlamadaService
     */
    public LlamadaService() {
    }

    @POST
    @Path("enviarencuesta")
    @Consumes("application/json")
    public void setEncuestaGlobal(Encuesta encuesta) {
        
    }

    @GET
    @Path("obtenerjsonencuesta")
    @Produces("application/json")
    public String getJsonEncuesta() {
        String json = null;
        try {
            Encuesta encuesta;
            InitialContext cont = new InitialContext();
            EncuestaFacade encuestaFacade = (EncuestaFacade) cont.lookup("java:module/EncuestaFacade");
            encuesta = encuestaFacade.find(1);
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            json = gson.toJson(encuesta);

        } catch (NamingException ex) {
            Logger.getLogger(LlamadaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }

    /**
     * Retrieves representation of an instance of
     * com.spontecorp.ferreasesor.webservice.LlamadaService
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //Este método solo sirve para probar el funcionamiento del ws
        LlamadaServerManager llsm = new LlamadaServerManager();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        List<LlamadaServer> lls = llsm.llenarListaLlamadasServer(null, null);
        String arrLlamaJson = gson.toJson(lls, new TypeToken<List<LlamadaServer>>() {
        }.getType());
        return arrLlamaJson;
    }

    /**
     * Con éste WS se pueden obtener las llamadas de una tienda por un rango de
     * fechas
     *
     * @param fechaI Fecha inicial del rango
     * @param fechaF Fecha final del rango
     * @param tiendaId id de la tienda
     * @param content "application/json" obligatoriamente
     * @return Arreglo Json con las llamadas
     */
    @GET
    @Path("tiendaporfecha/{fechaInicio}/{fechaFin}/{tiendaId}")
    @Produces("application/json")
    public String getJsonPorFecha(
            @PathParam(value = "fechaInicio") String fechaI,
            @PathParam(value = "fechaFin") String fechaF,
            @PathParam(value = "tiendaId") int tiendaId) {

        LlamadaServerManager llamadaServerManager = new LlamadaServerManager();
        //Se transforman los string de las fechas a fechas
        Date fechaInicio = llamadaServerManager.convertirFecha(fechaI);
        Date fechaFin = llamadaServerManager.convertirFecha(fechaF);
        //Se construye el gson de forma tal que ignore los campos que NO tengan la anotacion @Expose
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        //llenamos la lista con el método de la clase llamadaServerManager
        List<LlamadaServer> listaLlamadaServer = llamadaServerManager.llenarListaLlamadasServer(fechaInicio, fechaFin);
        //Se convierte la lista a tipo json
        String arrLlamaJson = gson.toJson(listaLlamadaServer, new TypeToken<List<LlamadaServer>>() {
        }.getType());
        //se retorna el arreglo de objetos json
        return arrLlamaJson;

    }

    /**
     * PUT method for updating or creating an instance of LlamadaService
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
