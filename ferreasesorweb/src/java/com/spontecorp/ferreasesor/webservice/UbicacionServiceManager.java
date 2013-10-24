package com.spontecorp.ferreasesor.webservice;

import com.spontecorp.ferreasesor.entity.Ubicacion;
import com.spontecorp.ferreasesor.jpa.UbicacionFacade;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Response;

public class UbicacionServiceManager {

    private Response response;

    public UbicacionServiceManager() {
    }

    /**
     * Método para Configurar las Ubicaciones
     * Obtenidas del WS
     * @param ubicacionList
     * @return 
     */
    public Response guardarUbicaciones(List<Ubicacion> ubicacionList) {
        try {
            InitialContext cont = new InitialContext();
            UbicacionFacade ubicacionFacade = (UbicacionFacade) cont.lookup("java:module/UbicacionFacade");
            
            //Verifico si existen Ubicaciones en la Tabla
            List<Ubicacion> ubicacionesExistentes = ubicacionFacade.findAll();
            
            //Elimino las Ubicaciones existentes 
            if(ubicacionesExistentes.size() > 0){
                for(Ubicacion ubicacion : ubicacionesExistentes){
                    ubicacionFacade.remove(ubicacion);
                }
            }
            
            //Creo las Ubicaciones Obtenidas del WS
            for(Ubicacion ubc : ubicacionList){
                ubicacionFacade.create(ubc);
            }
            
            response = Response.status(200).entity("Los Pasillos fueron Configurados con éxito!").build();

        } catch (NamingException ex) {
            Logger.getLogger(UbicacionServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    /**
     * Editar Ubicaciones
     * @param ubc
     * @return 
     */
    public Response editarUbicacion(Ubicacion ubc) {
        try {
            InitialContext cont = new InitialContext();
            UbicacionFacade ubicacionFacade = (UbicacionFacade) cont.lookup("java:module/UbicacionFacade");
            ubicacionFacade.edit(ubc);
            response = Response.status(200).entity("El Pasillo fue editado con éxito!").build();

        } catch (NamingException ex) {
            Logger.getLogger(UbicacionServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
}
