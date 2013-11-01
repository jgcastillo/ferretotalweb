/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.utilities.JpaUtilities;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private UploadedFile uploadedFile;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    //Datos de la BD a respaldar
    private String dbName = JpaUtilities.DB_NAME;
    private String dbUserName = JpaUtilities.DB_USER;
    private String dbPassword = JpaUtilities.DB_PASSWORD;
    //Ruta para crear el archivo .sql
    private String srcFile;
    private String savePath;
    //Nombre del archivo .sql a crear
    private String nameFile;
    private String restoreFile;
    //Ruta del mysqldump.exe 
    //Hacer BackUp de la BD
    private String mysqldump = "\"C:\\Program Files\\MySQL\\MySQL Server 5.1\\bin\\mysqldump.exe\"";
    //Ruta del mysql.exe 
    //Hacer Restore de la BD
    String mysql = "\"C:\\Program Files\\MySQL\\MySQL Server 5.1\\bin\\mysql.exe\"";
    private long FileLength = 0;

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
        savePath = "C:\\ferreasesorBackUpDB\\";
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

        if (uploadedFile != null) {

            if (uploadedFile.getContentType().equalsIgnoreCase(contentType)) {
                restoreFile = uploadedFile.getFileName();

                //Ruta + Nombre del Archivo a Restaurar
                srcFile = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/temp/" + restoreFile);

                //Se copia el archivo a la Carpeta temporal
                result = saveFile(uploadedFile, srcFile);

                if (result) {
                    //Se arma el comando a ejecutar
                    String[] executeCmd = new String[]{mysql, "--user=" + dbUserName, "--password="
                        + dbPassword, dbName, "-e", "source " + srcFile};
                    Process runtimeProcess;

                try {
                    runtimeProcess = Runtime.getRuntime().exec(executeCmd);
                    int processComplete = runtimeProcess.waitFor();

                    if (processComplete == 0) {
                        JsfUtil.addSuccessMessage("La Base de Datos fue restaurada con éxito!");
                        result = true;
                        //Elimino el archivo de la Carpeta Temporal
                        deleteFile(srcFile);
                    } else {
                        JsfUtil.addErrorMessage("No se pudo restaurar la Base de Datos.");
                        result = false;
                    }
                } catch (IOException | InterruptedException ex) {
                    System.out.println("Error al restaurar la Base de Datos: " + ex.getMessage());
                }

                } else {
                    JsfUtil.addErrorMessage("No se pudo restaurar la Base de Datos.");
                }
            } else {
                JsfUtil.addErrorMessage("Selecciones un archivo .sql");
            }
        } else {
            JsfUtil.addErrorMessage("Selecciones un archivo .sql");
        }
        uploadedFile = null;
        restoreFile = null;
        return result;
    }

    /**
     * Elimino el Archivo de la Carpeta Temporal una vez Restaurado
     * @param srcFile 
     */
    public boolean deleteFile(String srcFile) {
        boolean delete = false;
        try {
            //System.out.println("Delete file: " + srcFile);
            File newfile = new File(srcFile);
            if (newfile.delete()) {
                //System.out.println(newfile.getName() + " is deleted!");
                delete = true;
            } else {
                //System.out.println("Delete operation is failed.");
                delete = false;
            }
        } catch (Exception e) {
            System.out.println("Delete operation is failed.");
        }
        return delete;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    /**
     * Guardo el Archivo en la Carpeta Temporal
     *
     * @param mFile
     * @return
     */
    public boolean saveFile(UploadedFile mFile, String srcFile) {
        boolean retorno = false;
        try {
            File file = new File(srcFile);
            byte[] bytes = mFile.getContents();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(bytes);
                fos.flush();
                fos.close();
            }

            FileLength = file != null ? file.length() : 0;
            retorno = FileLength > 0;

        } catch (Exception e) {
            System.out.println("Clase: " + this.getClass().getName() + ", Error al salvar el archivo en la ruta = " + srcFile);
            return false;
        }
        return retorno;
    }
}
