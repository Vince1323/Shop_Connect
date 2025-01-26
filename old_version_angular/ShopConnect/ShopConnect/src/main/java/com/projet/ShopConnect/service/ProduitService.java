package com.projet.ShopConnect.service;

import com.projet.ShopConnect.model.Produit;
import com.projet.ShopConnect.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;

    // Récupérer tous les produits
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    // Récupérer un produit par son ID
    public Produit getProduitById(Long id) {
        return produitRepository.findById(id).orElse(null);
    }

    // Enregistrer un produit
    public Produit saveProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    // Mettre à jour un produit existant
    public Produit updateProduit(Long id, Produit updatedProduit) {
        return produitRepository.findById(id)
                .map(produit -> {
                    produit.setNom(updatedProduit.getNom());
                    produit.setDescription(updatedProduit.getDescription());
                    produit.setPrix(updatedProduit.getPrix());
                    produit.setQuantiteStock(updatedProduit.getQuantiteStock());
                    produit.setImageUrl(updatedProduit.getImageUrl());
                    produit.setCategorie(updatedProduit.getCategorie());
                    return produitRepository.save(produit);
                }).orElse(null);
    }

    // Supprimer un produit par son ID
    public void deleteProduit(Long id) {
        if (!produitRepository.existsById(id)) {
            throw new RuntimeException("Le produit avec l'ID " + id + " n'existe pas.");
        }
        produitRepository.deleteById(id);
    }

}
