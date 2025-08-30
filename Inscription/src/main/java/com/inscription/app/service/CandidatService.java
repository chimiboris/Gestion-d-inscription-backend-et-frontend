package com.inscription.app.service;

import com.inscription.app.domain.Candidat;
import com.inscription.app.repository.CandidatRepository;
import com.inscription.app.service.dto.CandidatDTO;
import com.inscription.app.service.mapper.CandidatMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.inscription.app.domain.Candidat}.
 */
@Service
@Transactional
public class CandidatService {

    private final Logger log = LoggerFactory.getLogger(CandidatService.class);

    private final CandidatRepository candidatRepository;

    private final CandidatMapper candidatMapper;

    public CandidatService(CandidatRepository candidatRepository, CandidatMapper candidatMapper) {
        this.candidatRepository = candidatRepository;
        this.candidatMapper = candidatMapper;
    }

    /**
     * Save a candidat.
     *
     * @param candidatDTO the entity to save.
     * @return the persisted entity.
     */
    public CandidatDTO save(CandidatDTO candidatDTO) {
        log.debug("Request to save Candidat : {}", candidatDTO);
        Candidat candidat = candidatMapper.toEntity(candidatDTO);
        candidat = candidatRepository.save(candidat);
        return candidatMapper.toDto(candidat);
    }

    /**
     * Update a candidat.
     *
     * @param candidatDTO the entity to save.
     * @return the persisted entity.
     */
    public CandidatDTO update(CandidatDTO candidatDTO) {
        log.debug("Request to update Candidat : {}", candidatDTO);
        Candidat candidat = candidatMapper.toEntity(candidatDTO);
        candidat = candidatRepository.save(candidat);
        return candidatMapper.toDto(candidat);
    }

    /**
     * Partially update a candidat.
     *
     * @param candidatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CandidatDTO> partialUpdate(CandidatDTO candidatDTO) {
        log.debug("Request to partially update Candidat : {}", candidatDTO);

        return candidatRepository
            .findById(candidatDTO.getId())
            .map(existingCandidat -> {
                candidatMapper.partialUpdate(existingCandidat, candidatDTO);

                return existingCandidat;
            })
            .map(candidatRepository::save)
            .map(candidatMapper::toDto);
    }

    /**
     * Get all the candidats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CandidatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Candidats");
        return candidatRepository.findAll(pageable).map(candidatMapper::toDto);
    }

    /**
     * Get all the candidats with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CandidatDTO> findAllWithEagerRelationships(Pageable pageable) {
        return candidatRepository.findAllWithEagerRelationships(pageable).map(candidatMapper::toDto);
    }

    /**
     *  Get all the candidats where Contacturgence is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CandidatDTO> findAllWhereContacturgenceIsNull() {
        log.debug("Request to get all candidats where Contacturgence is null");
        return StreamSupport
            .stream(candidatRepository.findAll().spliterator(), false)
            .filter(candidat -> candidat.getContacturgence() == null)
            .map(candidatMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the candidats where N is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CandidatDTO> findAllWhereNIsNull() {
        log.debug("Request to get all candidats where N is null");
        return StreamSupport
            .stream(candidatRepository.findAll().spliterator(), false)
            .filter(candidat -> candidat.getN() == null)
            .map(candidatMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one candidat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CandidatDTO> findOne(Long id) {
        log.debug("Request to get Candidat : {}", id);
        return candidatRepository.findOneWithEagerRelationships(id).map(candidatMapper::toDto);
    }

    /**
     * Delete the candidat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Candidat : {}", id);
        candidatRepository.deleteById(id);
    }
}
