package com.inscription.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContactUrgenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContactUrgence getContactUrgenceSample1() {
        return new ContactUrgence().id(1L).nomComplet("nomComplet1").lienParente("lienParente1").telephone("telephone1").email("email1");
    }

    public static ContactUrgence getContactUrgenceSample2() {
        return new ContactUrgence().id(2L).nomComplet("nomComplet2").lienParente("lienParente2").telephone("telephone2").email("email2");
    }

    public static ContactUrgence getContactUrgenceRandomSampleGenerator() {
        return new ContactUrgence()
            .id(longCount.incrementAndGet())
            .nomComplet(UUID.randomUUID().toString())
            .lienParente(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
