package com.sicos.service;

import com.sicos.entity.Estado;

import java.util.List;

public interface IEstadoService {

    List<Estado> buscarTodos();

    void guardar(Estado estado);

    void eliminar(int id);
}
