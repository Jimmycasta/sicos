package com.sicos.controller;

import com.sicos.entity.Estado;
import com.sicos.entity.Servicios;
import com.sicos.service.IEstadoService;
import com.sicos.service.IServiciosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/servicios")
public class ServiciosController {

    @Autowired
    private IServiciosService serviciosService;
    @Autowired
    private IEstadoService estadoService;

    @GetMapping("/lista")
    public String tablaServicios(Model model) {
        List<Servicios> listaServicios = serviciosService.buscarTodos();
        model.addAttribute("listaServicios", listaServicios);
        return "servicios/tablaServicios";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(@ModelAttribute("servicios") Servicios servicios, Model model) {
        List<Estado> listaEstado = estadoService.buscarTodos();
        servicios.setFechaAtencion(LocalDate.now());
        servicios.setFechaSolucion(LocalDate.now());
        model.addAttribute("estado", listaEstado);
        return "servicios/formServicios";
    }

    @PostMapping("/guardar")
    public String guardarRegistro(@Valid Servicios servicios, BindingResult result, Model model, RedirectAttributes attributes) {
        List<Estado> listaEstado = estadoService.buscarTodos();
        if (result.hasErrors()) {
            servicios.setFechaAtencion(LocalDate.now());
            servicios.setFechaSolucion(LocalDate.now());
            model.addAttribute("estado", listaEstado);
            return "servicios/formServicios";
        }

        serviciosService.guardar(servicios);
        attributes.addFlashAttribute("mensaje","Servicio guardado correctamente!");
        return "redirect:/servicios/lista";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable("id") int id, Model model) {
        Servicios servicio = serviciosService.buscarPorId(id);
        model.addAttribute("servicio", servicio);
        return "/servicios/detalle";
    }
}
