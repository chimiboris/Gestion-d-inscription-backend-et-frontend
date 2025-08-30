package com.inscription.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ParcoursTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Parcours getParcoursSample1() {
        return new Parcours().id(1L).etablissement("etablissement1").specialisation("specialisation1").commentaire("commentaire1");
    }

    public static Parcours getParcoursSample2() {
        return new Parcours().id(2L).etablissement("etablissement2").specialisation("specialisation2").commentaire("commentaire2");
    }

    public static Parcours getParcoursRandomSampleGenerator() {
        return new Parcours()
            .id(longCount.incrementAndGet())
            .etablissement(UUID.randomUUID().toString())
            .specialisation(UUID.randomUUID().toString())
            .commentaire(UUID.randomUUID().toString());
    }
}
