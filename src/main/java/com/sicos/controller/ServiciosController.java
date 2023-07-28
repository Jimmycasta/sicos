package com.sicos.controller;

import com.sicos.entity.Estado;
import com.sicos.entity.Servicios;
import com.sicos.service.IEstadoService;
import com.sicos.service.IServiciosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/servicios")
public class ServiciosController {

    @Autowired
    private IServiciosService serviciosService;
    @Autowired
    private IEstadoService estadoService;

    @GetMapping(value = "/lista")
    public String tablaServicios(@RequestParam Map<String,Object> params, Model model) {

        int page = params.get("page") != null ? (Integer.parseInt(params.get("page").toString()) - 1) : 0;
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Servicios> listaServicios = serviciosService.buscarTodosPage(pageRequest);

        int totalPages = listaServicios.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }

        model.addAttribute("listaServicios", listaServicios);
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPages);
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
        attributes.addFlashAttribute("mensaje", "Servicio guardado correctamente!");
        return "redirect:/servicios/lista";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable("id") int id, Model model) {
        Servicios servicio = serviciosService.buscarPorId(id);
        model.addAttribute("servicio", servicio);
        return "/servicios/detalle";
    }
}
