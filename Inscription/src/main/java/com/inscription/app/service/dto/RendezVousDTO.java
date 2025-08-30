package com.inscription.app.service.dto;

import com.inscription.app.domain.enumeration.statutRdv;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.inscription.app.domain.RendezVous} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RendezVousDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dateRdv;

    @NotNull
    private String motif;

    private statutRdv statut;

    private String commentaire;

    private CandidatDTO candidat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateRdv() {
        return dateRdv;
    }

    public void setDateRdv(Instant dateRdv) {
        this.dateRdv = dateRdv;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public statutRdv getStatut() {
        return statut;
    }

    public void setStatut(statutRdv statut) {
        this.statut = statut;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RendezVousDTO)) {
            return false;
        }

        RendezVousDTO rendezVousDTO = (RendezVousDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rendezVousDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RendezVousDTO{" +
            "id=" + getId() +
            ", dateRdv='" + getDateRdv() + "'" +
            ", motif='" + getMotif() + "'" +
            ", statut='" + getStatut() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", candidat=" + getCandidat() +
            "}";
    }
}
