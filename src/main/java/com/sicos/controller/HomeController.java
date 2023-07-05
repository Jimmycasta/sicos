package com.sicos.controller;

import com.sicos.entity.Reportes;
import com.sicos.service.IServiciosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Controller
public class HomeController {

    @Autowired
    private IServiciosService serviciosService;

    @GetMapping("/")
    public String home(@ModelAttribute("serviciosHoy") Reportes serviciosHoy, Model model) {

        LocalDate fechaInicio = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate fechaFin = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        serviciosHoy.setDiasTrabajados(serviciosService.diasTrabajados(fechaInicio, fechaFin));
        serviciosHoy.setMiasAtendidos(serviciosService.contarTicket(fechaInicio, fechaFin));

        //Calcula el total facturado, se cambia el total a formato a moneda y se envía a la vista.
        int diasTrabajados = serviciosHoy.getDiasTrabajados();
        double tarifaServicio = 90000;
        double total = diasTrabajados * tarifaServicio;
        DecimalFormat formato = new DecimalFormat("$##,##,###");
        String totalFacturado = formato.format(total);
        model.addAttribute("totalFacturado", totalFacturado);
        return "home";
    }
}
