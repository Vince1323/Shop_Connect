package com.projet.ShopConnect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Avis extends Identified {

    private String note;
    private String commentaire;
    private String dateCreation;

    // Relation ManyToOne vers Utilisateur : Plusieurs avis peuvent être donnés par un utilisateur
    @ManyToOne
    private User utilisateur;

    // Relation ManyToOne vers Produit : Plusieurs avis peuvent être associés à un produit
    @ManyToOne
    private Produit produit;
}