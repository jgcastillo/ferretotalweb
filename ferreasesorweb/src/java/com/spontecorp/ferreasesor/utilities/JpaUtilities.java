package com.spontecorp.ferreasesor.utilities;

import com.spontecorp.ferreasesor.security.Cifrador;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jgcastillo
 */
public class JpaUtilities {

    private static final Logger logger = LoggerFactory.getLogger(JpaUtilities.class);
    private static String PERSITENCE_UNIT = "FerreAsesorWebPU";
    public static int HABILITADO = 1;
    public static int INHABILITADO = 0;
    public static final Integer ID_TIENDA = 1;
    public static final int FERIADO = 1;
    public static final int NORMAL = 0;
    //Datos de la BD a respaldar
    public static final String DB_NAME = "ferreasesor";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "root";
    //Ruta del mysqldump.exe 
    //Hacer BackUp de la BD
    public static final String mysqldump = "\"C:\\Program Files\\MySQL\\MySQL Server 5.1\\bin\\mysqldump.exe\"";
    //Ruta del mysql.exe 
    //Hacer Restore de la BD
    public static final String mysql = "\"C:\\Program Files\\MySQL\\MySQL Server 5.1\\bin\\mysql.exe\"";

    public JpaUtilities() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory(PERSITENCE_UNIT);
//        String[] props = null;
//        try {
//            props = readPreFile();    
//        } catch (Exception e) {
//        }
//        return setProperties(props[0], props[1], props[2], props[3]);
    }

    private static EntityManagerFactory setProperties(String url, String psw, String driver, String user) {
        Map props = new HashMap();
        props.put("javax.persistence.jdbc.url", url);
        props.put("javax.persistence.jdbc.password", psw);
        props.put("javax.persistence.jdbc.driver", driver);
        props.put("javax.persistence.jdbc.user", user);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSITENCE_UNIT, props);
        return emf;
    }

    private static String[] readPreFile() throws IOException {
        Cifrador cipher = new Cifrador();
        String[] props = new String[5];
        BufferedReader input = null;
        try {
            int i = 0;
            String line;
            String decode;
            String temp = null;
            input = new BufferedReader(new FileReader("pre.spt"));

            while ((line = input.readLine()) != null) {
                props[i++] = line;
            }
//            while((line = input.readLine()) != null){
//                System.out.println("read: " + line);
//                if(line.endsWith("=")){
//                    if(temp != null){
//                        decode = cipher.decrypt(temp + line);
//                        temp = null;
//                    } else {
//                        decode = cipher.decrypt(line);
//                    }
//                    System.out.println(decode);
//                    props[i++] = decode;
//                } else {
//                    temp = line;
//                }
//            }
        } catch (IOException e) {
            logger.error("Error leyendo archivo de configuración" + e);
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return props;
    }

    /**
     * Guardo el Archivo en la Carpeta Temporal
     *
     * @param mFile
     * @return
     */
    public static boolean saveFile(UploadedFile mFile, String srcFile) {
        boolean retorno = false;
        long FileLength = 0;

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
            System.out.println("Error al salvar el archivo en la ruta: " + srcFile);
            return false;
        }
        return retorno;
    }

    /**
     * Elimino el Archivo de la Carpeta Temporal una vez Restaurado
     *
     * @param srcFile
     */
    public static boolean deleteFile(String srcFile) {
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

}
