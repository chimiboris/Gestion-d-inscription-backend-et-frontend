package com.inscription.app.domain;

import static com.inscription.app.domain.CandidatTestSamples.*;
import static com.inscription.app.domain.ContactUrgenceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.inscription.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactUrgenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactUrgence.class);
        ContactUrgence contactUrgence1 = getContactUrgenceSample1();
        ContactUrgence contactUrgence2 = new ContactUrgence();
        assertThat(contactUrgence1).isNotEqualTo(contactUrgence2);

        contactUrgence2.setId(contactUrgence1.getId());
        assertThat(contactUrgence1).isEqualTo(contactUrgence2);

        contactUrgence2 = getContactUrgenceSample2();
        assertThat(contactUrgence1).isNotEqualTo(contactUrgence2);
    }

    @Test
    void candidatTest() throws Exception {
        ContactUrgence contactUrgence = getContactUrgenceRandomSampleGenerator();
        Candidat candidatBack = getCandidatRandomSampleGenerator();

        contactUrgence.setCandidat(candidatBack);
        assertThat(contactUrgence.getCandidat()).isEqualTo(candidatBack);

        contactUrgence.candidat(null);
        assertThat(contactUrgence.getCandidat()).isNull();
    }
}
