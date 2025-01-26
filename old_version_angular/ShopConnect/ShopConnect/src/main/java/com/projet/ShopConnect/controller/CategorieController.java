package com.projet.ShopConnect.controller;

import com.projet.ShopConnect.model.Categorie;
import com.projet.ShopConnect.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Contrôleur Spring REST gérant les requêtes HTTP liées aux catégories
@RestController
// Toutes les requêtes à ce contrôleur commenceront par "/api/categories"
@RequestMapping("/categories")
public class CategorieController {

    // Injection automatique du service CategorieService pour gérer la logique métier
    @Autowired
    private CategorieService categorieService;

    // Méthode pour récupérer toutes les catégories
    // @GetMapping : traite les requêtes GET sur "/api/categories"
    @GetMapping
    public List<Categorie> getAllCategories() {
        // Appelle la méthode du service pour renvoyer la liste des catégories
        return categorieService.getAllCategories();
    }

    // Méthode pour récupérer une catégorie en fonction de son ID
    // @GetMapping("/{id}") : traite les requêtes GET pour une catégorie spécifique (ex : /api/categories/1)
    @GetMapping("/{id}")
    public ResponseEntity<Categorie> getCategorieById(@PathVariable Long id) {
        // Recherche la catégorie par son ID via le service
        Categorie categorie = categorieService.getCategorieById(id);
        // Si la catégorie est trouvée, renvoie une réponse avec les détails, sinon 404 NOT FOUND
        if (categorie != null) {
            return ResponseEntity.ok(categorie);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Catégorie non trouvée
        }
    }

    // Méthode pour créer une nouvelle catégorie
    // @PostMapping : traite les requêtes POST pour ajouter une nouvelle catégorie
    @PostMapping
    public ResponseEntity<Categorie> createCategorie(@RequestBody Categorie categorie) {
        try {
            // Sauvegarde la catégorie via le service et renvoie une réponse 201 CREATED avec ses détails
            Categorie createdCategorie = categorieService.saveCategorie(categorie);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategorie);
        } catch (Exception e) {
            // Si une erreur survient (ex : validation), renvoie une réponse 400 BAD REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Erreur lors de la création
        }
    }

    // Méthode pour mettre à jour une catégorie existante
    // @PutMapping("/{id}") : traite les requêtes PUT pour mettre à jour une catégorie existante
    @PutMapping("/{id}")
    public ResponseEntity<Categorie> updateCategorie(@PathVariable Long id, @RequestBody Categorie updatedCategorie) {
        try {
            // Met à jour la catégorie via le service
            Categorie categorie = categorieService.updateCategorie(id, updatedCategorie);
            // Si la mise à jour réussit, renvoie une réponse 200 OK avec les détails mis à jour
            return ResponseEntity.ok(categorie);
        } catch (RuntimeException e) {
            // Si la catégorie n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Catégorie non trouvée
        }
    }

    // Méthode pour supprimer une catégorie en fonction de son ID
    // @DeleteMapping("/{id}") : traite les requêtes DELETE pour supprimer une catégorie spécifique
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategorie(@PathVariable Long id) {
        try {
            // Supprime la catégorie via le service et renvoie une réponse 204 NO CONTENT si la suppression réussit
            categorieService.deleteCategorie(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Si la catégorie n'existe pas, renvoie une réponse 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Catégorie non trouvée
        }
    }
}
