package com.inscription.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A ContactUrgence.
 */
@Entity
@Table(name = "contact_urgence")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactUrgence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_complet", nullable = false)
    private String nomComplet;

    @NotNull
    @Column(name = "lien_parente", nullable = false)
    private String lienParente;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "email")
    private String email;

    @JsonIgnoreProperties(value = { "user", "piecejustificatives", "parcours", "contacturgence", "n" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @MapsId
    @JoinColumn(name = "id")
    private Candidat candidat;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContactUrgence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomComplet() {
        return this.nomComplet;
    }

    public ContactUrgence nomComplet(String nomComplet) {
        this.setNomComplet(nomComplet);
        return this;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getLienParente() {
        return this.lienParente;
    }

    public ContactUrgence lienParente(String lienParente) {
        this.setLienParente(lienParente);
        return this;
    }

    public void setLienParente(String lienParente) {
        this.lienParente = lienParente;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public ContactUrgence telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return this.email;
    }

    public ContactUrgence email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Candidat getCandidat() {
        return this.candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    public ContactUrgence candidat(Candidat candidat) {
        this.setCandidat(candidat);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactUrgence)) {
            return false;
        }
        return getId() != null && getId().equals(((ContactUrgence) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactUrgence{" +
            "id=" + getId() +
            ", nomComplet='" + getNomComplet() + "'" +
            ", lienParente='" + getLienParente() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
