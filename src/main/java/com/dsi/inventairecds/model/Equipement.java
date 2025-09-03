package com.dsi.inventairecds.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // PC, Imprimante, etc.
    private String nomUtilisateur;
    private String prenomUtilisateur;
    // private String numeroSerie;
    // private LocalDate dateAchat;
    private String etatActuel; // OK, Panne, Migration en cours...
}