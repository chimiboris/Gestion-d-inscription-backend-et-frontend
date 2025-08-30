package com.inscription.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CandidatTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Candidat getCandidatSample1() {
        return new Candidat().id(1L).nom("nom1").prenom("prenom1").nationalite("nationalite1").email("email1");
    }

    public static Candidat getCandidatSample2() {
        return new Candidat().id(2L).nom("nom2").prenom("prenom2").nationalite("nationalite2").email("email2");
    }

    public static Candidat getCandidatRandomSampleGenerator() {
        return new Candidat()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .nationalite(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
