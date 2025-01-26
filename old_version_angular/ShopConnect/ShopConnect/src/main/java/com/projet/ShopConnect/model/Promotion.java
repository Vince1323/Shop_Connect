package com.projet.ShopConnect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Promotion extends Identified {

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String description;

    private String dateDebut;
    private String dateFin;
    private String creePar;
    private Double valeurRemise;
    private String typeRemise;

    @ManyToOne
    private User utilisateur;

    @OneToMany(mappedBy = "promotion" , fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionProduit> produits;
}