package com.inscription.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.inscription.app.domain.ContactUrgence} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactUrgenceDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomComplet;

    @NotNull
    private String lienParente;

    @NotNull
    private String telephone;

    private String email;

    private CandidatDTO candidat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getLienParente() {
        return lienParente;
    }

    public void setLienParente(String lienParente) {
        this.lienParente = lienParente;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        if (!(o instanceof ContactUrgenceDTO)) {
            return false;
        }

        ContactUrgenceDTO contactUrgenceDTO = (ContactUrgenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactUrgenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactUrgenceDTO{" +
            "id=" + getId() +
            ", nomComplet='" + getNomComplet() + "'" +
            ", lienParente='" + getLienParente() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", email='" + getEmail() + "'" +
            ", candidat=" + getCandidat() +
            "}";
    }
}
