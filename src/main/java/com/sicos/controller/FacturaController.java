package com.sicos.controller;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FacturaController {

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadInvoice() throws JRException, IOException {

        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(Arrays.asList("Prueba", "test1"), false);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("total", "7000");

        JasperReport compileReport = JasperCompileManager
                .compileReport(new FileInputStream("src/main/resources/static/reports/CuentaCobro.jrxml"));

        JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);

        byte data[] = JasperExportManager.exportReportToPdf(jasperPrint);

        System.err.println(data);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=CuentaCobro.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
    }
}
