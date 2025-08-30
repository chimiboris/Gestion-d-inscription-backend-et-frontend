package com.inscription.app.web.rest.admin;

import com.inscription.app.domain.enumeration.StatutDossier;
import com.inscription.app.repository.DossierRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/admin")
public class AdminStatistiqueResource {

    private final DossierRepository dossierRepository;

    public AdminStatistiqueResource(DossierRepository dossierRepository) {
        this.dossierRepository = dossierRepository;
    }

    @GetMapping("/statistiques")
    public Map<String, Long> getStatistiques() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("EN_ATTENTE", dossierRepository.countByStatut(StatutDossier.EN_ATTENTE));
        stats.put("INCOMPLET", dossierRepository.countByStatut(StatutDossier.INCOMPLET));
        stats.put("VALIDE_AUTO", dossierRepository.countByStatut(StatutDossier.VALIDE_AUTO));
        stats.put("EN_COURS_VERIF", dossierRepository.countByStatut(StatutDossier.EN_COURS_VERIF));
        stats.put("VALIDE_MANUEL", dossierRepository.countByStatut(StatutDossier.VALIDE_MANUEL));
        stats.put("REFUSE", dossierRepository.countByStatut(StatutDossier.REFUSE));

        return stats;
    }
}
