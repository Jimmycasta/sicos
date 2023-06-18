package com.sicos.repository;

import com.sicos.entity.Servicios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


public interface ServiciosRepository extends JpaRepository<Servicios, Integer> {

    @Query(value = "select COUNT(distinct fechaSolucion) from servicios s where s.fechaSolucion between :fechaInicio and :fechaFin", nativeQuery = true)
     int getDiasTrabajados(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);


    @Query(value = "select count(ticket) from servicios s where s.fechaSolucion between :fechaInicio and :fechaFin", nativeQuery = true)
     int getContarTicket(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);



}
