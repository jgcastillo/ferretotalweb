/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.jpa;

import com.spontecorp.ferreasesor.entity.Motivo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author sponte03
 */
@Stateless
public class MotivoFacade extends AbstractFacade<Motivo> {
    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MotivoFacade() {
        super(Motivo.class);
    }
    
}
