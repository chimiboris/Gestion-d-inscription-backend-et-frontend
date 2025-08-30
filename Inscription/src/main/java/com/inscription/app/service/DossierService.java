package com.inscription.app.service;

import com.inscription.app.domain.Dossier;
import com.inscription.app.repository.CandidatRepository;
import com.inscription.app.repository.DossierRepository;
import com.inscription.app.service.dto.DossierDTO;
import com.inscription.app.service.mapper.DossierMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.inscription.app.domain.Dossier}.
 */
@Service
@Transactional
public class DossierService {

    private final Logger log = LoggerFactory.getLogger(DossierService.class);

    private final DossierRepository dossierRepository;

    private final DossierMapper dossierMapper;

    private final CandidatRepository candidatRepository;

    public DossierService(DossierRepository dossierRepository, DossierMapper dossierMapper, CandidatRepository candidatRepository) {
        this.dossierRepository = dossierRepository;
        this.dossierMapper = dossierMapper;
        this.candidatRepository = candidatRepository;
    }

    /**
     * Save a dossier.
     *
     * @param dossierDTO the entity to save.
     * @return the persisted entity.
     */
    public DossierDTO save(DossierDTO dossierDTO) {
        log.debug("Request to save Dossier : {}", dossierDTO);
        Dossier dossier = dossierMapper.toEntity(dossierDTO);
        Long candidatId = dossierDTO.getCandidat().getId();
        candidatRepository.findById(candidatId).ifPresent(dossier::candidat);
        dossier = dossierRepository.save(dossier);
        return dossierMapper.toDto(dossier);
    }

    /**
     * Update a dossier.
     *
     * @param dossierDTO the entity to save.
     * @return the persisted entity.
     */
    public DossierDTO update(DossierDTO dossierDTO) {
        log.debug("Request to update Dossier : {}", dossierDTO);
        Dossier dossier = dossierMapper.toEntity(dossierDTO);
        Long candidatId = dossierDTO.getCandidat().getId();
        candidatRepository.findById(candidatId).ifPresent(dossier::candidat);
        dossier = dossierRepository.save(dossier);
        return dossierMapper.toDto(dossier);
    }

    /**
     * Partially update a dossier.
     *
     * @param dossierDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DossierDTO> partialUpdate(DossierDTO dossierDTO) {
        log.debug("Request to partially update Dossier : {}", dossierDTO);

        return dossierRepository
            .findById(dossierDTO.getId())
            .map(existingDossier -> {
                dossierMapper.partialUpdate(existingDossier, dossierDTO);

                return existingDossier;
            })
            .map(dossierRepository::save)
            .map(dossierMapper::toDto);
    }

    /**
     * Get all the dossiers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DossierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dossiers");
        return dossierRepository.findAll(pageable).map(dossierMapper::toDto);
    }

    /**
     * Get all the dossiers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DossierDTO> findAllWithEagerRelationships(Pageable pageable) {
        return dossierRepository.findAllWithEagerRelationships(pageable).map(dossierMapper::toDto);
    }

    public Dossier findEntityById(Long id) {
        return dossierRepository.findById(id).orElseThrow(() -> new RuntimeException("Dossier not found"));
    }


    /**
     * Get one dossier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DossierDTO> findOne(Long id) {
        log.debug("Request to get Dossier : {}", id);
        return dossierRepository.findOneWithEagerRelationships(id).map(dossierMapper::toDto);
    }

    /**
     * Delete the dossier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Dossier : {}", id);
        dossierRepository.deleteById(id);
    }
}
