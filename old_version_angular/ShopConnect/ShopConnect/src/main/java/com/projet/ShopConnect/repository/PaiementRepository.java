package com.projet.ShopConnect.repository;

import com.projet.ShopConnect.model.Paiement;
import com.projet.ShopConnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByStatutPaiement(String statut); // Ex : "PAID", "PENDING"
    List<Paiement> findByUtilisateur_Id(Long utilisateurId); // Paiements associés à un utilisateur
    List<Paiement> findByUtilisateur(User utilisateur); // Paiements associés à un utilisateur


}
