package com.inscription.app.repository;

import com.inscription.app.domain.Dossier;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.inscription.app.domain.enumeration.StatutDossier;

/**
 * Spring Data JPA repository for the Dossier entity.
 */
@Repository
public interface DossierRepository extends JpaRepository<Dossier, Long> {

    long countByStatut(StatutDossier statut);

    @Query("select dossier from Dossier dossier where dossier.agent.login = ?#{authentication.name}")
    List<Dossier> findByAgentIsCurrentUser();

    default Optional<Dossier> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Dossier> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Dossier> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query("SELECT d FROM Dossier d LEFT JOIN FETCH d.candidat WHERE d.id = :id")
    Optional<Dossier> findByIdWithCandidat(@Param("id") Long id);


    @Query(
        value = "select dossier from Dossier dossier left join fetch dossier.agent",
        countQuery = "select count(dossier) from Dossier dossier"
    )
    Page<Dossier> findAllWithToOneRelationships(Pageable pageable);

    @Query("select dossier from Dossier dossier left join fetch dossier.agent")
    List<Dossier> findAllWithToOneRelationships();

    @Query("select dossier from Dossier dossier left join fetch dossier.agent where dossier.id =:id")
    Optional<Dossier> findOneWithToOneRelationships(@Param("id") Long id);
}
