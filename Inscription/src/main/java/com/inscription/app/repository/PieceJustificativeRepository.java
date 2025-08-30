package com.inscription.app.repository;

import com.inscription.app.domain.PieceJustificative;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the PieceJustificative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PieceJustificativeRepository extends JpaRepository<PieceJustificative, Long> {
    List<PieceJustificative> findByCandidatId(Long id);
}
