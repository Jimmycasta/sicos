package com.sicos.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="estados")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
}
