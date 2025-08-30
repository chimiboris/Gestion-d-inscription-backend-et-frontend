package com.inscription.app.domain;

import static com.inscription.app.domain.CandidatTestSamples.*;
import static com.inscription.app.domain.PieceJustificativeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.inscription.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PieceJustificativeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PieceJustificative.class);
        PieceJustificative pieceJustificative1 = getPieceJustificativeSample1();
        PieceJustificative pieceJustificative2 = new PieceJustificative();
        assertThat(pieceJustificative1).isNotEqualTo(pieceJustificative2);

        pieceJustificative2.setId(pieceJustificative1.getId());
        assertThat(pieceJustificative1).isEqualTo(pieceJustificative2);

        pieceJustificative2 = getPieceJustificativeSample2();
        assertThat(pieceJustificative1).isNotEqualTo(pieceJustificative2);
    }

    @Test
    void candidatTest() throws Exception {
        PieceJustificative pieceJustificative = getPieceJustificativeRandomSampleGenerator();
        Candidat candidatBack = getCandidatRandomSampleGenerator();

        pieceJustificative.setCandidat(candidatBack);
        assertThat(pieceJustificative.getCandidat()).isEqualTo(candidatBack);

        pieceJustificative.candidat(null);
        assertThat(pieceJustificative.getCandidat()).isNull();
    }
}
