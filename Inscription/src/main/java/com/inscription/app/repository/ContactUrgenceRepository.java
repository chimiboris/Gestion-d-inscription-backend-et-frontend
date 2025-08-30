package com.inscription.app.repository;

import com.inscription.app.domain.ContactUrgence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContactUrgence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactUrgenceRepository extends JpaRepository<ContactUrgence, Long> {}
