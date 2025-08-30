package com.inscription.app.domain;

import static com.inscription.app.domain.CandidatTestSamples.*;
import static com.inscription.app.domain.RendezVousTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.inscription.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RendezVousTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RendezVous.class);
        RendezVous rendezVous1 = getRendezVousSample1();
        RendezVous rendezVous2 = new RendezVous();
        assertThat(rendezVous1).isNotEqualTo(rendezVous2);

        rendezVous2.setId(rendezVous1.getId());
        assertThat(rendezVous1).isEqualTo(rendezVous2);

        rendezVous2 = getRendezVousSample2();
        assertThat(rendezVous1).isNotEqualTo(rendezVous2);
    }

    @Test
    void candidatTest() throws Exception {
        RendezVous rendezVous = getRendezVousRandomSampleGenerator();
        Candidat candidatBack = getCandidatRandomSampleGenerator();

        rendezVous.setCandidat(candidatBack);
        assertThat(rendezVous.getCandidat()).isEqualTo(candidatBack);

        rendezVous.candidat(null);
        assertThat(rendezVous.getCandidat()).isNull();
    }
}
