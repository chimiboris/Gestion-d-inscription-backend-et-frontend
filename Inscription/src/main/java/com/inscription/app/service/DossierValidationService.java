package com.inscription.app.service;

import com.inscription.app.domain.Dossier;
import com.inscription.app.domain.PieceJustificative;
import com.inscription.app.domain.enumeration.StatutDossier;
import com.inscription.app.domain.enumeration.Type;
import com.inscription.app.repository.DossierRepository;
import com.inscription.app.repository.PieceJustificativeRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DossierValidationService {

    private final DossierRepository dossierRepository;
    private final PieceJustificativeRepository pieceJustificativeRepository;

    public DossierValidationService(
        DossierRepository dossierRepository,
        PieceJustificativeRepository pieceJustificativeRepository
    ) {
        this.dossierRepository = dossierRepository;
        this.pieceJustificativeRepository = pieceJustificativeRepository;
    }

    public void validerAutomatiquement(Dossier dossier) {
        List<PieceJustificative> pieces = pieceJustificativeRepository.findByCandidatId(dossier.getCandidat().getId());

        List<Type> requises = List.of(
            Type.CNI_RECTO,
            Type.CNI_VERSO,
            Type.ACTENAISSANCE,
            Type.DIPLOME,
            Type.PASSEPORT
        );

        boolean complet = requises.stream()
            .allMatch(type -> pieces.stream().anyMatch(p -> p.getType() == type));

        if (complet) {
            dossier.setStatut(StatutDossier.VALIDE_AUTO);
            dossier.setCommentaire("Toutes les pièces ont été fournies. Dossier validé automatiquement.");
        } else {
            dossier.setStatut(StatutDossier.INCOMPLET);
            dossier.setCommentaire("Une ou plusieurs pièces sont manquantes.");
        }

        dossierRepository.save(dossier);
    }
}

