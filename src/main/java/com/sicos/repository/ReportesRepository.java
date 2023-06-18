package com.sicos.repository;

import com.sicos.entity.Reportes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportesRepository extends JpaRepository<Reportes, Integer> {

    @Query(value = "select max(numeroFactura) from reportes", nativeQuery = true)
    String findMaxNumeroFactura();

}

