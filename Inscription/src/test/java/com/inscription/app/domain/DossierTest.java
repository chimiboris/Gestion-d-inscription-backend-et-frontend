package com.inscription.app.domain;

import static com.inscription.app.domain.CandidatTestSamples.*;
import static com.inscription.app.domain.DossierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.inscription.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DossierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dossier.class);
        Dossier dossier1 = getDossierSample1();
        Dossier dossier2 = new Dossier();
        assertThat(dossier1).isNotEqualTo(dossier2);

        dossier2.setId(dossier1.getId());
        assertThat(dossier1).isEqualTo(dossier2);

        dossier2 = getDossierSample2();
        assertThat(dossier1).isNotEqualTo(dossier2);
    }

    @Test
    void candidatTest() throws Exception {
        Dossier dossier = getDossierRandomSampleGenerator();
        Candidat candidatBack = getCandidatRandomSampleGenerator();

        dossier.setCandidat(candidatBack);
        assertThat(dossier.getCandidat()).isEqualTo(candidatBack);

        dossier.candidat(null);
        assertThat(dossier.getCandidat()).isNull();
    }
}
