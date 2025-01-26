package com.projet.ShopConnect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Panier extends Identified {

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation; // Date de création du panier, non modifiable

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private User utilisateur; // L'utilisateur auquel le panier est associé



    @OneToMany(mappedBy = "panier", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PanierProduit> produits = new ArrayList<>();


    public List<PanierProduit> getProduits() {
        return produits;
    }

    public void setProduits(List<PanierProduit> produits) {
        this.produits = produits;
    }
}
