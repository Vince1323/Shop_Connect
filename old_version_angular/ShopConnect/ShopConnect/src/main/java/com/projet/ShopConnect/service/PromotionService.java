package com.projet.ShopConnect.service;

import com.projet.ShopConnect.model.Promotion;
import com.projet.ShopConnect.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    // Récupérer toutes les promotions
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    // Récupérer une promotion par son ID
    public Promotion getPromotionById(Long id) {
        return promotionRepository.findById(id).orElse(null);
    }

    // Créer une nouvelle promotion
    public Promotion savePromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    // Mettre à jour une promotion existante
    public Promotion updatePromotion(Long id, Promotion updatedPromotion) {
        return promotionRepository.findById(id)
                .map(promotion -> {
                    promotion.setCode(updatedPromotion.getCode());
                    promotion.setDescription(updatedPromotion.getDescription());
                    promotion.setDateDebut(updatedPromotion.getDateDebut());
                    promotion.setDateFin(updatedPromotion.getDateFin());
                    promotion.setValeurRemise(updatedPromotion.getValeurRemise());
                    promotion.setTypeRemise(updatedPromotion.getTypeRemise());
                    return promotionRepository.save(promotion);
                }).orElse(null);
    }

    // Supprimer une promotion par son ID
    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }
}
