/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "respuesta_obtenida")
@NamedQueries({
    @NamedQuery(name = "RespuestaObtenida.findAll", query = "SELECT r FROM RespuestaObtenida r"),
    @NamedQuery(name = "RespuestaObtenida.findById", query = "SELECT r FROM RespuestaObtenida r WHERE r.id = :id"),
    @NamedQuery(name = "RespuestaObtenida.findByRespuesta", query = "SELECT r FROM RespuestaObtenida r WHERE r.respuesta = :respuesta")})
public class RespuestaObtenida implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private Integer id;
    @Size(max = 150)
    @Column(name = "respuesta")
    @Expose
    private String respuesta;
    @JoinColumn(name = "respuesta_conf_id", referencedColumnName = "id")
    @ManyToOne(optional = false)   
    private RespuestaConf respuestaConfId;
    @JoinColumn(name = "pregunta_id", referencedColumnName = "id")
    @ManyToOne(optional = false)   
    private Pregunta preguntaId;
    @JoinColumn(name = "encuesta_id", referencedColumnName = "id")
    @ManyToOne(optional = false)    
    private Encuesta encuestaId;
    
    public RespuestaObtenida() {
    }

    public RespuestaObtenida(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public RespuestaConf getRespuestaConfId() {
        return respuestaConfId;
    }

    public void setRespuestaConfId(RespuestaConf respuestaConfId) {
        this.respuestaConfId = respuestaConfId;
    }

    public Pregunta getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(Pregunta preguntaId) {
        this.preguntaId = preguntaId;
    }

    public Encuesta getEncuestaId() {
        return encuestaId;
    }

    public void setEncuestaId(Encuesta encuestaId) {
        this.encuestaId = encuestaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RespuestaObtenida)) {
            return false;
        }
        RespuestaObtenida other = (RespuestaObtenida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.ferreasesor.entity.RespuestaObtenida[ id=" + id + " ]";
    }
    
}
