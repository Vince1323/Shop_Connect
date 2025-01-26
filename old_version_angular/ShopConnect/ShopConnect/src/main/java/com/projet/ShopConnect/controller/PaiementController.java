package com.projet.ShopConnect.controller;

import com.projet.ShopConnect.model.Paiement;
import com.projet.ShopConnect.service.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Contrôleur Spring REST gérant les requêtes HTTP liées aux paiements
@RestController
// Toutes les requêtes à ce contrôleur commenceront par "/api/paiements"
@RequestMapping("/paiements")
public class PaiementController {

    // Injection automatique du service PaiementService pour gérer la logique métier
    @Autowired
    private PaiementService paiementService;

    // Méthode pour récupérer tous les paiements
    // @GetMapping : traite les requêtes GET sur "/api/paiements"
    @GetMapping
    public List<Paiement> getAllPaiements() {
        // Appelle la méthode du service pour renvoyer la liste des paiements
        return paiementService.getAllPaiements();
    }

    // Méthode pour récupérer un paiement en fonction de son ID
    // @GetMapping("/{id}") : traite les requêtes GET pour un paiement spécifique (ex : /api/paiements/1)
    @GetMapping("/{id}")
    public ResponseEntity<Paiement> getPaiementById(@PathVariable Long id) {
        // Recherche le paiement par son ID via le service
        Paiement paiement = paiementService.getPaiementById(id);
        // Si le paiement est trouvé, renvoie une réponse avec les détails, sinon 404 NOT FOUND
        if (paiement != null) {
            return ResponseEntity.ok(paiement);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Paiement non trouvé
        }
    }

    // Méthode pour créer un nouveau paiement
    // @PostMapping : traite les requêtes POST pour ajouter un nouveau paiement
    @PostMapping
    public ResponseEntity<Paiement> createPaiement(@RequestBody Paiement paiement) {
        try {
            // Sauvegarde le paiement via le service et renvoie une réponse 201 CREATED avec ses détails
            Paiement newPaiement = paiementService.savePaiement(paiement);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPaiement);
        } catch (Exception e) {
            // Si une erreur survient (ex : validation), renvoie une réponse 400 BAD REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Erreur lors de la création
        }
    }

    // Méthode pour mettre à jour un paiement existant
    // @PutMapping("/{id}") : traite les requêtes PUT pour mettre à jour un paiement existant
    @PutMapping("/{id}")
    public ResponseEntity<Paiement> updatePaiement(@PathVariable Long id, @RequestBody Paiement updatedPaiement) {
        try {
            // Met à jour le paiement via le service
            Paiement paiement = paiementService.updatePaiement(id, updatedPaiement);
            // Si la mise à jour réussit, renvoie une réponse 200 OK avec les détails mis à jour
            return ResponseEntity.ok(paiement);
        } catch (RuntimeException e) {
            // Si le paiement n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Paiement non trouvé
        }
    }

    // Méthode pour supprimer un paiement en fonction de son ID
    // @DeleteMapping("/{id}") : traite les requêtes DELETE pour supprimer un paiement spécifique
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaiement(@PathVariable Long id) {
        try {
            // Supprime le paiement via le service et renvoie une réponse 204 NO CONTENT si la suppression réussit
            paiementService.deletePaiement(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Si le paiement n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Paiement non trouvé
        }
    }
}
