package com.projet.ShopConnect.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BoutiqueProduit extends Identified {

    // Relation ManyToOne vers Boutique : Une boutique peut avoir plusieurs produits
    @ManyToOne
    private Boutique boutique;

    // Relation ManyToOne vers Produit : Un produit peut être vendu dans plusieurs boutiques
    @ManyToOne
    private Produit produit;
}