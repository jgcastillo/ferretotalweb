/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.jpa;

import com.spontecorp.ferreasesor.entity.Llamada;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jgcastillo
 */
@Stateless
public class LlamadaFacade extends AbstractFacade<Llamada> {
    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LlamadaFacade() {
        super(Llamada.class);
    }
    
    /**
     * Listado de las ultimas 75 Llamadas cerradas
     * accion = 0
     * @return 
     */
    public List<Llamada> findLastCalls(){
        String query = "SELECT ll from Llamada ll where ll.accion = '0' ORDER BY ll.id DESC";
        Query q = getEntityManager().createQuery(query);
        q.setMaxResults(75);
        return q.getResultList();
    }
    
    public List<Llamada> findLlamadasTiempo(Date fechaInicio, Date fechaFin) {
        EntityManager em = getEntityManager();
        List<Llamada> result = null;
        try {
            String query = "SELECT ll FROM Llamada ll "
                + "WHERE ll.accion = '0' AND ll.fechaOpen = :fechaInicio AND ll.fechaClose = :fechaFin";
            Query q = em.createQuery(query);
            q.setParameter("fechaInicio", fechaInicio);
            q.setParameter("fechaFin", fechaFin);
            result = q.getResultList();
        } catch (Exception e) {
            System.out.println("El error es: " + e);
        }
        return result;
    }
    
    
}
