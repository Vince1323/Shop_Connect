package com.projet.ShopConnect.repository;

import com.projet.ShopConnect.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
        List<Commande> findByUtilisateurId(Long utilisateurId); // Trouver toutes les commandes d'un utilisateur
        List<Commande> findByStatut(String statut); // Trouver toutes les commandes avec un statut spécifique
    }

