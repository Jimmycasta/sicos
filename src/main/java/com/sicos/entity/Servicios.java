package com.sicos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@Table(name = "servicios")
public class Servicios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaAtencion;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaSolucion;
    @NotBlank(message = "Debe ingresar un usuario")
    private String usuario;
    @NotNull(message = "Debe ingresar un ticket")
    private int ticket;
    @NotBlank(message = "Debe de ingresar el motivo del ticket ")
    @Column(length = 2000)
    private String motivoMia;
    @NotBlank(message = "Debe de ingresar una solución")
    @Column(length = 2000)
    private String solucion;
    @OneToOne
    @JoinColumn(name = "idEstado")
    private Estado estado;

    public Servicios() {
        this.fechaAtencion = LocalDate.now();
    }
}
