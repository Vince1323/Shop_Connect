package com.projet.ShopConnect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class CommandeProduit extends Identified {

    private Integer quantite;

    @Column(nullable = false)
    private BigDecimal prixUnitaire;

    @ManyToOne
    private Commande commande;

    @ManyToOne
    private Produit produit;
}