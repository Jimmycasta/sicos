package com.sicos.service;

import com.sicos.entity.Reportes;

import java.util.List;
import java.util.Optional;

public interface IReportesService {

    List<Reportes> buscarTodos();

    List<Reportes> buscarCuentaCobroPorId(int id);

    void guardar(Reportes reportes);

    void eliminar(int id);

    String buscarMaxNumeroFactura();

    Reportes buscarPorCuentcaCobro(String cuentaCobro);


}
