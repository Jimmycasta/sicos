package com.sicos.controller;

import com.sicos.entity.Reportes;
import com.sicos.service.IReportesService;
import com.sicos.service.IServiciosService;
import com.sicos.utilities.Fecha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public String detalleReportes(Reportes reportes, Model model, RedirectAttributes attributes) {

        List<String> listaFechaInicio = reportesService.buscarFechaInicio();
        String nombreMes = reportes.getFechaInicio().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        String fechaNueva = String.valueOf(reportes.getFechaInicio());

        if (!listaFechaInicio.contains(fechaNueva)) {
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

        } else {
            attributes.addFlashAttribute("mensaje", "El mes de " + nombreMes + " ya fue facturado!");
            return "redirect:/reportes/fecha";
        }
    }

    @PostMapping("/guardar")
    public String guardarReporte(Reportes reportesDatos, RedirectAttributes attributes) {

        reportesService.guardar(reportesDatos);
        attributes.addFlashAttribute("mensaje", "Cuenta de cobro guardada!");
        return "redirect:/reportes/listar";
    }

    @GetMapping("/listar")
    public String listarCuentaCobro(@RequestParam Map<String,Object> params, Model model) {
        int currentPage = params.get("page") != null ? (Integer.parseInt(params.get("page").toString()) -1) : 0;
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize);
        Page<Reportes> listaReportes = reportesService.buscarTodosPage(pageRequest);

        int totalPages = listaReportes.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("listaReportes", listaReportes);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("nextPage", currentPage + 2);
        model.addAttribute("prevPage", currentPage);
        model.addAttribute("lastPage", totalPages);
        return "reportes/listarCuentaCobro";
    }

/*    @GetMapping("/listar")
    public String listarCuentaCobro(Model model) {
        List<Reportes> listaReportes = reportesService.buscarTodos();
        model.addAttribute("listaReportes", listaReportes);
        return "reportes/listarCuentaCobro";
    }
    */

    //Método para generar el número consecutivo de Cuenta cobro.
    public String getNumeroFactura() {
        String numeroFactura = reportesService.buscarMaxNumeroFactura();

        if (numeroFactura == null) {
            numeroFactura = String.format("%04d", +1);

        } else {
            int num = Integer.parseInt(numeroFactura);
            numeroFactura = String.format("%04d", num + 1);
        }
        return numeroFactura;
    }
}


