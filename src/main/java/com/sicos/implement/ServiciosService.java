package com.sicos.implement;

import com.sicos.entity.Servicios;
import com.sicos.repository.ServiciosRepository;
import com.sicos.service.IServiciosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ServiciosService implements IServiciosService {

    @Autowired
    private ServiciosRepository serviciosRepository;

    @Override
    public List<Servicios> buscarTodos() {
        return serviciosRepository.findAll();
    }

    @Override
    public Page<Servicios> buscarTodosPage(Pageable pageable) {
        return serviciosRepository.findAll(pageable);
    }

    @Override
    public Servicios buscarPorId(int id) {
        Optional<Servicios> optional = serviciosRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public void guardar(Servicios servicios) {
        serviciosRepository.save(servicios);

    }

    @Override
    public void eliminar(int id) {
        serviciosRepository.deleteById(id);

    }

    @Override
    public int diasTrabajados(LocalDate fechaInicio, LocalDate fechaFin) {
        return serviciosRepository.getDiasTrabajados(fechaInicio, fechaFin);
    }

    @Override
    public int contarTicket(LocalDate fechaInicio, LocalDate fechaFin) {
        return serviciosRepository.getContarTicket(fechaInicio, fechaFin);
    }

    @Override
    public List<Servicios> buscarUltimoMes(LocalDate fechaInicio, LocalDate fechaFin) {
        return serviciosRepository.getUltimoMes(fechaInicio,fechaFin);
    }

}
