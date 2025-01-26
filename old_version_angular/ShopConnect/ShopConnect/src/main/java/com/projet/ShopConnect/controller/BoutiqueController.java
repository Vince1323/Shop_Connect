package com.projet.ShopConnect.controller;

import com.projet.ShopConnect.model.Boutique;
import com.projet.ShopConnect.service.BoutiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Contrôleur Spring REST gérant les requêtes HTTP liées aux boutiques
@RestController
// Toutes les requêtes à ce contrôleur commenceront par "/api/boutiques"
@RequestMapping("/boutiques")
public class BoutiqueController {

    // Injection automatique du service BoutiqueService pour gérer la logique métier
    @Autowired
    private BoutiqueService boutiqueService;

    // Méthode pour récupérer toutes les boutiques
    // @GetMapping : traite les requêtes GET sur "/api/boutiques"
    @GetMapping
    public List<Boutique> getAllBoutiques() {
        // Appelle la méthode du service pour renvoyer la liste des boutiques
        return boutiqueService.getAllBoutiques();
    }

    // Méthode pour récupérer une boutique en fonction de son ID
    // @GetMapping("/{id}") : traite les requêtes GET pour une boutique spécifique (ex : /api/boutiques/1)
    @GetMapping("/{id}")
    public ResponseEntity<Boutique> getBoutiqueById(@PathVariable Long id) {
        // Recherche la boutique par son ID via le service
        Boutique boutique = boutiqueService.getBoutiqueById(id);
        // Si la boutique est trouvée, renvoie une réponse avec les détails, sinon 404 NOT FOUND
        if (boutique != null) {
            return ResponseEntity.ok(boutique);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Boutique non trouvée
        }
    }

    // Méthode pour créer une nouvelle boutique
    // @PostMapping : traite les requêtes POST pour ajouter une nouvelle boutique
    @PostMapping
    public ResponseEntity<Boutique> createBoutique(@RequestBody Boutique boutique) {
        try {
            // Sauvegarde la boutique via le service et renvoie une réponse 201 CREATED avec ses détails
            Boutique createdBoutique = boutiqueService.saveBoutique(boutique);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBoutique);
        } catch (Exception e) {
            // Si une erreur survient (ex : validation), renvoie une réponse 400 BAD REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Erreur lors de la création
        }
    }

    // Méthode pour mettre à jour une boutique existante
    // @PutMapping("/{id}") : traite les requêtes PUT pour mettre à jour une boutique existante
    @PutMapping("/{id}")
    public ResponseEntity<Boutique> updateBoutique(@PathVariable Long id, @RequestBody Boutique updatedBoutique) {
        try {
            // Met à jour la boutique via le service
            Boutique boutique = boutiqueService.updateBoutique(id, updatedBoutique);
            // Si la mise à jour réussit, renvoie une réponse 200 OK avec les détails mis à jour
            return ResponseEntity.ok(boutique);
        } catch (RuntimeException e) {
            // Si la boutique n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Boutique non trouvée
        }
    }

    // Méthode pour supprimer une boutique en fonction de son ID
    // @DeleteMapping("/{id}") : traite les requêtes DELETE pour supprimer une boutique spécifique
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoutique(@PathVariable Long id) {
        try {
            // Supprime la boutique via le service et renvoie une réponse 204 NO CONTENT si la suppression réussit
            boutiqueService.deleteBoutique(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Si la boutique n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Boutique non trouvée
        }
    }
}
