package com.inscription.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inscription.app.domain.enumeration.statutRdv;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A RendezVous.
 */
@Entity
@Table(name = "rendez_vous")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RendezVous implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_rdv", nullable = false)
    private Instant dateRdv;

    @NotNull
    @Column(name = "motif", nullable = false)
    private String motif;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private statutRdv statut;

    @Column(name = "commentaire")
    private String commentaire;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "piecejustificatives", "parcours", "contacturgence", "n", "rendezVous" }, allowSetters = true)
    private Candidat candidat;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RendezVous id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateRdv() {
        return this.dateRdv;
    }

    public RendezVous dateRdv(Instant dateRdv) {
        this.setDateRdv(dateRdv);
        return this;
    }

    public void setDateRdv(Instant dateRdv) {
        this.dateRdv = dateRdv;
    }

    public String getMotif() {
        return this.motif;
    }

    public RendezVous motif(String motif) {
        this.setMotif(motif);
        return this;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public statutRdv getStatut() {
        return this.statut;
    }

    public RendezVous statut(statutRdv statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(statutRdv statut) {
        this.statut = statut;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public RendezVous commentaire(String commentaire) {
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

    public RendezVous candidat(Candidat candidat) {
        this.setCandidat(candidat);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RendezVous)) {
            return false;
        }
        return getId() != null && getId().equals(((RendezVous) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RendezVous{" +
            "id=" + getId() +
            ", dateRdv='" + getDateRdv() + "'" +
            ", motif='" + getMotif() + "'" +
            ", statut='" + getStatut() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            "}";
    }
}
