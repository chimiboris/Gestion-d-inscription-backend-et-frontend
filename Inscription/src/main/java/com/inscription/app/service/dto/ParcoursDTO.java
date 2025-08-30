package com.inscription.app.service.dto;

import com.inscription.app.domain.enumeration.NiveauEtude;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.inscription.app.domain.Parcours} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParcoursDTO implements Serializable {

    private Long id;

    @NotNull
    private String etablissement;

    @NotNull
    private String specialisation;

    @NotNull
    private NiveauEtude niveau;

    private String commentaire;

    @NotNull
    private Instant dateDebut;

    @NotNull
    private Instant dateFin;

    private CandidatDTO candidat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public NiveauEtude getNiveau() {
        return niveau;
    }

    public void setNiveau(NiveauEtude niveau) {
        this.niveau = niveau;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return dateFin;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
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
        if (!(o instanceof ParcoursDTO)) {
            return false;
        }

        ParcoursDTO parcoursDTO = (ParcoursDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, parcoursDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParcoursDTO{" +
            "id=" + getId() +
            ", etablissement='" + getEtablissement() + "'" +
            ", specialisation='" + getSpecialisation() + "'" +
            ", niveau='" + getNiveau() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", candidat=" + getCandidat() +
            "}";
    }
}
