package com.projet.ShopConnect.repository;

import com.projet.ShopConnect.model.Panier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PanierRepository extends JpaRepository<Panier, Long> {
    Optional<Panier> findByUtilisateurId(Long utilisateurId); // Récupérer le panier d'un utilisateur
}
