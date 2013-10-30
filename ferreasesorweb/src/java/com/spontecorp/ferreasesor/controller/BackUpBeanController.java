/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.utilities.JpaUtilities;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "backUpBeanController")
@SessionScoped
public class BackUpBeanController implements Serializable {

    private UploadedFile file;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    //Datos de la BD a respaldar
    private String dbName = JpaUtilities.DB_NAME;
    private String dbUserName = JpaUtilities.DB_USER;
    private String dbPassword = JpaUtilities.DB_PASSWORD;
    //Ruta para crear el archivo .sql
    private String savePath = "C:\\ferreasesorBackUpDB\\";
    //Nombre del archivo .sql a crear
    private String nameFile;
    private String restoreFile;
    //Ruta del mysqldump.exe 
    private String mysqldump = "\"C:\\Program Files\\MySQL\\MySQL Server 5.1\\bin\\mysqldump.exe\"";
    //Ruta del mysql.exe 
    String mysql = "\"C:\\Program Files\\MySQL\\MySQL Server 5.1\\bin\\mysql.exe\"";

    public BackUpBeanController() {
    }

    /**
     * BackUp de la DB Método para crear Respaldo de la Base de Datos
     *
     * @param evt
     * @return
     */
    public boolean backUpDB(ActionEvent evt) {

        boolean result = false;
        nameFile = "ferreasesorDB_" + sdf.format((new Date())) + ".sql";

        //Se arma el comando a ejecutar
        String executeCmd = mysqldump + " -u " + dbUserName + " -p" + dbPassword
                + " --add-drop-database -B " + dbName + " -r " + savePath + nameFile;

        Process runtimeProcess;
        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                //System.out.println("Backup created successfully");
                JsfUtil.addSuccessMessage("Respaldo de la Base de Datos creado con éxito! "
                        + "Ruta del archivo creado: " + savePath + nameFile);
                result = true;
            } else {
                //System.out.println("Could not create the backup");
                JsfUtil.addErrorMessage("No se pudo crear el Respaldo de la Base de Datos.");
                result = false;
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error al crear el Respaldo de la Base de Datos: " + ex.getMessage());
        }

        return result;
    }

    /**
     * Restore de la DB Método para Restaurar la Base de Datos
     *
     * @param evt
     * @return
     */
    public boolean restoreDB(ActionEvent evt) throws IOException {

        boolean result = false;
        //Tipo de archivo permitido (.sql)
        String contentType = "application/octet-stream";

        if (file != null) {
            if (file.getContentType().equalsIgnoreCase(contentType)) {
                restoreFile = file.getFileName();
                
                //Se arma el comando a ejecutar
                String[] executeCmd = new String[]{mysql, "--user=" + dbUserName, "--password="
                    + dbPassword, dbName, "-e", "source " + savePath + restoreFile};
                Process runtimeProcess;
                
                try {
                    runtimeProcess = Runtime.getRuntime().exec(executeCmd);
                    int processComplete = runtimeProcess.waitFor();

                    if (processComplete == 0) {
                        JsfUtil.addSuccessMessage("La Base de Datos fue restaurada con éxito!");
                        result = true;
                    } else {
                        JsfUtil.addErrorMessage("No se pudo restaurar la Base de Datos.");
                        result = false;
                    }
                } catch (IOException | InterruptedException ex) {
                    System.out.println("Error al restaurar la Base de Datos: " + ex.getMessage());
                }
            } else {
                JsfUtil.addErrorMessage("Selecciones un archivo .sql");
            }
        } else {
            JsfUtil.addErrorMessage("Selecciones un archivo .sql");
        }
        file = null;
        restoreFile = null;
        return result;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
}
