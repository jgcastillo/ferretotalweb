package com.spontecorp.ferreasesor.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.spontecorp.ferreasesor.entity.Encuesta;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

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

    public LlamadaService() {
    }

    @POST
    @Path("enviarencuesta")
    @Consumes("application/json")
    public Response setEncuestaGlobal(Encuesta encuesta) {
        EncuestaServiceManager encuestaServiceManager = new EncuestaServiceManager();
        Response rsp = encuestaServiceManager.crearEncuestaGlobal(encuesta);
        return rsp;
    }

    @GET
    @Path("obtenerresultadosencuesta/{globalId}")
    @Produces("application/json")
    public String getResultadosEncuesta1(@PathParam(value = "globalId") int globalId) {

        EncuestaServiceManager encuestaServiceManager = new EncuestaServiceManager();
        String json = encuestaServiceManager.enviarResultadoEncuesta(globalId);
        return json;
    }

    @GET
    @Path("obtenerresultadosencuesta2/{globalId}")
    @Produces("application/json")
    public String getResultadosEncuesta2(@PathParam(value = "globalId") int globalId) {


        EncuestaServiceManager encuestaServiceManager = new EncuestaServiceManager();
        String json = encuestaServiceManager.enviarResultadoEncuesta2(globalId);
        return json;
    }
    
    @GET
    @Path("obtenerresultadosencuesta3/{globalId}")
    @Produces("application/json")
    public String getResultadosEncuesta3(@PathParam(value = "globalId") int globalId) {


        EncuestaServiceManager encuestaServiceManager = new EncuestaServiceManager();
        String json = encuestaServiceManager.enviarResultadoEncuesta3(globalId);
        return json;
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
    public String getEncuestaPorFecha(
            @PathParam(value = "fechaInicio") String fechaI,
            @PathParam(value = "fechaFin") String fechaF,
            @PathParam(value = "tiendaId") int tiendaId) {

        LlamadaServiceManager llamadaServerManager = new LlamadaServiceManager();
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
}
