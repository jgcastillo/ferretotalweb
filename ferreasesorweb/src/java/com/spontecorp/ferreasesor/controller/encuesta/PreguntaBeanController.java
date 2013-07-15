package com.spontecorp.ferreasesor.controller.encuesta;

import com.spontecorp.ferreasesor.controller.reporte.JasperBeanEncuestas;
import com.spontecorp.ferreasesor.controller.reporte.JasperManagement;
import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.Pregunta;
import com.spontecorp.ferreasesor.entity.RespuestaConf;
import com.spontecorp.ferreasesor.entity.RespuestaObtenida;
import com.spontecorp.ferreasesor.jpa.EncuestaFacade;
import com.spontecorp.ferreasesor.jpa.PreguntaFacade;
import com.spontecorp.ferreasesor.jpa.RespuestaConfFacade;
import com.spontecorp.ferreasesor.jpa.RespuestaObtenidaFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import net.sf.jasperreports.engine.JRException;
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
    private String respuestaNumeric;
    private String preguntaSeleccion;
    private String preguntaSeleccionItem;
    private List<String> preguntaSeleccionValores;
    private List<Pregunta> preguntaList = null;
    //private List<String> respuestaList = new ArrayList();
    //  private String[] respuestaList;
    private String respuestaList[] = new String[25];
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
    @EJB
    private RespuestaObtenidaFacade respObtenidaFacade;
    private DataModel<Pregunta> preguntaItems;
    private Map<Pregunta, List<RespuestaConf>> respuestas;
    private static final int ENCUESTA_ACTIVA = 1;
    //Tipos de Preguntas
    private static final int TEXTUAL = 1;
    private static final int NUMERICO = 2;
    private static final int SELECCION = 3;
    private static final int CALIFICACION = 4;
    private boolean message1 = false;
    private boolean message2 = false;
    private boolean message3 = false;
    private String nombreReporte;

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

    public RespuestaObtenidaFacade getRespObtenidaFacade() {
        return respObtenidaFacade;
    }

    public String getRespuestaTexto() {
        return respuestaTexto;
    }

    public String[] getRespuestaList() {
        return respuestaList;
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

    public boolean isMessage1() {
        return message1;
    }

    public boolean isMessage2() {
        return message2;
    }

    public boolean isMessage3() {
        return message3;
    }

    /**
     * Listado de Preguntas para llenar la Tabla
     *
     * @return
     */
    public DataModel getPreguntaItems() {
        //recreateModel();
        preguntaItems = null;
        if (preguntaItems == null) {
            preguntaItems = new ListDataModel(getPreguntaFacade().findAll(encuesta));
            for (Pregunta pregunta : preguntaItems) {
                List<RespuestaObtenida> respList = null;
                if (respList == null) {
                    respList = getRespObtenidaFacade().findRespuestaObtenidaList(pregunta);
                }
                pregunta.setRespuestaObtenidaList(respList);
            }
        }
        return preguntaItems;
    }

    /**
     * Listado de Preguntas
     *
     * @return
     */
    public List<Pregunta> getPreguntaList() {
        encuesta = getEncuestaActiva();
        preguntaList = null;
        opcionsList = null;

        if (encuesta != null && preguntaList == null && opcionsList == null) {
            preguntaSeleccionValores = new ArrayList();
            preguntaList = getPreguntaFacade().findAll(encuesta);
            for (Pregunta pregunta : preguntaList) {
                opcionsList = getRespuestaFacade().findRespuestaConf(pregunta);
                pregunta.setRespuestaConfList(opcionsList);
            }
        }

        return preguntaList;

    }

    /**
     * Metodo para Guardar Respuesta por Preguntas
     *
     * @param actionEvent
     * @return
     */
    public String guardaRespuesta() {

        for (int i = 0; i < preguntaList.size(); i++) {
            respuestaTexto = respuestaList[i].toString();

            RespuestaObtenida respObtenida = new RespuestaObtenida();

            if (!respuestaTexto.equals("")) {
                if (preguntaList.get(i).getTipo() == SELECCION) {
                    respuesta = getRespuestaFacade().find(Integer.valueOf(respuestaTexto));
                    respObtenida.setRespuesta(null);
                } else {
                    respuesta = preguntaList.get(i).getRespuestaConfList().get(0);
                    respObtenida.setRespuesta(respuestaTexto);
                }

                respObtenida.setRespuestaConfId(respuesta);
                respObtenida.setEncuestaId(encuesta);
                respObtenida.setPreguntaId(preguntaList.get(i));
                respObtenidaFacade.create(respObtenida);
            }
        }
        for (int i = 0; i < preguntaList.size(); i++) {
            respuestaList[i] = "";
        }

        setRespuestaTexto(null);
        return showMessage();
        //JsfUtil.addSuccessMessage("La Encuesta fue enviada con éxito!");
        //return "message?faces-redirect=true";
    }

    public String showMessage() {
        //System.out.println("Entro a ShowMessage()");
        return "message?faces-redirect=true";
    }

    /**
     * Configurar Preguntas
     *
     * @return
     */
    public String configuraPregunta() {
        //promptPreguntaTextual = "";
        //setPromptPreguntaTextual(promptPreguntaTextual);
        //System.out.println("Pregunta en configuraPregunta: "+preguntaTexto);
        preguntaSeleccionValores = new ArrayList();
        String next;

        if (tipoPregunta == 1 || tipoPregunta == 2) {
            next = "showQuestion?faces-redirect=true";
        } else {
            next = "configQuestion?faces-redirect=true";
        }
        resetMessage();
        return next;
    }

    /**
     *
     * @return
     */
    public String retornaCreate() {
        resetMessage();
        recreateModel();
        return "createQuestions?faces-redirect=true";
    }

    /**
     *
     * @return
     */
    public String muestraPregunta() {
        return "showQuestion?faces-redirect=true";
    }

    /**
     * Guardar Preguntas
     *
     * @return
     */
    public String guardaPregunta() {
        preguntaFacade = getPreguntaFacade();
        respuestaFacade = getRespuestaFacade();
        current = new Pregunta();
        current.setEncuestaId(encuesta);
        current.setPregunta(preguntaTexto);
        current.setTipo(tipoPregunta);

        if (encuesta != null) {
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
            JsfUtil.addSuccessMessage("Pregunta agregada con éxito");
        }
        recreateModel();
        resetMessage();
        message3 = true;
        return "createQuestions?faces-redirect=true";
    }

    /**
     * Añadir Opciones a las Preguntas del Tipo Seleccion
     *
     * @param event
     */
    public void addItemToSeleccion(ActionEvent event) {
        if (preguntaSeleccionValores.add(preguntaSeleccionItem)) {
            preguntaSeleccionItem = null;
            JsfUtil.addSuccessMessage("Opción agregada con éxito");
        } else {
            JsfUtil.addErrorMessage("No se pudo agregar Opciónr");
        }
    }

    /**
     * Remover Opciones a las Preguntas del Tipo Seleccion
     */
    public void removeItemFromSeleccion() {
        if (preguntaSeleccionValores.remove(preguntaSeleccionItem)) {
            preguntaSeleccionItem = null;
            JsfUtil.addSuccessMessage("Opción eliminada con éxito");
        } else {
            JsfUtil.addErrorMessage("No se pudo eliminar la Opción");
        }
    }

    /**
     * Opciones de las Preguntas del Tipo Seleccion
     *
     * @return
     */
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

    /**
     *
     * @return
     */
    public String prepareCancel() {
        //setEncuesta(null);
        current = null;
        recreateModel();
        return "createQuestions?faces-redirect=true";
    }

    /**
     *
     * @return
     */
    public String prepareCreate() {
        recreateModel();
        return "createQuestions?faces-redirect=true";
    }

    /**
     *
     * @return
     */
    public String prepareDelete() {
        preguntaSeleccionValores = new ArrayList();
        current = (Pregunta) getPreguntaItems().getRowData();
        tipoPregunta = current.getTipo();
        preguntaTexto = current.getPregunta();
        for (RespuestaConf opcionConf : current.getRespuestaConfList()) {
            promptPreguntaTextual = opcionConf.getPrompt();
            preguntaSeleccionValores.add(opcionConf.getOpcion());
        }
        resetMessage();
        message3 = false;
        return "deleteQuestions?faces-redirect=true";
    }

    /**
     * Eliminar Preguntas
     *
     * @return
     */
    public String deletePregunta() {
        respuestaFacade = getRespuestaFacade();
        preguntaFacade = getPreguntaFacade();

        int totalRespuestas = 0;
        totalRespuestas = respObtenidaFacade.findRespuestaObtenida(current);

        if (totalRespuestas == 0) {
            opcionsList = respuestaFacade.findRespuestaConf(current);
            current.setRespuestaConfList(opcionsList);
            for (RespuestaConf opcionConf : current.getRespuestaConfList()) {
                respuestaFacade.remove(opcionConf);
            }
            //current.setRespuestaConfList(null);
            preguntaFacade.remove(current);
            //tipoPregunta = 0;
            //preguntaTexto = null;
            //promptPreguntaTextual = null;
            //preguntaSeleccionValores.clear();
            message2 = false;
            message1 = true;
            JsfUtil.addSuccessMessage("Pregunta eliminada de la encuesta");
        } else if (totalRespuestas > 0) {
            JsfUtil.addErrorMessage("La pregunta no puede ser eliminada, ya que tiene respuestas asociadas");
            message1 = false;
            message2 = true;
        }
        recreateModel();
        return prepareCreate();
    }

    private void resetMessage() {
        message1 = false;
        message2 = false;
    }

    /**
     * Limpiar Variables para mostrar Listados actualizados
     */
    private void recreateModel() {
        preguntaTexto = null;
        tipoPregunta = 0;
        preguntaItems = null;
        promptPreguntaTextual = null;
        message3 = false;
    }
//    public void exportarReportePDF(ActionEvent actionEvent) throws JRException, IOException {
//        String extension = "PDF";
//        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reporteEncuestaPdf.jasper");
//        exportarReporte(extension, jasperFileAddress);
//
//    }
//    public void exportarReporte(String extension, String jasperFileAddress) {
//        try {
//            List<JasperBeanEncuestas> myList;
//            JasperManagement jm = new JasperManagement();
//            String logoAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/ferretotallogo.jpg");
//            Map parametros = new HashMap();          
//            
//            parametros.put("logo", logoAddress);
//            parametros.put("nombrereporte", nombreReporte);
//            
//            getPreguntaList();
//
//            myList = jm.FillListEncuestas(preguntaList);
//            jm.FillReportEncuesta(parametros, myList, extension, jasperFileAddress, nombreReporte);
//
//        } catch (JRException | IOException ex) {
//            Logger.getLogger(PreguntaBeanController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
}
