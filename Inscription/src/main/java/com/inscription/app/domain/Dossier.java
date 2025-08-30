package com.inscription.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inscription.app.domain.enumeration.StatutDossier;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Dossier.
 */
@Entity
@Table(name = "dossier")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dossier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutDossier statut;

    @NotNull
    @Column(name = "date_soumission", nullable = false)
    private Instant dateSoumission;

    @Column(name = "commentaire")
    private String commentaire;

    @JsonIgnoreProperties(value = { "user", "piecejustificatives", "parcours", "contacturgence", "n" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @MapsId
    @JoinColumn(name = "id")
    private Candidat candidat;

    @ManyToOne(fetch = FetchType.LAZY)
    private User agent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dossier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatutDossier getStatut() {
        return this.statut;
    }

    public Dossier statut(StatutDossier statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutDossier statut) {
        this.statut = statut;
    }

    public Instant getDateSoumission() {
        return this.dateSoumission;
    }

    public Dossier dateSoumission(Instant dateSoumission) {
        this.setDateSoumission(dateSoumission);
        return this;
    }

    public void setDateSoumission(Instant dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Dossier commentaire(String commentaire) {
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

    public Dossier candidat(Candidat candidat) {
        this.setCandidat(candidat);
        return this;
    }

    public User getAgent() {
        return this.agent;
    }

    public void setAgent(User user) {
        this.agent = user;
    }

    public Dossier agent(User user) {
        this.setAgent(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dossier)) {
            return false;
        }
        return getId() != null && getId().equals(((Dossier) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dossier{" +
            "id=" + getId() +
            ", statut='" + getStatut() + "'" +
            ", dateSoumission='" + getDateSoumission() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            "}";
    }
}
