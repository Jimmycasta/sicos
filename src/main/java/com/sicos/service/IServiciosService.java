package com.sicos.service;

import com.sicos.entity.Servicios;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

public interface IServiciosService {

    List<Servicios> buscarTodos();

    Servicios buscarPorId(int id);

    void guardar(Servicios servicios);

    void eliminar(int id);

    int diasTrabajados(LocalDate fechaInicio, LocalDate fechaFin);

    int contarTicket(LocalDate fechaInicio, LocalDate fechaFin);


}
