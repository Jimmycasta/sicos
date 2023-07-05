package com.sicos.controller;

import com.sicos.entity.Servicios;
import com.sicos.service.IReportesService;
import com.sicos.service.IServiciosService;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class JasperTestController {

    @Autowired
    private IReportesService reportesService;
    @Autowired
    private IServiciosService serviciosService;


    @GetMapping("/test")
    public String test() {

        LocalDate fechaInicio = YearMonth.now().minusMonths(1).atDay(1);
        LocalDate fechaFin = YearMonth.now().minusMonths(1).atEndOfMonth();

        List<Servicios> lista = serviciosService.buscarUltimoMes(fechaInicio, fechaFin);


        return "test";

    }

    @GetMapping("/xl")
    public void exportaXlsx(HttpServletResponse response) throws IOException, JRException {

        //Establece la hora de inicio y hora final del mes anterior a facturar.
        LocalDate fechaInicio = YearMonth.now().minusMonths(1).atDay(1);
        LocalDate fechaFin = YearMonth.now().minusMonths(1).atEndOfMonth();

        //trae los datos de la base de datos y se la pasa a una List.
        List<Servicios> dataList = serviciosService.buscarUltimoMes(fechaInicio, fechaFin);

        //Se llena la fuente de datos con la lista que se llenó anteriormente.
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dataList);

        //Se crea un mapa de parámetros y se le pasa los parámetros que no se pueden pasar en la fuente de datos o base de datos.
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("beanCollectionDataSource", beanCollectionDataSource);
        //parameters.put("firma", new FileInputStream("src/main/resources/static/reports/images/firma.png"));

        //Se compila el archivo jrxml y con un New FileInputStream, se le pasa la ruta de donde está físicamente el archivo.
        JasperReport jasperReport = JasperCompileManager
                .compileReport(new FileInputStream("src/main/resources/static/reports/CuentaCobro_xlsx.jrxml"));

        //Se guarda el archivo compilado para mejorar el rendimiento.
        //JRSaver.saveObject(jasperReport, "CuentaCobro_xlsx.jasper");

        //Se llena el reporte con el reporte compilado anteriormente, con los parámetros y la fuente de datos.
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanCollectionDataSource);

        //Se formatea la fecha que va a ir en el nombre del archivo.
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fecha = fechaActual.format(formatoFecha);

        //Se pone el nombre del archivo.
        String nombreArchivo = "CuentaCobro" + "_" + fecha + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        //response.setContentType("application/octet-stream");
        //response.setContentType("application/vnd.ms-excel");

        JRXlsxExporter exporter = new JRXlsxExporter();

        //Se configura los parametros del archivo xlsx a exportar.
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        configuration.setOnePagePerSheet(true);
        configuration.setRemoveEmptySpaceBetweenColumns(true);
        configuration.setWhitePageBackground(true);
        exporter.setConfiguration(configuration);
        //configuration.setSheetNames(new String[] { "sheet1" });

        //Se prepara el exportador.
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        exporter.exportReport();
    }
}



