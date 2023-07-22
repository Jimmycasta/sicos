package com.sicos.service;

import com.sicos.entity.Reportes;

import java.util.List;

public interface IReportesService {

    List<Reportes> buscarTodos();

    List<Reportes> buscarCuentaCobroPorId(int id);

    void guardar(Reportes reportes);

    void eliminar(int id);

    String buscarMaxNumeroFactura();

    Reportes buscarPorCuentaCobro(String cuentaCobro);

    List<String> buscarFechaInicio();


}
