package com.projet.ShopConnect.service;

import com.projet.ShopConnect.model.*;
import com.projet.ShopConnect.repository.CommandeRepository;
import com.projet.ShopConnect.repository.PanierRepository;
import com.projet.ShopConnect.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private PanierRepository panierRepository;

    @Autowired
    private ProduitRepository produitRepository;

    // Récupérer toutes les commandes
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    // Récupérer une commande par son ID
    public Commande getCommandeById(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
    }

    // Enregistrer ou mettre à jour une commande
    public Commande saveCommande(Commande commande) {
        if (commande.getProduits() == null || commande.getProduits().isEmpty()) {
            throw new RuntimeException("La commande doit contenir au moins un produit");
        }
        commande.setDateCommande(LocalDateTime.now());
        return commandeRepository.save(commande);
    }

    // Supprimer une commande par son ID
    public void deleteCommande(Long id) {
        if (!commandeRepository.existsById(id)) {
            throw new RuntimeException("Commande non trouvée");
        }
        commandeRepository.deleteById(id);
    }

    // Mettre à jour une commande existante
    public Commande updateCommande(Long id, Commande updatedCommande) {
        return commandeRepository.findById(id)
                .map(commande -> {
                    commande.setAdresseLivraison(updatedCommande.getAdresseLivraison());
                    commande.setStatut(updatedCommande.getStatut());
                    commande.setTotalMontant(updatedCommande.getTotalMontant());
                    return commandeRepository.save(commande);
                })
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
    }

    public Commande creerCommandeDepuisPanier(Long panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        if (panier.getProduits().isEmpty()) {
            throw new RuntimeException("Le panier est vide");
        }

        // Vérification des stocks
        for (PanierProduit panierProduit : panier.getProduits()) {
            Produit produit = panierProduit.getProduit();
            if (produit.getQuantiteStock() < panierProduit.getQuantite()) {
                throw new RuntimeException("Stock insuffisant pour le produit : " + produit.getNom());
            }
        }

        // Création de la commande
        Commande commande = new Commande();
        commande.setUtilisateur(panier.getUtilisateur());
        // commande.setAdresseLivraison(panier.getUtilisateur().getAddress()); 📌📌
        commande.setDateCommande(LocalDateTime.now());
        commande.setStatut("EN_COURS");

        // Calcul du montant total de la commande
        BigDecimal totalMontant = BigDecimal.ZERO;
        List<CommandeProduit> produitsCommande = new ArrayList<>();

        for (PanierProduit panierProduit : panier.getProduits()) {
            Produit produit = panierProduit.getProduit();

            // Création d'un CommandeProduit
            CommandeProduit commandeProduit = new CommandeProduit();
            commandeProduit.setProduit(produit);
            commandeProduit.setQuantite(panierProduit.getQuantite());
            commandeProduit.setPrixUnitaire(produit.getPrix());
            commandeProduit.setCommande(commande);

            produitsCommande.add(commandeProduit);

            // Mise à jour des stocks
            produit.setQuantiteStock(produit.getQuantiteStock() - panierProduit.getQuantite());
            produitRepository.save(produit);

            // Ajout au montant total
            totalMontant = totalMontant.add(produit.getPrix().multiply(BigDecimal.valueOf(panierProduit.getQuantite())));
        }

        commande.setProduits(produitsCommande);
        commande.setTotalMontant(totalMontant);

        // Sauvegarde de la commande
        commandeRepository.save(commande);

        // Vider le panier
        panier.getProduits().clear();
        panierRepository.save(panier);

        return commande;
    }


    // Récupérer les commandes par statut
    public List<Commande> getCommandesByStatut(String statut) {
        return commandeRepository.findByStatut(statut);
    }

    // Récupérer les commandes d'un utilisateur
    public List<Commande> getCommandesByUtilisateur(Long utilisateurId) {
        return commandeRepository.findByUtilisateurId(utilisateurId);
    }
}
