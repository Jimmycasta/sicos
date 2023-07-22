package com.sicos.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "reportes")
@Data
public class Reportes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCuentaCobro;
    @Column(unique = true)
    private String numeroFactura;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaReporte;
    private int diasTrabajados;
    private int miasAtendidos;
    private String mesFacturado;
    @Column(columnDefinition = "double")
    private double totalCuentaCobro;

    public Reportes() {
    }
}
