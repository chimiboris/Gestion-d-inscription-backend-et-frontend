package com.inscription.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inscription.app.domain.enumeration.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A PieceJustificative.
 */
@Entity
@Table(name = "piece_justificative")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PieceJustificative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Lob
    @Column(name = "fichier", nullable = false)
    private byte[] fichier;

    @NotNull
    @Column(name = "fichier_content_type", nullable = false)
    private String fichierContentType;

    @Column(name = "date_upload")
    private Instant dateUpload;

    @Column(name = "valide")
    private Boolean valide;

    @Column(name = "commentaire")
    private String commentaire;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "piecejustificatives", "parcours", "contacturgence", "n" }, allowSetters = true)
    private Candidat candidat;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PieceJustificative id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return this.type;
    }

    public PieceJustificative type(Type type) {
        this.setType(type);
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public byte[] getFichier() {
        return this.fichier;
    }

    public PieceJustificative fichier(byte[] fichier) {
        this.setFichier(fichier);
        return this;
    }

    public void setFichier(byte[] fichier) {
        this.fichier = fichier;
    }

    public String getFichierContentType() {
        return this.fichierContentType;
    }

    public PieceJustificative fichierContentType(String fichierContentType) {
        this.fichierContentType = fichierContentType;
        return this;
    }

    public void setFichierContentType(String fichierContentType) {
        this.fichierContentType = fichierContentType;
    }

    public Instant getDateUpload() {
        return this.dateUpload;
    }

    public PieceJustificative dateUpload(Instant dateUpload) {
        this.setDateUpload(dateUpload);
        return this;
    }

    public void setDateUpload(Instant dateUpload) {
        this.dateUpload = dateUpload;
    }

    public Boolean getValide() {
        return this.valide;
    }

    public PieceJustificative valide(Boolean valide) {
        this.setValide(valide);
        return this;
    }

    public void setValide(Boolean valide) {
        this.valide = valide;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public PieceJustificative commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Candidat getCandidat() {
        return this.candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    public PieceJustificative candidat(Candidat candidat) {
        this.setCandidat(candidat);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PieceJustificative)) {
            return false;
        }
        return getId() != null && getId().equals(((PieceJustificative) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PieceJustificative{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", fichier='" + getFichier() + "'" +
            ", fichierContentType='" + getFichierContentType() + "'" +
            ", dateUpload='" + getDateUpload() + "'" +
            ", valide='" + getValide() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            "}";
    }
}
