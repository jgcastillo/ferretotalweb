package com.spontecorp.ferreasesor.controller.encuesta;


import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.Tienda;
import com.spontecorp.ferreasesor.jpa.EncuestaFacade;
import com.spontecorp.ferreasesor.jpa.TiendaFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "encuestaBeanController")
@SessionScoped
public class EncuestaBeanController implements Serializable {

    private Encuesta current;
    private DataModel items = null;
    @EJB
    private EncuestaFacade ejbFacade;
    @EJB
    private TiendaFacade tiendaFacade;
    private static final int ACTIVO = 1;
    private static final int INACTIVO = 0;
    
    @ManagedProperty (value = "#{preguntaBean}")
    private PreguntaBeanController preguntaBean;

    public EncuestaBeanController() {
    }

    /**
     * Usados para soportar la annotacion @ManagedProperty
     * @return 
     */
    public PreguntaBeanController getPreguntaBean() {
        return preguntaBean;
    }

    public void setPreguntaBean(PreguntaBeanController preguntaBean) {
        this.preguntaBean = preguntaBean;
    }
    
    public Encuesta getSelected() {
        if (current == null) {
            current = new Encuesta();
        }
        return current;
    }

    private EncuestaFacade getFacade() {
        return ejbFacade;
    }

    private TiendaFacade getTiendaFacade() {
        return tiendaFacade;
    }

    public DataModel getItems() {
        recreateModel();
        System.out.println("llego a getITems");
        if(items == null){
            items = new ListDataModel(getFacade().findAll());
        }
        return items;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareCreate() {
        current = new Encuesta();
        return "createSurvey?faces-redirect=true";
    }
    
    private void getCurrent(){
        current = (Encuesta) getItems().getRowData();
        preguntaBean.setEncuesta(current);
    }

    public String prepareCancel() {
        current = null;
        recreateModel();
        return "encuestaMain?faces-redirect=true";
    }
    
    public String prepareEdit(){
        getCurrent();
        return "editSurvey?faces-redirect=true";
    }
    
    public String prepareAddQuestions(){
        getCurrent();
        return "createQuestions?faces-redirect=true";
    }
    
    public String prepareDelete(){
        getCurrent();
        if(!current.getPreguntaList().isEmpty()){
            JsfUtil.addErrorMessage("La encuesta tiene preguntas cargadas, no puede ser eliminada");
        } else {
            getFacade().remove(current);
        }
        return prepareCancel();
    }

    private void recreateModel() {
        items = null;
    }
    
    public String prepareActivate(){
        getCurrent();
        if(current.getStatus() == ACTIVO){
            current.setStatus(INACTIVO);
        } else {
            current.setStatus(ACTIVO);
        }
        update();
        JsfUtil.addSuccessMessage("Encuesta actualizada");
        return prepareCancel();
    }

    public String create() {
        Tienda tienda = tiendaFacade.find(1);
        try {
            current.setTiendaId(tienda);
            current.setStatus(INACTIVO);
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Encuesta creada con éxito");
            return prepareCancel();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Error a nivel de Base de Datos");
            return null;
        }
    }
    
    public String update(){
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Encuesta eliminada con éxito");
            return prepareCancel();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Error a nivel de Base de Datos");
            return null;
        }
    }
}
