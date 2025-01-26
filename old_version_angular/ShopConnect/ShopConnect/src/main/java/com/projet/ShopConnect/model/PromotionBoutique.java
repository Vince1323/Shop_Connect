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
public class PromotionBoutique extends Identified {

    // Relation ManyToOne vers Promotion : Une promotion peut s'appliquer à plusieurs boutiques
    @ManyToOne
    private Promotion promotion;

    // Relation ManyToOne vers Boutique : Une boutique peut avoir plusieurs promotions
    @ManyToOne
    private Boutique boutique;
}