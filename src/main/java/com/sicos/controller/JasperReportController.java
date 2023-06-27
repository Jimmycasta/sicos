package com.sicos.controller;

import com.sicos.entity.Reportes;
import com.sicos.service.IReportesService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Controller
public class JasperReportController {

    @Autowired
    private IReportesService reportesService;

    @GetMapping("/pdf/{cuentaCobro}")

    public ResponseEntity<byte[]> descargaCuentaCobro(@PathVariable("cuentaCobro") String cuentaCobro) throws JRException, IOException {
        
        Reportes cuenta = reportesService.buscarPorCuentcaCobro(cuentaCobro);
        List<Reportes>listaReporte = new ArrayList<>();
        listaReporte.add(cuenta);
        System.out.println("listaReporte = " + listaReporte);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaReporte);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("beanCollectionDataSource", beanCollectionDataSource);
        parametros.put("firma", new FileInputStream("src/main/resources/static/reports/images/firma.png"));

        JasperReport compileReport = JasperCompileManager
                .compileReport(new FileInputStream("src/main/resources/static/reports/CuentaCobro.jrxml"));

        JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parametros, beanCollectionDataSource);

        byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
        System.err.println(Arrays.toString(data));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=CuentaCobro.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
    }


    //Controlador para pruebas
    @GetMapping(value = "/cobro")
    public ResponseEntity<byte[]> cobro() throws FileNotFoundException, JRException {

        List<Reportes> lista = reportesService.buscarCuentaCobroPorId(77);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("beanCollectionDataSource", beanCollectionDataSource);
        parametros.put("firma", new FileInputStream("src/main/resources/static/reports/images/firma.png"));

        JasperReport jasperReport = JasperCompileManager
                .compileReport(new FileInputStream("src/main/resources/static/reports/cobro.jrxml"));

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, beanCollectionDataSource);

        byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
        //byte[] data = JasperExportManager.exportReportToPdfFile(jasperPrint);
        System.err.println(Arrays.toString(data));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=cobro.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);

    }

}