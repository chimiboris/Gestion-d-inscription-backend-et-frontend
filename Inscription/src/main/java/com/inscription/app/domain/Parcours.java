package com.inscription.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inscription.app.domain.enumeration.NiveauEtude;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Parcours.
 */
@Entity
@Table(name = "parcours")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Parcours implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "etablissement", nullable = false)
    private String etablissement;

    @NotNull
    @Column(name = "specialisation", nullable = false)
    private String specialisation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "niveau", nullable = false)
    private NiveauEtude niveau;

    @Column(name = "commentaire")
    private String commentaire;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private Instant dateDebut;

    @NotNull
    @Column(name = "date_fin", nullable = false)
    private Instant dateFin;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "piecejustificatives", "parcours", "contacturgence", "n" }, allowSetters = true)
    private Candidat candidat;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Parcours id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtablissement() {
        return this.etablissement;
    }

    public Parcours etablissement(String etablissement) {
        this.setEtablissement(etablissement);
        return this;
    }

    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
    }

    public String getSpecialisation() {
        return this.specialisation;
    }

    public Parcours specialisation(String specialisation) {
        this.setSpecialisation(specialisation);
        return this;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public NiveauEtude getNiveau() {
        return this.niveau;
    }

    public Parcours niveau(NiveauEtude niveau) {
        this.setNiveau(niveau);
        return this;
    }

    public void setNiveau(NiveauEtude niveau) {
        this.niveau = niveau;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Parcours commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Instant getDateDebut() {
        return this.dateDebut;
    }

    public Parcours dateDebut(Instant dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return this.dateFin;
    }

    public Parcours dateFin(Instant dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public Candidat getCandidat() {
        return this.candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    public Parcours candidat(Candidat candidat) {
        this.setCandidat(candidat);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parcours)) {
            return false;
        }
        return getId() != null && getId().equals(((Parcours) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parcours{" +
            "id=" + getId() +
            ", etablissement='" + getEtablissement() + "'" +
            ", specialisation='" + getSpecialisation() + "'" +
            ", niveau='" + getNiveau() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
