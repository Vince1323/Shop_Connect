package com.projet.ShopConnect.service;

import com.projet.ShopConnect.model.Boutique;
import com.projet.ShopConnect.repository.BoutiqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoutiqueService {

    @Autowired
    private BoutiqueRepository boutiqueRepository;

    // Récupérer toutes les boutiques
    public List<Boutique> getAllBoutiques() {
        return boutiqueRepository.findAll();
    }

    // Récupérer une boutique par son ID
    public Boutique getBoutiqueById(Long id) {
        return boutiqueRepository.findById(id).orElse(null);
    }

    // Enregistrer ou mettre à jour une boutique
    public Boutique saveBoutique(Boutique boutique) {
        return boutiqueRepository.save(boutique);
    }

    // Supprimer une boutique par son ID
    public void deleteBoutique(Long id) {
        boutiqueRepository.deleteById(id);
    }

    // Mettre à jour une boutique existante
    public Boutique updateBoutique(Long id, Boutique updatedBoutique) {
        return boutiqueRepository.findById(id)
                .map(boutique -> {
                    // Mise à jour des champs de la boutique
                    boutique.setNom(updatedBoutique.getNom());
                    boutique.setDescription(updatedBoutique.getDescription());
                    boutique.setAdresse(updatedBoutique.getAdresse());
                    boutique.setUrlSlug(updatedBoutique.getUrlSlug());
                    boutique.setTelephone(updatedBoutique.getTelephone());
                    return boutiqueRepository.save(boutique);
                })
                .orElseThrow(() -> new RuntimeException("Boutique non trouvée"));
    }
}
