package com.inscription.app.domain;

import static com.inscription.app.domain.CandidatTestSamples.*;
import static com.inscription.app.domain.ContactUrgenceTestSamples.*;
import static com.inscription.app.domain.DossierTestSamples.*;
import static com.inscription.app.domain.ParcoursTestSamples.*;
import static com.inscription.app.domain.PieceJustificativeTestSamples.*;
import static com.inscription.app.domain.RendezVousTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.inscription.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CandidatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Candidat.class);
        Candidat candidat1 = getCandidatSample1();
        Candidat candidat2 = new Candidat();
        assertThat(candidat1).isNotEqualTo(candidat2);

        candidat2.setId(candidat1.getId());
        assertThat(candidat1).isEqualTo(candidat2);

        candidat2 = getCandidatSample2();
        assertThat(candidat1).isNotEqualTo(candidat2);
    }

    @Test
    void piecejustificativeTest() throws Exception {
        Candidat candidat = getCandidatRandomSampleGenerator();
        PieceJustificative pieceJustificativeBack = getPieceJustificativeRandomSampleGenerator();

        candidat.addPiecejustificative(pieceJustificativeBack);
        assertThat(candidat.getPiecejustificatives()).containsOnly(pieceJustificativeBack);
        assertThat(pieceJustificativeBack.getCandidat()).isEqualTo(candidat);

        candidat.removePiecejustificative(pieceJustificativeBack);
        assertThat(candidat.getPiecejustificatives()).doesNotContain(pieceJustificativeBack);
        assertThat(pieceJustificativeBack.getCandidat()).isNull();

        candidat.piecejustificatives(new HashSet<>(Set.of(pieceJustificativeBack)));
        assertThat(candidat.getPiecejustificatives()).containsOnly(pieceJustificativeBack);
        assertThat(pieceJustificativeBack.getCandidat()).isEqualTo(candidat);

        candidat.setPiecejustificatives(new HashSet<>());
        assertThat(candidat.getPiecejustificatives()).doesNotContain(pieceJustificativeBack);
        assertThat(pieceJustificativeBack.getCandidat()).isNull();
    }

    @Test
    void parcoursTest() throws Exception {
        Candidat candidat = getCandidatRandomSampleGenerator();
        Parcours parcoursBack = getParcoursRandomSampleGenerator();

        candidat.addParcours(parcoursBack);
        assertThat(candidat.getParcours()).containsOnly(parcoursBack);
        assertThat(parcoursBack.getCandidat()).isEqualTo(candidat);

        candidat.removeParcours(parcoursBack);
        assertThat(candidat.getParcours()).doesNotContain(parcoursBack);
        assertThat(parcoursBack.getCandidat()).isNull();

        candidat.parcours(new HashSet<>(Set.of(parcoursBack)));
        assertThat(candidat.getParcours()).containsOnly(parcoursBack);
        assertThat(parcoursBack.getCandidat()).isEqualTo(candidat);

        candidat.setParcours(new HashSet<>());
        assertThat(candidat.getParcours()).doesNotContain(parcoursBack);
        assertThat(parcoursBack.getCandidat()).isNull();
    }

    @Test
    void contacturgenceTest() throws Exception {
        Candidat candidat = getCandidatRandomSampleGenerator();
        ContactUrgence contactUrgenceBack = getContactUrgenceRandomSampleGenerator();

        candidat.setContacturgence(contactUrgenceBack);
        assertThat(candidat.getContacturgence()).isEqualTo(contactUrgenceBack);
        assertThat(contactUrgenceBack.getCandidat()).isEqualTo(candidat);

        candidat.contacturgence(null);
        assertThat(candidat.getContacturgence()).isNull();
        assertThat(contactUrgenceBack.getCandidat()).isNull();
    }

    @Test
    void nTest() throws Exception {
        Candidat candidat = getCandidatRandomSampleGenerator();
        Dossier dossierBack = getDossierRandomSampleGenerator();

        candidat.setN(dossierBack);
        assertThat(candidat.getN()).isEqualTo(dossierBack);
        assertThat(dossierBack.getCandidat()).isEqualTo(candidat);

        candidat.n(null);
        assertThat(candidat.getN()).isNull();
        assertThat(dossierBack.getCandidat()).isNull();
    }

    @Test
    void rendezVousTest() throws Exception {
        Candidat candidat = getCandidatRandomSampleGenerator();
        RendezVous rendezVousBack = getRendezVousRandomSampleGenerator();

        candidat.addRendezVous(rendezVousBack);
        assertThat(candidat.getRendezVous()).containsOnly(rendezVousBack);
        assertThat(rendezVousBack.getCandidat()).isEqualTo(candidat);

        candidat.removeRendezVous(rendezVousBack);
        assertThat(candidat.getRendezVous()).doesNotContain(rendezVousBack);
        assertThat(rendezVousBack.getCandidat()).isNull();

        candidat.rendezVous(new HashSet<>(Set.of(rendezVousBack)));
        assertThat(candidat.getRendezVous()).containsOnly(rendezVousBack);
        assertThat(rendezVousBack.getCandidat()).isEqualTo(candidat);

        candidat.setRendezVous(new HashSet<>());
        assertThat(candidat.getRendezVous()).doesNotContain(rendezVousBack);
        assertThat(rendezVousBack.getCandidat()).isNull();
    }
}
