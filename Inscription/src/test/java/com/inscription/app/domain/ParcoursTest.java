package com.inscription.app.domain;

import static com.inscription.app.domain.CandidatTestSamples.*;
import static com.inscription.app.domain.ParcoursTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.inscription.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParcoursTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parcours.class);
        Parcours parcours1 = getParcoursSample1();
        Parcours parcours2 = new Parcours();
        assertThat(parcours1).isNotEqualTo(parcours2);

        parcours2.setId(parcours1.getId());
        assertThat(parcours1).isEqualTo(parcours2);

        parcours2 = getParcoursSample2();
        assertThat(parcours1).isNotEqualTo(parcours2);
    }

    @Test
    void candidatTest() throws Exception {
        Parcours parcours = getParcoursRandomSampleGenerator();
        Candidat candidatBack = getCandidatRandomSampleGenerator();

        parcours.setCandidat(candidatBack);
        assertThat(parcours.getCandidat()).isEqualTo(candidatBack);

        parcours.candidat(null);
        assertThat(parcours.getCandidat()).isNull();
    }
}
