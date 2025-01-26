package com.projet.ShopConnect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Commande extends Identified {

    private String adresseLivraison;

    @Column(nullable = false)
    private BigDecimal totalMontant;

    private String statut; // Exemple : "EN_COURS", "LIVREE", "ANNULEE"

    private LocalDateTime dateCommande;

    @ManyToOne
    private User utilisateur;

    @OneToMany(mappedBy = "commande", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandeProduit> produits;

    @OneToOne(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private Paiement paiement;
    // Setter pour la liste des produits
    public void setProduits(List<CommandeProduit> produits) {
        // Supprime les produits existants
        this.produits.clear();
        // Ajoute les nouveaux produits
        if (produits != null) {
            produits.forEach(produit -> produit.setCommande(this));
            this.produits.addAll(produits);
        }
    }
}
