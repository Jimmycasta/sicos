package com.sicos.service;

import com.sicos.entity.Reportes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IReportesService {

    List<Reportes> buscarTodos();
    Page<Reportes> buscarTodosPage(Pageable pageable);

    List<Reportes> buscarCuentaCobroPorId(int id);

    void guardar(Reportes reportes);

    void eliminar(int id);

    String buscarMaxNumeroFactura();

    Reportes buscarPorCuentaCobro(String cuentaCobro);

    List<String> buscarFechaInicio();


}
