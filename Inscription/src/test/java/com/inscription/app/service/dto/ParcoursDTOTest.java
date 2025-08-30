package com.inscription.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.inscription.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParcoursDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParcoursDTO.class);
        ParcoursDTO parcoursDTO1 = new ParcoursDTO();
        parcoursDTO1.setId(1L);
        ParcoursDTO parcoursDTO2 = new ParcoursDTO();
        assertThat(parcoursDTO1).isNotEqualTo(parcoursDTO2);
        parcoursDTO2.setId(parcoursDTO1.getId());
        assertThat(parcoursDTO1).isEqualTo(parcoursDTO2);
        parcoursDTO2.setId(2L);
        assertThat(parcoursDTO1).isNotEqualTo(parcoursDTO2);
        parcoursDTO1.setId(null);
        assertThat(parcoursDTO1).isNotEqualTo(parcoursDTO2);
    }
}
