package com.inscription.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PieceJustificativeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PieceJustificative getPieceJustificativeSample1() {
        return new PieceJustificative().id(1L).commentaire("commentaire1");
    }

    public static PieceJustificative getPieceJustificativeSample2() {
        return new PieceJustificative().id(2L).commentaire("commentaire2");
    }

    public static PieceJustificative getPieceJustificativeRandomSampleGenerator() {
        return new PieceJustificative().id(longCount.incrementAndGet()).commentaire(UUID.randomUUID().toString());
    }
}
