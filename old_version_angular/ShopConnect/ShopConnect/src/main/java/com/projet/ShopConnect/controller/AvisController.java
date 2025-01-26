package com.projet.ShopConnect.controller;

import com.projet.ShopConnect.model.Avis;
import com.projet.ShopConnect.service.AvisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Contrôleur Spring REST gérant les requêtes HTTP liées aux avis
@RestController
// Toutes les requêtes à ce contrôleur commenceront par "/api/avis"
@RequestMapping("/avis")
public class AvisController {

    // Injection automatique du service AvisService pour gérer la logique métier
    @Autowired
    private AvisService avisService;

    // Méthode pour récupérer tous les avis
    // @GetMapping : traite les requêtes GET sur "/api/avis"
    @GetMapping
    public List<Avis> getAllAvis() {
        // Appelle la méthode du service pour renvoyer la liste des avis
        return avisService.getAllAvis();
    }

    // Méthode pour récupérer un avis en fonction de son ID
    // @GetMapping("/{id}") : traite les requêtes GET pour un avis spécifique (ex : /api/avis/1)
    @GetMapping("/{id}")
    public ResponseEntity<Avis> getAvisById(@PathVariable Long id) {
        // Recherche l'avis par son ID via le service
        Avis avis = avisService.getAvisById(id);
        // Si l'avis est trouvé, renvoie une réponse avec les détails, sinon 404 NOT FOUND
        if (avis != null) {
            return ResponseEntity.ok(avis);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Avis non trouvé
        }
    }

    // Méthode pour créer un nouvel avis
    // @PostMapping : traite les requêtes POST pour ajouter un nouvel avis
    @PostMapping
    public ResponseEntity<Avis> createAvis(@RequestBody Avis avis) {
        try {
            // Sauvegarde l'avis via le service et renvoie une réponse 201 CREATED avec ses détails
            Avis createdAvis = avisService.saveAvis(avis);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAvis);
        } catch (Exception e) {
            // Si une erreur survient (ex : validation), renvoie une réponse 400 BAD REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Erreur lors de la création
        }
    }

    // Méthode pour mettre à jour un avis existant
    // @PutMapping("/{id}") : traite les requêtes PUT pour mettre à jour un avis existant
    @PutMapping("/{id}")
    public ResponseEntity<Avis> updateAvis(@PathVariable Long id, @RequestBody Avis updatedAvis) {
        try {
            // Met à jour l'avis via le service
            Avis avis = avisService.updateAvis(id, updatedAvis);
            // Si la mise à jour réussit, renvoie une réponse 200 OK avec les détails mis à jour
            return ResponseEntity.ok(avis);
        } catch (RuntimeException e) {
            // Si l'avis n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Avis non trouvé
        }
    }

    // Méthode pour supprimer un avis en fonction de son ID
    // @DeleteMapping("/{id}") : traite les requêtes DELETE pour supprimer un avis spécifique
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvis(@PathVariable Long id) {
        try {
            // Supprime l'avis via le service et renvoie une réponse 204 NO CONTENT si la suppression réussit
            avisService.deleteAvis(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Si l'avis n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Avis non trouvé
        }
    }
}
