package com.projet.ShopConnect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PromotionProduit extends Identified {

    @ManyToOne
    private Promotion promotion;

    @ManyToOne
    private Produit produit;
}