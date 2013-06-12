package com.spontecorp.ferreasesor.controller.encuesta;

import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.Pregunta;
import com.spontecorp.ferreasesor.entity.RespuestaConf;
import com.spontecorp.ferreasesor.jpa.EncuestaFacade;
import com.spontecorp.ferreasesor.jpa.PreguntaFacade;
import com.spontecorp.ferreasesor.jpa.RespuestaConfFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Casper
 */
@ManagedBean(name = "preguntaBean")
@SessionScoped
public class PreguntaBeanController implements Serializable {

    private Pregunta current;
    private RespuestaConf respuesta;
    private String preguntaTexto;
    private String promptPreguntaTextual;
    private String respuestaTexto;
    //private String promptPreguntaNumeric;
    private String respuestaNumeric;
    private String preguntaSeleccion;
    private String preguntaSeleccionItem;
    private List<String> preguntaSeleccionValores;
    private List<Pregunta> preguntaList = null;
    private List<RespuestaConf> opcionsList = null;
    private Integer respuestaSeleccion;
    private Integer respuestaRating;
    private int tipoPregunta;
    private Encuesta encuesta;
    @EJB
    private PreguntaFacade preguntaFacade;
    @EJB
    private RespuestaConfFacade respuestaFacade;
    @EJB
    private EncuestaFacade encuestaFacade;
    private DataModel<Pregunta> preguntaItems;
    private Map<Pregunta, List<RespuestaConf>> respuestas;
    private static final int ENCUESTA_ACTIVA = 1;
    private static final Logger logger = LoggerFactory.getLogger(PreguntaBeanController.class);

    public PreguntaBeanController() {
    }

    private PreguntaFacade getPreguntaFacade() {
        return preguntaFacade;
    }

    private RespuestaConfFacade getRespuestaFacade() {
        return respuestaFacade;
    }

    public EncuestaFacade getEncuestaFacade() {
        return encuestaFacade;
    }

    public String getRespuestaTexto() {
        return respuestaTexto;
    }

    public void setRespuestaTexto(String respuestaTexto) {
        this.respuestaTexto = respuestaTexto;
    }

    public String getPreguntaTexto() {
        return preguntaTexto;
    }

    public void setPreguntaTexto(String preguntaTexto) {
        this.preguntaTexto = preguntaTexto;
    }

    public int getTipoPregunta() {
        return tipoPregunta;
    }

