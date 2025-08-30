package com.inscription.app.service.dto;

import com.inscription.app.domain.enumeration.StatutDossier;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.inscription.app.domain.Dossier} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DossierDTO implements Serializable {

    private Long id;

    @NotNull
    private StatutDossier statut;

    @NotNull
    private Instant dateSoumission;

    private String commentaire;

    private CandidatDTO candidat;

    private UserDTO agent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatutDossier getStatut() {
        return statut;
    }

    public void setStatut(StatutDossier statut) {
        this.statut = statut;
    }

    public Instant getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(Instant dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public CandidatDTO getCandidat() {
        return candidat;
    }

    public void setCandidat(CandidatDTO candidat) {
        this.candidat = candidat;
    }

    public UserDTO getAgent() {
        return agent;
    }

    public void setAgent(UserDTO agent) {
        this.agent = agent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DossierDTO)) {
            return false;
        }

        DossierDTO dossierDTO = (DossierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dossierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DossierDTO{" +
            "id=" + getId() +
            ", statut='" + getStatut() + "'" +
            ", dateSoumission='" + getDateSoumission() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", candidat=" + getCandidat() +
            ", agent=" + getAgent() +
            "}";
    }
}
