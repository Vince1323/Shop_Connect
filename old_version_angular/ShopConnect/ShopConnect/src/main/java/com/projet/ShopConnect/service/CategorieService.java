package com.projet.ShopConnect.service;

import com.projet.ShopConnect.model.Categorie;
import com.projet.ShopConnect.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    // Récupérer toutes les catégories
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    // Récupérer une catégorie par son ID
    public Categorie getCategorieById(Long id) {
        return categorieRepository.findById(id).orElse(null);
    }

    // Enregistrer ou mettre à jour une catégorie
    public Categorie saveCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    // Supprimer une catégorie par son ID
    public void deleteCategorie(Long id) {
        categorieRepository.deleteById(id);
    }

    // Mettre à jour une catégorie existante
    public Categorie updateCategorie(Long id, Categorie updatedCategorie) {
        return categorieRepository.findById(id)
                .map(categorie -> {
                    categorie.setNom(updatedCategorie.getNom());
                    categorie.setDescription(updatedCategorie.getDescription());
                    return categorieRepository.save(categorie);
                })
                .orElseThrow(() -> new RuntimeException("Categorie non trouvée"));
    }
}
