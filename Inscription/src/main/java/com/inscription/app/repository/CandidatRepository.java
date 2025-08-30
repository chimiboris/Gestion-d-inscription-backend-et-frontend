package com.inscription.app.repository;

import com.inscription.app.domain.Candidat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Candidat entity.
 */
@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Long> {

    long countBySexe(String sexe);

    @Query("select candidat from Candidat candidat where candidat.user.login = ?#{authentication.name}")
    List<Candidat> findByUserIsCurrentUser();

    default Optional<Candidat> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Candidat> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Candidat> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select candidat from Candidat candidat left join fetch candidat.user",
        countQuery = "select count(candidat) from Candidat candidat"
    )
    Page<Candidat> findAllWithToOneRelationships(Pageable pageable);

    @Query("select candidat from Candidat candidat left join fetch candidat.user")
    List<Candidat> findAllWithToOneRelationships();

    @Query("select candidat from Candidat candidat left join fetch candidat.user where candidat.id =:id")
    Optional<Candidat> findOneWithToOneRelationships(@Param("id") Long id);
}
