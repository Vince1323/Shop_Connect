package com.projet.ShopConnect.service;

import com.projet.ShopConnect.model.Paiement;
import com.projet.ShopConnect.repository.PaiementRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    @PostConstruct
    public void initStripe() {
        // Clé API Stripe à configurer
        Stripe.apiKey = "votre_cle_secrete_stripe";
    }

    // Récupérer tous les paiements
    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    // Récupérer un paiement par son ID
    public Paiement getPaiementById(Long id) {
        return paiementRepository.findById(id).orElse(null);
    }

    // Enregistrer un paiement
    public Paiement savePaiement(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    // Mettre à jour un paiement existant
    public Paiement updatePaiement(Long id, Paiement updatedPaiement) {
        return paiementRepository.findById(id)
                .map(paiement -> {
                    paiement.setMontant(updatedPaiement.getMontant());
                    paiement.setMethodePaiement(updatedPaiement.getMethodePaiement());
                    paiement.setStatutPaiement(updatedPaiement.getStatutPaiement());
                    paiement.setDatePaiement(updatedPaiement.getDatePaiement());
                    paiement.setTransactionId(updatedPaiement.getTransactionId());
                    return paiementRepository.save(paiement);
                }).orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
    }

    // Supprimer un paiement par son ID
    public void deletePaiement(Long id) {
        paiementRepository.deleteById(id);
    }

    // Créer un paiement avec Stripe
    public String creerPaiement(Double montant) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", (int) (montant * 100)); // Montant en centimes
        params.put("currency", "eur");
        params.put("payment_method_types", List.of("card"));

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent.getClientSecret(); // Renvoie le secret client pour finaliser le paiement côté frontend
    }

    // Vérifier l'état d'un paiement avec Stripe
    public String verifierStatutPaiement(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        return paymentIntent.getStatus(); // Ex: "succeeded", "pending", "failed"
    }
}
