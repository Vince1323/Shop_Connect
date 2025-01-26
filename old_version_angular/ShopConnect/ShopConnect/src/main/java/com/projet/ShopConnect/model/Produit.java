package com.projet.ShopConnect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
public class Produit extends Identified {

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, length = 500)
    private String description; // Description du produit avec une limite de 500 caractères

    @Column(nullable = false)
    private int quantiteStock; // Stock actuel du produit

    private String imageUrl; // URL de l'image du produit

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix; // Prix du produit (exemple : 10.99)

    @Column(unique = true)
    private String urlSlug; // URL simplifiée pour les produits

    // Relation avec la catégorie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    // Promotions associées au produit
    @OneToMany(mappedBy = "produit", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionProduit> promotions;

    // Commandes contenant ce produit
    @OneToMany(mappedBy = "produit", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandeProduit> commandes;

    // Paniers contenant ce produit
    @OneToMany(mappedBy = "produit", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PanierProduit> paniers;

    // Avis des utilisateurs sur ce produit
    @OneToMany(mappedBy = "produit", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avis> avis;

    // Setter pour quantiteStock avec validation
    public void setQuantiteStock(int quantiteStock) {
        if (quantiteStock < 0) {
            throw new IllegalArgumentException("La quantité en stock ne peut pas être négative.");
        }
        this.quantiteStock = quantiteStock;
    }

    // Setter pour prix avec validation
    public void setPrix(BigDecimal prix) {
        if (prix == null || prix.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le prix doit être supérieur à zéro.");
        }
        this.prix = prix;
    }
}
