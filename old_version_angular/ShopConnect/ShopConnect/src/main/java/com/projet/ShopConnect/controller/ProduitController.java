package com.projet.ShopConnect.controller;

import com.projet.ShopConnect.model.Produit;
import com.projet.ShopConnect.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Contrôleur Spring REST gérant les requêtes HTTP liées aux produits
@RestController
// Toutes les requêtes à ce contrôleur commenceront par "/api/produits"
@RequestMapping("/produits")
public class ProduitController {

    // Injection automatique du service ProduitService pour gérer la logique métier
    @Autowired
    private ProduitService produitService;

    // Méthode pour récupérer tous les produits
    // @GetMapping : traite les requêtes GET sur "/api/produits"
    @GetMapping
    public List<Produit> getAllProduits() {
        // Appelle la méthode du service pour renvoyer la liste des produits
        return produitService.getAllProduits();
    }

    // Méthode pour récupérer un produit en fonction de son ID
    // @GetMapping("/{id}") : traite les requêtes GET pour un produit spécifique (ex : /api/produits/1)
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        // Recherche le produit par son ID via le service
        Produit produit = produitService.getProduitById(id);
        // Si le produit est trouvé, renvoie une réponse avec les détails, sinon 404 NOT FOUND
        if (produit != null) {
            return ResponseEntity.ok(produit);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Produit non trouvé
        }
    }

    // Méthode pour créer un nouveau produit
    // @PostMapping : traite les requêtes POST pour ajouter un nouveau produit
    @PostMapping
    public ResponseEntity<Produit> createProduit(@RequestBody Produit produit) {
        try {
            // Sauvegarde le produit via le service et renvoie une réponse 201 CREATED avec ses détails
            Produit newProduit = produitService.saveProduit(produit);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduit);
        } catch (Exception e) {
            // Si une erreur survient (ex : validation), renvoie une réponse 400 BAD REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Erreur lors de la création
        }
    }

    // Méthode pour mettre à jour un produit existant
    // @PutMapping("/{id}") : traite les requêtes PUT pour mettre à jour un produit existant
    @PutMapping("/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable Long id, @RequestBody Produit updatedProduit) {
        try {
            // Met à jour le produit via le service
            Produit produit = produitService.updateProduit(id, updatedProduit);
            // Si la mise à jour réussit, renvoie une réponse 200 OK avec les détails mis à jour
            return ResponseEntity.ok(produit);
        } catch (RuntimeException e) {
            // Si le produit n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Produit non trouvé
        }
    }

    // Méthode pour supprimer un produit en fonction de son ID
    // @DeleteMapping("/{id}") : traite les requêtes DELETE pour supprimer un produit spécifique
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        try {
            // Supprime le produit via le service et renvoie une réponse 204 NO CONTENT si la suppression réussit
            produitService.deleteProduit(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Si le produit n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Produit non trouvé
        }
    }
}
