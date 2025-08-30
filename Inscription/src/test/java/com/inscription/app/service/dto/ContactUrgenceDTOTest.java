package com.inscription.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.inscription.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactUrgenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactUrgenceDTO.class);
        ContactUrgenceDTO contactUrgenceDTO1 = new ContactUrgenceDTO();
        contactUrgenceDTO1.setId(1L);
        ContactUrgenceDTO contactUrgenceDTO2 = new ContactUrgenceDTO();
        assertThat(contactUrgenceDTO1).isNotEqualTo(contactUrgenceDTO2);
        contactUrgenceDTO2.setId(contactUrgenceDTO1.getId());
        assertThat(contactUrgenceDTO1).isEqualTo(contactUrgenceDTO2);
        contactUrgenceDTO2.setId(2L);
        assertThat(contactUrgenceDTO1).isNotEqualTo(contactUrgenceDTO2);
        contactUrgenceDTO1.setId(null);
        assertThat(contactUrgenceDTO1).isNotEqualTo(contactUrgenceDTO2);
    }
}