    public void setTipoPregunta(int tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    public String getPromptPreguntaTextual() {
        return promptPreguntaTextual;
    }

    public void setPromptPreguntaTextual(String promptPreguntaTextual) {
        this.promptPreguntaTextual = promptPreguntaTextual;
    }

    public String getRespuestaNumeric() {
        return respuestaNumeric;
    }

    public void setRespuestaNumeric(String respuestaNumeric) {
        this.respuestaNumeric = respuestaNumeric;
    }

    public String getPreguntaSeleccionItem() {
        return preguntaSeleccionItem;
    }

    public void setPreguntaSeleccionItem(String preguntaSeleccionItem) {
        this.preguntaSeleccionItem = preguntaSeleccionItem;
    }

    public List<String> getPreguntaSeleccionValores() {
        return preguntaSeleccionValores;
    }

    public void setPreguntaSeleccionValores(List<String> preguntaSeleccionValores) {
        this.preguntaSeleccionValores = preguntaSeleccionValores;
    }

    public String getPreguntaSeleccion() {
        return preguntaSeleccion;
    }

    public void setPreguntaSeleccion(String preguntaSeleccion) {
        this.preguntaSeleccion = preguntaSeleccion;
    }

    public Integer getRespuestaSeleccion() {
        return respuestaSeleccion;
    }

    public void setRespuestaSeleccion(Integer respuestaSeleccion) {
        this.respuestaSeleccion = respuestaSeleccion;
    }

    public Integer getRespuestaRating() {
        return respuestaRating;
    }

    public void setRespuestaRating(Integer respuestaRating) {
        this.respuestaRating = respuestaRating;
    }

    public Encuesta getEncuestaActiva() {
        encuesta = getEncuestaFacade().findAll(ENCUESTA_ACTIVA);
        return encuesta;
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
    }

    public DataModel getPreguntaItems() {
        if (preguntaItems == null) {
            preguntaItems = new ListDataModel(getPreguntaFacade().findAll(encuesta));
        }
        return preguntaItems;
    }

    public List<Pregunta> getPreguntaList() {
        encuesta = getEncuestaActiva();
        preguntaList = null;
        opcionsList = null;
        
        if (encuesta != null && preguntaList == null && opcionsList == null) {
            preguntaSeleccionValores = new ArrayList();
            preguntaList = getPreguntaFacade().findAll(encuesta);
            for (Pregunta pregunta : preguntaList){
                opcionsList = getRespuestaFacade().findRespuestaConf(pregunta);
                pregunta.setRespuestaConfList(opcionsList);
            }
        }

        return preguntaList;

    }

    public String guardaRespuesta() {
        return null;
    }

    public String configuraPregunta() {
        //promptPreguntaTextual = "";
        //setPromptPreguntaTextual(promptPreguntaTextual);
        //System.out.println("Pregunta en configuraPregunta: "+preguntaTexto);
        preguntaSeleccionValores = new ArrayList();
        String next;
        
        if(tipoPregunta == 1 || tipoPregunta == 2){
            next = "showQuestion?faces-redirect=true";
        }else{
            next = "configQuestion?faces-redirect=true";
        }
        
        return next;
    }

    public String retornaCreate() {
        promptPreguntaTextual = null;
        preguntaTexto = null;
        return "createQuestions?faces-redirect=true";
    }

    public String muestraPregunta() {
        //setPromptPreguntaTextual(promptPreguntaTextual);
        return "showQuestion?faces-redirect=true";
    }

    public String guardaPregunta() {
        preguntaFacade = getPreguntaFacade();
        respuestaFacade = getRespuestaFacade();
        current = new Pregunta();
        current.setEncuestaId(encuesta);
        current.setPregunta(preguntaTexto);
        current.setTipo(tipoPregunta);
        preguntaFacade.create(current);

        switch (tipoPregunta) {
            case 1:
            case 2:
            case 4: {
                respuesta = new RespuestaConf();
                respuesta.setPreguntaId(current);
                respuesta.setPrompt(promptPreguntaTextual);
                respuestaFacade.create(respuesta);
                break;
            }
            case 3: {
                for (String opcion : preguntaSeleccionValores) {
                    respuesta = new RespuestaConf();
                    respuesta.setPreguntaId(current);
                    respuesta.setOpcion(opcion);
                    respuesta.setPrompt(promptPreguntaTextual);
                    respuestaFacade.create(respuesta);
                }
                break;
            }
        }
        recreateModel();
        JsfUtil.addSuccessMessage("Pregunta agregada con éxito");
        return "createQuestions?faces-redirect=true";
    }

    public void addItemToSeleccion(ActionEvent event) {
        if (preguntaSeleccionValores.add(preguntaSeleccionItem)) {
            JsfUtil.addSuccessMessage("Opción agregada");
        } else {
            JsfUtil.addErrorMessage("Opción no se pudo agregar");
        }
    }

    public void removeItemFromSeleccion() {
        if (preguntaSeleccionValores.remove(preguntaSeleccionItem)) {
            JsfUtil.addSuccessMessage("Opción eliminar");
        } else {
            JsfUtil.addErrorMessage("Opción no se pudo eliminar");
        }
    }

    public Map<String, Integer> getOpcionesSeleccion() {
        Map<String, Integer> opciones = new LinkedHashMap();
        //List<SelectItem> opciones = new ArrayList<SelectItem>();
        int i = 0;
        for (String opcion : preguntaSeleccionValores) {
            opciones.put(opcion, i);
            i++;
        }
        return opciones;
    }

    public String prepareCancel() {
        setEncuesta(null);
        current = null;
        recreateModel();
        return "encuestaMain?faces-redirect=true";
    }

    public String prepareCreate() {
        recreateModel();
        return "createQuestions?faces-redirect=true";
    }

    public String prepareDelete() {
        preguntaSeleccionValores = new ArrayList();
        current = (Pregunta) getPreguntaItems().getRowData();
        tipoPregunta = current.getTipo();
        preguntaTexto = current.getPregunta();
        for (RespuestaConf opcionConf : current.getRespuestaConfList()) {
            promptPreguntaTextual = opcionConf.getPrompt();
            preguntaSeleccionValores.add(opcionConf.getOpcion());
        }
        return "deleteQuestions?faces-redirect=true";
    }

    public String deletePregunta() {
        respuestaFacade = getRespuestaFacade();
        preguntaFacade = getPreguntaFacade();
        for (RespuestaConf opcionConf : current.getRespuestaConfList()) {
            respuestaFacade.remove(opcionConf);
        }
        current.setRespuestaConfList(null);
        preguntaFacade.remove(current);
        tipoPregunta = 0;
        preguntaTexto = null;
        promptPreguntaTextual = null;
        preguntaSeleccionValores.clear();
        JsfUtil.addSuccessMessage("Pregunta eliminada de la encuesta");
        return prepareCreate();
    }

    private void recreateModel() {
        preguntaTexto = null;
        tipoPregunta = 0;
        preguntaItems = null;
        promptPreguntaTextual = null;
    }
}
