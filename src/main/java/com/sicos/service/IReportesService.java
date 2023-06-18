package com.sicos.service;

import com.sicos.entity.Reportes;

import java.util.List;

public interface IReportesService {

    List<Reportes> buscarTodos();
    Reportes buscarPorId(int id);
    void guardar(Reportes reportes);
    void eliminar(int id);
    String buscarMaxNumeroFactura();


}
