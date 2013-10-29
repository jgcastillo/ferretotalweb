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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "backUpBeanController")
@SessionScoped
public class BackUpBeanController implements Serializable {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    
    public BackUpBeanController() {
    }

    /**
     * BackUp DB
     * Método para crear Respaldo de la Base de Datos
     * @param evt
     * @return 
     */
    public boolean backUpDB(ActionEvent evt) {
        
        boolean result = false;
        
        //Ruta para crear el archivo .sql
        String savePath = "C:\\ferreasesorBackUpDB\\";

        //Nombre del archivo .sql a crear
        String nameFile = "backUpDB_"+ sdf.format((new Date())) + ".sql";
        
        //Ruta del mysqldump.exe 
        String mysqldump = "\"C:\\Program Files\\MySQL\\MySQL Server 5.1\\bin\\mysqldump.exe\"";
        
        //Datos de la BD a respaldar
        String dbName = JpaUtilities.DB_NAME;
        String dbUserName = JpaUtilities.DB_USER;
        String dbPassword = JpaUtilities.DB_PASSWORD;
        
        //Se arma el comando a ejecutar
        String executeCmd = mysqldump + " -u " + dbUserName + " -p" + dbPassword 
                + " --add-drop-database -B " + dbName + " -r " + savePath + nameFile;
        
        Process runtimeProcess;

        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                //System.out.println("Backup created successfully");
                JsfUtil.addSuccessMessage("Backup de la Base de Datos creado con éxito! "
                        + "Ruta del archivo creado: " + savePath + nameFile);
                result = true;
            } else {
                //System.out.println("Could not create the backup");
                JsfUtil.addErrorMessage("No se pudo crear el Backup de la Base de Datos.");
                result = false;
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error al crear el Backup de la Base de Datos: " + ex.getMessage());
        }
        return result;
    }

}
