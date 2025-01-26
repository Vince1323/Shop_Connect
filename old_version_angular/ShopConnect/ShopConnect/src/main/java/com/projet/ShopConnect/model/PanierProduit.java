package com.projet.ShopConnect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PanierProduit extends Identified {

    @Column(nullable = false)
    private Integer quantite; // Quantité du produit dans le panier

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panier_id", nullable = false)
    private Panier panier; // Le panier auquel ce produit est associé

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit; // Le produit lié au panier

    // Setter pour quantite avec validation
    public void setQuantite(Integer quantite) {
        if (quantite == null || quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive.");
        }
        this.quantite = quantite;
    }
}
