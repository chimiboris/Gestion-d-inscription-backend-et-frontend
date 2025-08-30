package com.inscription.app.service.dto;

import com.inscription.app.domain.enumeration.Type;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.inscription.app.domain.PieceJustificative} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PieceJustificativeDTO implements Serializable {

    private Long id;

    @NotNull
    private Type type;

    @Lob
    private byte[] fichier;

    private String fichierContentType;
    private Instant dateUpload;

    private Boolean valide;

    private String commentaire;

    private CandidatDTO candidat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public byte[] getFichier() {
        return fichier;
    }

    public void setFichier(byte[] fichier) {
        this.fichier = fichier;
    }

    public String getFichierContentType() {
        return fichierContentType;
    }

    public void setFichierContentType(String fichierContentType) {
        this.fichierContentType = fichierContentType;
    }

    public Instant getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(Instant dateUpload) {
        this.dateUpload = dateUpload;
    }

    public Boolean getValide() {
        return valide;
    }

    public void setValide(Boolean valide) {
        this.valide = valide;
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
        if (!(o instanceof PieceJustificativeDTO)) {
            return false;
        }

        PieceJustificativeDTO pieceJustificativeDTO = (PieceJustificativeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pieceJustificativeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PieceJustificativeDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", fichier='" + getFichier() + "'" +
            ", dateUpload='" + getDateUpload() + "'" +
            ", valide='" + getValide() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", candidat=" + getCandidat() +
            "}";
    }
}
