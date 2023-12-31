package com.sicos.implement;

import com.sicos.entity.Reportes;
import com.sicos.repository.ReportesRepository;
import com.sicos.service.IReportesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteService implements IReportesService {

    @Autowired
    private ReportesRepository reportesRepository;


    @Override
    public List<Reportes> buscarTodos() {
        return reportesRepository.findAll();
    }

    @Override
    public Page<Reportes> buscarTodosPage(Pageable pageable) {
        return reportesRepository.findAll(pageable);
    }


    @Override
    public List<Reportes> buscarCuentaCobroPorId(int id) {

        List<Reportes> lista = new ArrayList<>();
        Optional<Reportes> optional = reportesRepository.findById(id);
        if (optional.isPresent()) {
            lista.add(optional.get());
            return lista;
        }
        return null;
    }

    @Override
    public void guardar(Reportes reportes) {
        reportesRepository.save(reportes);
    }

    @Override
    public void eliminar(int id) {

    }

    @Override
    public String buscarMaxNumeroFactura() {

        return reportesRepository.findMaxNumeroFactura();
    }

    @Override
    public Reportes buscarPorCuentaCobro(String cuentaCobro) {

        Optional<Reportes> optional = Optional.ofNullable(reportesRepository.findByNumeroFactura(cuentaCobro));
        return optional.orElseGet(() -> reportesRepository.findByNumeroFactura(cuentaCobro));

    }

    @Override
    public List<String> buscarFechaInicio() {
        return reportesRepository.getFechaFin();
    }


}
