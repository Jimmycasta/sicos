package com.sicos.controller;

import com.sicos.entity.Reportes;
import com.sicos.entity.Servicios;
import com.sicos.service.IReportesService;
import com.sicos.service.IServiciosService;
import com.sicos.utilities.Fecha;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class JasperReportController {

    @Autowired
    private IReportesService reportesService;
    @Autowired
    private IServiciosService serviciosService;

    //Endpoint para exportar pdf al disco duro.
    @GetMapping("/pdf/{factura}")
    public void exportaPdf(HttpServletResponse response, @PathVariable("factura") String factura) throws IOException, JRException {

        //trae los datos de la base de datos y se la pasa a una List.
        Reportes reportes = reportesService.buscarPorCuentaCobro(factura);
        List<Reportes> dataList = new ArrayList<>();
        dataList.add(reportes);

        //Se llena la fuente de datos con la lista que se llenó anteriormente.
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dataList);

        //Se crea un mapa de parámetros y se le pasa los parámetros que no se pueden pasar en la fuente de datos o base de datos.
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("beanCollectionDataSource", beanCollectionDataSource);
        parameters.put("firma", new FileInputStream("src/main/resources/static/reports/images/firma.png"));

        //Se compila el archivo jrxml y con un New FileInputStream, se le pasa la ruta de donde esta físicamente el archivo.
        JasperReport jasperReport = JasperCompileManager
                .compileReport(new FileInputStream("src/main/resources/static/reports/cuentaCobro.jrxml"));

        //Se guarda el archivo compilado para mejorar el rendimiento.
        JRSaver.saveObject(jasperReport, "cuentaCobro.jasper");

        //Se llena el reporte con el reporte compilado anteriormente, con los parámetros y la fuente de datos.
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanCollectionDataSource);

        //Se formatea la fecha que va a ir en el nombre del archivo.
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fecha = fechaActual.format(formatoFecha);

        //Se pone el nombre del archivo.
        String nombreArchivo = "CuentaCobro" + "_" + fecha + ".pdf";

        //Se descarga el pdf en disco.
        response.setContentType("application/x-download");
        response.addHeader("content-disposition", "attachment; filename=" + nombreArchivo);
        OutputStream outputStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }


    //End point para Exportar el excel al disco duro.
    @GetMapping("/xlsx")
    public void exportaXlsx(HttpServletResponse response) throws IOException, JRException {

        //Establece la hora de inicio y hora final del mes anterior a facturar.
        LocalDate fechaInicio = YearMonth.now().minusMonths(1).atDay(1);
        LocalDate fechaFin = YearMonth.now().minusMonths(1).atEndOfMonth();

        //trae los datos de la base de datos y se la pasa a una List.
        List<Servicios> dataList = serviciosService.buscarUltimoMes(fechaInicio,fechaFin);

        //Se llena la fuente de datos con la lista que se llenó anteriormente.
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dataList);

        //Se crea un mapa de parámetros y se le pasa los parámetros que no se pueden pasar en la fuente de datos o base de datos.
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("beanCollectionDataSource", beanCollectionDataSource);
        //parameters.put("firma", new FileInputStream("src/main/resources/static/reports/images/firma.png"));

        //Se compila el archivo jrxml y con un New FileInputStream, se le pasa la ruta de donde esta físicamente el archivo.
        JasperReport jasperReport = JasperCompileManager
                .compileReport(new FileInputStream("src/main/resources/static/reports/CuentaCobro_xlsx.jrxml"));

        // Se guarda el archivo compilado para mejorar el rendimiento.
        JRSaver.saveObject(jasperReport, "CuentaCobro_xlsx.jasper");

        //Se llena el reporte con el reporte compilado anteriormente, con los parámetros y la fuente de datos.
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanCollectionDataSource);

        //Se formatea la fecha que va a ir en el nombre del archivo.
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fecha = fechaActual.format(formatoFecha);
        String nombreArchivo = "CuentaCobro" + "_" + fecha + ".xlsx";

        //Especifica tipo de contenido a excel y se fuerza la descarga del archivo.
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

        //Se configura los parametros del archivo xlsx a exportar.
        JRXlsxExporter exporter = new JRXlsxExporter();
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(true);
        configuration.setOnePagePerSheet(false);
        configuration.setRemoveEmptySpaceBetweenColumns(true);
        configuration.setWhitePageBackground(true);
        configuration.setAutoFitRow(true);
        exporter.setConfiguration(configuration);
        configuration.setSheetNames(new String[] {"Mes de " + Fecha.getMes(fechaFin)});

        //Se prepara el exportador.
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        exporter.exportReport();
    }
}



