package com.projet.ShopConnect.controller;

import com.projet.ShopConnect.model.Commande;
import com.projet.ShopConnect.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Contrôleur Spring REST gérant les requêtes HTTP liées aux commandes
@RestController
// Toutes les requêtes à ce contrôleur commenceront par "/api/commandes"
@RequestMapping("/commandes")
public class CommandeController {

    // Injection automatique du service CommandeService pour gérer la logique métier
    @Autowired
    private CommandeService commandeService;

    // Méthode pour récupérer toutes les commandes
    // @GetMapping : traite les requêtes GET sur "/api/commandes"
    @GetMapping
    public List<Commande> getAllCommandes() {
        // Appelle la méthode du service pour renvoyer la liste des commandes
        return commandeService.getAllCommandes();
    }

    // Méthode pour récupérer une commande en fonction de son ID
    // @GetMapping("/{id}") : traite les requêtes GET pour une commande spécifique (ex : /api/commandes/1)
    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Long id) {
        // Recherche la commande par son ID via le service
        Commande commande = commandeService.getCommandeById(id);
        // Si la commande est trouvée, renvoie une réponse avec les détails, sinon 404 NOT FOUND
        if (commande != null) {
            return ResponseEntity.ok(commande);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Commande non trouvée
        }
    }

    // Méthode pour créer une nouvelle commande
    // @PostMapping : traite les requêtes POST pour ajouter une nouvelle commande
    @PostMapping
    public ResponseEntity<Commande> createCommande(@RequestBody Commande commande) {
        try {
            // Sauvegarde la commande via le service et renvoie une réponse 201 CREATED avec ses détails
            Commande createdCommande = commandeService.saveCommande(commande);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCommande);
        } catch (Exception e) {
            // Si une erreur survient (ex : validation), renvoie une réponse 400 BAD REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Erreur lors de la création
        }
    }

    // Méthode pour mettre à jour une commande existante
    // @PutMapping("/{id}") : traite les requêtes PUT pour mettre à jour une commande existante
    @PutMapping("/{id}")
    public ResponseEntity<Commande> updateCommande(@PathVariable Long id, @RequestBody Commande updatedCommande) {
        try {
            // Met à jour la commande via le service
            Commande commande = commandeService.updateCommande(id, updatedCommande);
            // Si la mise à jour réussit, renvoie une réponse 200 OK avec les détails mis à jour
            return ResponseEntity.ok(commande);
        } catch (RuntimeException e) {
            // Si la commande n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Commande non trouvée
        }
    }

    // Méthode pour supprimer une commande en fonction de son ID
    // @DeleteMapping("/{id}") : traite les requêtes DELETE pour supprimer une commande spécifique
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        try {
            // Supprime la commande via le service et renvoie une réponse 204 NO CONTENT si la suppression réussit
            commandeService.deleteCommande(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Si la commande n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Commande non trouvée
        }
    }
}
