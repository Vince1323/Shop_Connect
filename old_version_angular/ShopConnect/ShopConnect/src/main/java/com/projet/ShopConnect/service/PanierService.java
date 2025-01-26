package com.projet.ShopConnect.service;

import com.projet.ShopConnect.model.*;
import com.projet.ShopConnect.repository.PanierRepository;
import com.projet.ShopConnect.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PanierService {

    @Autowired
    private PanierRepository panierRepository;

    @Autowired
    private ProduitRepository produitRepository;

    // Récupérer tous les paniers
    public List<Panier> getAllPaniers() {
        return panierRepository.findAll();
    }

    // Récupérer un panier par son ID
    public Panier getPanierById(Long id) {
        return panierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));
    }

    // Récupérer le panier d'un utilisateur
    public Panier getPanierParUtilisateur(Long utilisateurId) {
        return panierRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Panier pour cet utilisateur non trouvé"));
    }

    // Enregistrer un panier
    public Panier savePanier(Panier panier) {
        return panierRepository.save(panier);
    }

    // Mettre à jour un panier existant
    public Panier updatePanier(Long id, Panier updatedPanier) {
        return panierRepository.findById(id)
                .map(panier -> {
                    panier.setDateCreation(updatedPanier.getDateCreation());
                    panier.setUtilisateur(updatedPanier.getUtilisateur());
                    return panierRepository.save(panier);
                })
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));
    }

    // Supprimer un panier par son ID
    public void deletePanier(Long id) {
        panierRepository.deleteById(id);
    }

    // Ajouter un produit au panier
    public Panier ajouterProduitAuPanier(Long panierId, Long produitId, int quantite) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        if (produit.getQuantiteStock() < quantite) {
            throw new RuntimeException("Stock insuffisant pour le produit : " + produit.getNom());
        }

        PanierProduit existant = panier.getProduits().stream()
                .filter(panierProduit -> panierProduit.getProduit().getId().equals(produitId))
                .findFirst()
                .orElse(null);

        if (existant != null) {
            existant.setQuantite(existant.getQuantite() + quantite);
        } else {
            PanierProduit panierProduit = new PanierProduit();
            panierProduit.setProduit(produit);
            panierProduit.setPanier(panier);
            panierProduit.setQuantite(quantite);
            panier.getProduits().add(panierProduit);
        }

        produit.setQuantiteStock(produit.getQuantiteStock() - quantite);
        produitRepository.save(produit);

        return panierRepository.save(panier);
    }



    // Supprimer un produit du panier
    public Panier supprimerProduitDuPanier(Long panierId, Long produitId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        panier.getProduits().removeIf(panierProduit -> panierProduit.getProduit().getId().equals(produitId));
        return panierRepository.save(panier);
    }

    // Vider le panier
    public Panier viderPanier(Long panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        panier.getProduits().clear();
        return panierRepository.save(panier);
    }
}
