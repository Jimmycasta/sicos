package com.sicos.controller;

import com.sicos.entity.Reportes;
import com.sicos.service.IReportesService;
import com.sicos.service.IServiciosService;
import com.sicos.utilities.Fecha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/reportes")
public class ReportesController {
    @Autowired
    private IServiciosService serviciosService;
    @Autowired
    private IReportesService reportesService;

    @GetMapping("/fecha")
    public String fechaReporte(@ModelAttribute("reportes") Reportes reportes) {
        reportes.setFechaInicio(YearMonth.now().minusMonths(1).atDay(1));
        reportes.setFechaFin(YearMonth.now().minusMonths(1).atEndOfMonth());
        return "reportes/fechaReporte";
    }

    @PostMapping("/detalle")
    public String detalleReportes(Reportes reportes, Model model) {
        reportes.setFechaInicio(reportes.getFechaInicio());
        reportes.setFechaFin(reportes.getFechaFin());
        reportes.setFechaReporte(LocalDate.now());
        reportes.setDiasTrabajados(serviciosService.diasTrabajados(reportes.getFechaInicio(), reportes.getFechaFin()));
        reportes.setMiasAtendidos(serviciosService.contarTicket(reportes.getFechaInicio(), reportes.getFechaFin()));
        reportes.setMesFacturado(Fecha.getMes(reportes.getFechaFin()));
        reportes.setNumeroFactura(reportes.getNumeroFactura());
        String nextNumeroFactura = getNumeroFactura();
        int diasTrabajados = reportes.getDiasTrabajados();
        double tarifaServicio = 90000;
        double totalCuentaCobro = diasTrabajados * tarifaServicio;
        reportes.setTotalCuentaCobro(totalCuentaCobro);
        model.addAttribute("reportesDatos", reportes);
        model.addAttribute("nextNumeroFactura", nextNumeroFactura);
        return "reportes/formDetalleReportes";
    }

    @PostMapping("/guardar")
    public String guardarReporte(Reportes reportesDatos, RedirectAttributes attributes) {
        reportesService.guardar(reportesDatos);
        attributes.addFlashAttribute("mensaje", "Cuenta de cobro guardada!");
        return "redirect:/reportes/listar";
    }
    @GetMapping("/listar")
    public String listarCuentaCobro(Model model){
        List<Reportes>listaReportes = reportesService.buscarTodos();
        model.addAttribute("listaReportes",listaReportes);
        return "reportes/listarCuentaCobro";
    }



    //Método para generar el número consecutivo de Cuenta cobro.
    public String getNumeroFactura() {

        String numeroFactura = reportesService.buscarMaxNumeroFactura();

        if (numeroFactura == null) {
            numeroFactura = "00001";

        } else {

            int consecutivo = Integer.parseInt(numeroFactura);
            consecutivo += 1;
            String ultimaFactura = "0000" + consecutivo;
            numeroFactura = ultimaFactura;
        }
        return numeroFactura;
    }

}


