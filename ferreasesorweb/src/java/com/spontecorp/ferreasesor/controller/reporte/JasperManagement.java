/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.Asesor;
import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.entity.Turno;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author sponte03
 */
public class JasperManagement {

    private List<JasperBean> lista = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public List<JasperBean> FillList(List<Object[]> result) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String nombre = "";
        for (Object[] array : result) {
            if (array[0] instanceof Boton) {
                nombre = ((Boton) array[0]).getUbicacion();
            } else if (array[0] instanceof Asesor) {
                nombre = ((Asesor) array[0]).getNombre() + " " + ((Asesor) array[0]).getApellido();
            } else if (array[0] instanceof Turno) {
                nombre = ((Turno) array[0]).getNombre();
            } else if (array[0] instanceof Date) {
                nombre = sdf.format(((Date) array[0]));
            }
            JasperBean jb = new JasperBean(nombre, ((Long) array[1]).doubleValue());
            lista.add(jb);
        }
        return lista;
    }

    public List<JasperBean> FillList(String[] rango, Double[] dominio) {
        for (int i = 0; i < rango.length; i++) {
            JasperBean jb = new JasperBean(rango[i], dominio[i]);
            //System.out.println(dominio[i]);
            lista.add(jb);
        }
        return lista;
    }

    public void FillReport(Map parametros, List<JasperBean> lista, String extension, String nombreJasper) throws JRException, IOException {

        JRExporter exporter;
        File file = new File(nombreJasper);
        JasperReport reporte = (JasperReport) JRLoader.loadObject(file);
        JRBeanCollectionDataSource jbs = new JRBeanCollectionDataSource(lista);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, jbs);

//        if ("PDF".equals(extension)) {
//            exporter = new JRPdfExporter();
//        } else {
//            exporter = new JRXlsExporter();
//        }

        //exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        //exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new java.io.File(storage + "report." + extension));
        //exporter.exportReport();

        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report_" + sdf.format((new Date())) + "." + extension);
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

        if ("PDF".equals(extension)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        } else {
            JRXlsxExporter docxExporter = new JRXlsxExporter();
            docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            docxExporter.exportReport();
        }
        
    }

    public JasperManagement() {
        super();
    }
}
