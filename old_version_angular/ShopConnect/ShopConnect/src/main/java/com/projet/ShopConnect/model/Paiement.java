package com.projet.ShopConnect.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Paiement extends Identified {

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant; // Montant du paiement, avec une précision pour les valeurs décimales

    @Column(nullable = false)
    private String methodePaiement; // Exemple : "Stripe", "PayPal"

    @Column(nullable = false)
    private String statutPaiement; // Exemple : "PAID", "PENDING", "FAILED"

    @Column(nullable = false)
    private LocalDateTime datePaiement; // Date et heure du paiement

    private String transactionId; // ID de transaction externe (exemple : Stripe, PayPal)

    @OneToOne
    @JoinColumn(name = "commande_id", nullable = false) // Clé étrangère pour lier la commande associée
    private Commande commande;


    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private User utilisateur;

    // Validation du montant
    public void setMontant(BigDecimal montant) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à zéro.");
        }
        this.montant = montant;
    }

    // Validation de la méthode de paiement
    public void setMethodePaiement(String methodePaiement) {
        if (methodePaiement == null || methodePaiement.isEmpty()) {
            throw new IllegalArgumentException("La méthode de paiement est obligatoire.");
        }
        this.methodePaiement = methodePaiement;
    }

    // Validation du statut de paiement
    public void setStatutPaiement(String statutPaiement) {
        if (statutPaiement == null || statutPaiement.isEmpty()) {
            throw new IllegalArgumentException("Le statut de paiement est obligatoire.");
        }
        this.statutPaiement = statutPaiement;
    }

    // Validation de la date de paiement
    public void setDatePaiement(LocalDateTime datePaiement) {
        if (datePaiement == null) {
            throw new IllegalArgumentException("La date de paiement est obligatoire.");
        }
        this.datePaiement = datePaiement;
    }


}
