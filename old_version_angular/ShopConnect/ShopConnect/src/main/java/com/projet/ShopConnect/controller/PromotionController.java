package com.projet.ShopConnect.controller;

import com.projet.ShopConnect.model.Promotion;
import com.projet.ShopConnect.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Contrôleur Spring REST gérant les requêtes HTTP liées aux promotions
@RestController
// Toutes les requêtes à ce contrôleur commenceront par "/api/promotions"
@RequestMapping("/promotions")
public class PromotionController {

    // Injection automatique du service PromotionService pour gérer la logique métier
    @Autowired
    private PromotionService promotionService;

    // Méthode pour récupérer toutes les promotions
    // @GetMapping : traite les requêtes GET sur "/api/promotions"
    @GetMapping
    public List<Promotion> getAllPromotions() {
        // Appelle la méthode du service pour renvoyer la liste des promotions
        return promotionService.getAllPromotions();
    }

    // Méthode pour récupérer une promotion en fonction de son ID
    // @GetMapping("/{id}") : traite les requêtes GET pour une promotion spécifique (ex : /api/promotions/1)
    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable Long id) {
        // Recherche la promotion par son ID via le service
        Promotion promotion = promotionService.getPromotionById(id);
        // Si la promotion est trouvée, renvoie une réponse avec les détails, sinon 404 NOT FOUND
        if (promotion != null) {
            return ResponseEntity.ok(promotion);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Promotion non trouvée
        }
    }

    // Méthode pour créer une nouvelle promotion
    // @PostMapping : traite les requêtes POST pour ajouter une nouvelle promotion
    @PostMapping
    public ResponseEntity<Promotion> createPromotion(@RequestBody Promotion promotion) {
        try {
            // Sauvegarde la promotion via le service et renvoie une réponse 201 CREATED avec ses détails
            Promotion newPromotion = promotionService.savePromotion(promotion);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPromotion);
        } catch (Exception e) {
            // Si une erreur survient (ex : validation), renvoie une réponse 400 BAD REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Erreur lors de la création
        }
    }

    // Méthode pour mettre à jour une promotion existante
    // @PutMapping("/{id}") : traite les requêtes PUT pour mettre à jour une promotion existante
    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable Long id, @RequestBody Promotion updatedPromotion) {
        try {
            // Met à jour la promotion via le service
            Promotion promotion = promotionService.updatePromotion(id, updatedPromotion);
            // Si la mise à jour réussit, renvoie une réponse 200 OK avec les détails mis à jour
            return ResponseEntity.ok(promotion);
        } catch (RuntimeException e) {
            // Si la promotion n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Promotion non trouvée
        }
    }

    // Méthode pour supprimer une promotion en fonction de son ID
    // @DeleteMapping("/{id}") : traite les requêtes DELETE pour supprimer une promotion spécifique
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        try {
            // Supprime la promotion via le service et renvoie une réponse 204 NO CONTENT si la suppression réussit
            promotionService.deletePromotion(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Si la promotion n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Promotion non trouvée
        }
    }
}
