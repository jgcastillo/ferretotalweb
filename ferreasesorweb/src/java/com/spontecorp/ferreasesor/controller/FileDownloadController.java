/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.entity.ConfigurationDb;
import com.spontecorp.ferreasesor.jpa.ConfigurationDbFacade;
import com.spontecorp.ferreasesor.utilities.JpaUtilities;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "fileDownloadController")
@ViewScoped
public final class FileDownloadController implements Serializable {

    private StreamedContent downloadFile;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    //Datos de la BD 
    private String dbName = null;
    private String dbUserName = null;
    private String dbPassword = null;
    private String mysqldump = null;
    private String savePath = null;
    //Nombre del archivo .sql a crear
    private String nameFile = null;

    public FileDownloadController() {
        boolean findConfig = configurarDB();

        if (findConfig) {
            backUpDB();
        } else {
            JsfUtil.addErrorMessage("No se ha Configurado la Base de Datos.");
        }
    }

    /**
     * Busco la configuración de la Base de Datos
     * @return 
     */
    public boolean configurarDB() {
        List<ConfigurationDb> configuracionDB = new ArrayList<>();
        ConfigurationDb configDB = new ConfigurationDb();
        boolean isConfig = false;
        try {
            InitialContext context = new InitialContext();
            ConfigurationDbFacade configuracionDBFacade = (ConfigurationDbFacade) context.lookup("java:module/ConfigurationDbFacade");
            configuracionDB = configuracionDBFacade.findAll();
            if (configuracionDB.size() > 0) {
                configDB = configuracionDB.get(0);
                dbName = configDB.getDbName();
                System.out.println("dbName: "+dbName);
                dbUserName = configDB.getDbuserName();
                System.out.println("dbUserName: "+dbUserName);
                dbPassword = configDB.getDbPassword();
                System.out.println("dbPassword: "+dbPassword);
                mysqldump = "\"" + configDB.getPathMysqldump() + "\"";
                System.out.println("mysqldump: "+mysqldump);
                isConfig = true;
            } else {
                isConfig = false;
            }
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(JpaUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isConfig;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    /**
     * BackUp de la DB. Método para Respaldar la Base de Datos
     */
    public void backUpDB() {

        boolean result = false;
        nameFile = "ferreasesorDB_" + sdf.format((new Date())) + ".sql";
        savePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/temp/" + nameFile);

        //Se arma el comando a ejecutar
        String executeCmd = mysqldump + " -u " + dbUserName + " -p" + dbPassword
                + " --add-drop-database -B " + dbName + " -r " + savePath;
        
        System.out.println("executeCmd: "+executeCmd);

        Process runtimeProcess;
        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                //System.out.println("Backup created successfully");
                JsfUtil.addSuccessMessage("Respaldo de la Base de Datos creado con éxito! "
                        + "Ruta del archivo creado: " + savePath);
                result = true;

                InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/temp/" + nameFile);
                downloadFile = new DefaultStreamedContent(stream, "application/octet-stream", nameFile);

                //Elimino el archivo de la Carpeta Temporal
                JpaUtilities.deleteFile(savePath);
            } else {
                //System.out.println("Could not create the backup");
                JsfUtil.addErrorMessage("No se pudo crear el Respaldo de la Base de Datos.");
                result = false;
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error al crear el Respaldo de la Base de Datos: " + ex.getMessage());
        }
    }
}
