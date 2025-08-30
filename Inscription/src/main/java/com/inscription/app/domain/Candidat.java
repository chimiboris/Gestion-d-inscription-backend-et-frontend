package com.inscription.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inscription.app.domain.enumeration.Sexe;
import com.inscription.app.domain.enumeration.TypePieceIdentite;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Candidat.
 */
@Entity
@Table(name = "candidat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Candidat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @NotNull
    @Column(name = "nationalite", nullable = false)
    private String nationalite;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_piece_identite", nullable = false)
    private TypePieceIdentite typePieceIdentite;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "date_naiss", nullable = false)
    private Instant dateNaiss;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidat")
    @JsonIgnoreProperties(value = { "candidat" }, allowSetters = true)
    private Set<PieceJustificative> piecejustificatives = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidat")
    @JsonIgnoreProperties(value = { "candidat" }, allowSetters = true)
    private Set<Parcours> parcours = new HashSet<>();

    @JsonIgnoreProperties(value = { "candidat" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "candidat")
    private ContactUrgence contacturgence;

    @JsonIgnoreProperties(value = { "candidat", "agent" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "candidat")
    private Dossier n;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidat")
    @JsonIgnoreProperties(value = { "candidat" }, allowSetters = true)
    private Set<RendezVous> rendezVous = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Candidat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Candidat nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Candidat prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Sexe getSexe() {
        return this.sexe;
    }

    public Candidat sexe(Sexe sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getNationalite() {
        return this.nationalite;
    }

    public Candidat nationalite(String nationalite) {
        this.setNationalite(nationalite);
        return this;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public TypePieceIdentite getTypePieceIdentite() {
        return this.typePieceIdentite;
    }

    public Candidat typePieceIdentite(TypePieceIdentite typePieceIdentite) {
        this.setTypePieceIdentite(typePieceIdentite);
        return this;
    }

    public void setTypePieceIdentite(TypePieceIdentite typePieceIdentite) {
        this.typePieceIdentite = typePieceIdentite;
    }

    public String getEmail() {
        return this.email;
    }

    public Candidat email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getDateNaiss() {
        return this.dateNaiss;
    }

    public Candidat dateNaiss(Instant dateNaiss) {
        this.setDateNaiss(dateNaiss);
        return this;
    }

    public void setDateNaiss(Instant dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Candidat user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<PieceJustificative> getPiecejustificatives() {
        return this.piecejustificatives;
    }

    public void setPiecejustificatives(Set<PieceJustificative> pieceJustificatives) {
        if (this.piecejustificatives != null) {
            this.piecejustificatives.forEach(i -> i.setCandidat(null));
        }
        if (pieceJustificatives != null) {
            pieceJustificatives.forEach(i -> i.setCandidat(this));
        }
        this.piecejustificatives = pieceJustificatives;
    }

    public Candidat piecejustificatives(Set<PieceJustificative> pieceJustificatives) {
        this.setPiecejustificatives(pieceJustificatives);
        return this;
    }

    public Candidat addPiecejustificative(PieceJustificative pieceJustificative) {
        this.piecejustificatives.add(pieceJustificative);
        pieceJustificative.setCandidat(this);
        return this;
    }

    public Candidat removePiecejustificative(PieceJustificative pieceJustificative) {
        this.piecejustificatives.remove(pieceJustificative);
        pieceJustificative.setCandidat(null);
        return this;
    }

    public Set<Parcours> getParcours() {
        return this.parcours;
    }

    public void setParcours(Set<Parcours> parcours) {
        if (this.parcours != null) {
            this.parcours.forEach(i -> i.setCandidat(null));
        }
        if (parcours != null) {
            parcours.forEach(i -> i.setCandidat(this));
        }
        this.parcours = parcours;
    }

    public Candidat parcours(Set<Parcours> parcours) {
        this.setParcours(parcours);
        return this;
    }

    public Candidat addParcours(Parcours parcours) {
        this.parcours.add(parcours);
        parcours.setCandidat(this);
        return this;
    }

    public Candidat removeParcours(Parcours parcours) {
        this.parcours.remove(parcours);
        parcours.setCandidat(null);
        return this;
    }

    public ContactUrgence getContacturgence() {
        return this.contacturgence;
    }

    public void setContacturgence(ContactUrgence contactUrgence) {
        if (this.contacturgence != null) {
            this.contacturgence.setCandidat(null);
        }
        if (contactUrgence != null) {
            contactUrgence.setCandidat(this);
        }
        this.contacturgence = contactUrgence;
    }

    public Candidat contacturgence(ContactUrgence contactUrgence) {
        this.setContacturgence(contactUrgence);
        return this;
    }

    public Dossier getN() {
        return this.n;
    }

    public void setN(Dossier dossier) {
        if (this.n != null) {
            this.n.setCandidat(null);
        }
        if (dossier != null) {
            dossier.setCandidat(this);
        }
        this.n = dossier;
    }

    public Candidat n(Dossier dossier) {
        this.setN(dossier);
        return this;
    }

    public Set<RendezVous> getRendezVous() {
        return this.rendezVous;
    }

    public void setRendezVous(Set<RendezVous> rendezVous) {
        if (this.rendezVous != null) {
            this.rendezVous.forEach(i -> i.setCandidat(null));
        }
        if (rendezVous != null) {
            rendezVous.forEach(i -> i.setCandidat(this));
        }
        this.rendezVous = rendezVous;
    }

    public Candidat rendezVous(Set<RendezVous> rendezVous) {
        this.setRendezVous(rendezVous);
        return this;
    }

    public Candidat addRendezVous(RendezVous rendezVous) {
        this.rendezVous.add(rendezVous);
        rendezVous.setCandidat(this);
        return this;
    }

    public Candidat removeRendezVous(RendezVous rendezVous) {
        this.rendezVous.remove(rendezVous);
        rendezVous.setCandidat(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Candidat)) {
            return false;
        }
        return getId() != null && getId().equals(((Candidat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Candidat{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", nationalite='" + getNationalite() + "'" +
            ", typePieceIdentite='" + getTypePieceIdentite() + "'" +
            ", email='" + getEmail() + "'" +
            ", dateNaiss='" + getDateNaiss() + "'" +
            "}";
    }
}
