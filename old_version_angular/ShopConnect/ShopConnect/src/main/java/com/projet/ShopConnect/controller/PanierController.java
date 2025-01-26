package com.projet.ShopConnect.controller;

import com.projet.ShopConnect.model.Panier;
import com.projet.ShopConnect.service.PanierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Contrôleur Spring REST gérant les requêtes HTTP liées aux paniers
@RestController
// Toutes les requêtes à ce contrôleur commenceront par "/api/paniers"
@RequestMapping("/paniers")
public class PanierController {

    // Injection automatique du service PanierService pour gérer la logique métier
    @Autowired
    private PanierService panierService;

    // Méthode pour récupérer tous les paniers
    // @GetMapping : traite les requêtes GET sur "/api/paniers"
    @GetMapping
    public List<Panier> getAllPaniers() {
        // Appelle la méthode du service pour renvoyer la liste des paniers
        return panierService.getAllPaniers();
    }

    // Méthode pour récupérer un panier en fonction de son ID
    // @GetMapping("/{id}") : traite les requêtes GET pour un panier spécifique (ex : /api/paniers/1)
    @GetMapping("/{id}")
    public ResponseEntity<Panier> getPanierById(@PathVariable Long id) {
        // Recherche le panier par son ID via le service
        Panier panier = panierService.getPanierById(id);
        // Si le panier est trouvé, renvoie une réponse avec les détails, sinon 404 NOT FOUND
        if (panier != null) {
            return ResponseEntity.ok(panier);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Panier non trouvé
        }
    }

    // Méthode pour créer un nouveau panier
    // @PostMapping : traite les requêtes POST pour ajouter un nouveau panier
    @PostMapping
    public ResponseEntity<Panier> createPanier(@RequestBody Panier panier) {
        try {
            // Sauvegarde le panier via le service et renvoie une réponse 201 CREATED avec ses détails
            Panier newPanier = panierService.savePanier(panier);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPanier);
        } catch (Exception e) {
            // Si une erreur survient (ex : validation), renvoie une réponse 400 BAD REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Erreur lors de la création
        }
    }

    // Méthode pour mettre à jour un panier existant
    // @PutMapping("/{id}") : traite les requêtes PUT pour mettre à jour un panier existant
    @PutMapping("/{id}")
    public ResponseEntity<Panier> updatePanier(@PathVariable Long id, @RequestBody Panier updatedPanier) {
        try {
            // Met à jour le panier via le service
            Panier panier = panierService.updatePanier(id, updatedPanier);
            // Si la mise à jour réussit, renvoie une réponse 200 OK avec les détails mis à jour
            return ResponseEntity.ok(panier);
        } catch (RuntimeException e) {
            // Si le panier n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Panier non trouvé
        }
    }

    // Méthode pour supprimer un panier en fonction de son ID
    // @DeleteMapping("/{id}") : traite les requêtes DELETE pour supprimer un panier spécifique
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePanier(@PathVariable Long id) {
        try {
            // Supprime le panier via le service et renvoie une réponse 204 NO CONTENT si la suppression réussit
            panierService.deletePanier(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Si le panier n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Panier non trouvé
        }
    }
}
