package com.sicos.implement;

import com.sicos.entity.Estado;
import com.sicos.repository.EstadoRepository;
import com.sicos.service.IEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EstadoService implements IEstadoService {
    @Autowired
    private EstadoRepository estadoRepository;
    @Override
    public List<Estado> buscarTodos() {
        return estadoRepository.findAll();
    }

    @Override
    public void guardar(Estado estado) {
        estadoRepository.save(estado);

    }

    @Override
    public void eliminar(int id) {
        estadoRepository.deleteById(id);
    }
}
