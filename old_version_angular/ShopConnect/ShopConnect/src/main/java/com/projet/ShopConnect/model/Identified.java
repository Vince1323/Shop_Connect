package com.projet.ShopConnect.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Identified { // Classe d'héritage pour toutes les tables de la DB
    @Id // Fait comprendre à SPRING que c'est une PrimaryKey
    @GeneratedValue(strategy = GenerationType.IDENTITY) // pour l'auto-incrémentation

    private Long id;

    public Long getId() {
        return id;
    }
    private boolean deleted;

    /*  GESTION DES LOGGS  */
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedBy
    private String modifiedBy;
    @LastModifiedDate
    private LocalDateTime modifiedAt;

}
