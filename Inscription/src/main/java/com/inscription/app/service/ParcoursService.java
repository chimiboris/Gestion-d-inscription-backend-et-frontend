package com.inscription.app.service;

import com.inscription.app.domain.Parcours;
import com.inscription.app.repository.ParcoursRepository;
import com.inscription.app.service.dto.ParcoursDTO;
import com.inscription.app.service.mapper.ParcoursMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.inscription.app.domain.Parcours}.
 */
@Service
@Transactional
public class ParcoursService {

    private final Logger log = LoggerFactory.getLogger(ParcoursService.class);

    private final ParcoursRepository parcoursRepository;

    private final ParcoursMapper parcoursMapper;

    public ParcoursService(ParcoursRepository parcoursRepository, ParcoursMapper parcoursMapper) {
        this.parcoursRepository = parcoursRepository;
        this.parcoursMapper = parcoursMapper;
    }

    /**
     * Save a parcours.
     *
     * @param parcoursDTO the entity to save.
     * @return the persisted entity.
     */
    public ParcoursDTO save(ParcoursDTO parcoursDTO) {
        log.debug("Request to save Parcours : {}", parcoursDTO);
        Parcours parcours = parcoursMapper.toEntity(parcoursDTO);
        parcours = parcoursRepository.save(parcours);
        return parcoursMapper.toDto(parcours);
    }

    /**
     * Update a parcours.
     *
     * @param parcoursDTO the entity to save.
     * @return the persisted entity.
     */
    public ParcoursDTO update(ParcoursDTO parcoursDTO) {
        log.debug("Request to update Parcours : {}", parcoursDTO);
        Parcours parcours = parcoursMapper.toEntity(parcoursDTO);
        parcours = parcoursRepository.save(parcours);
        return parcoursMapper.toDto(parcours);
    }

    /**
     * Partially update a parcours.
     *
     * @param parcoursDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParcoursDTO> partialUpdate(ParcoursDTO parcoursDTO) {
        log.debug("Request to partially update Parcours : {}", parcoursDTO);

        return parcoursRepository
            .findById(parcoursDTO.getId())
            .map(existingParcours -> {
                parcoursMapper.partialUpdate(existingParcours, parcoursDTO);

                return existingParcours;
            })
            .map(parcoursRepository::save)
            .map(parcoursMapper::toDto);
    }

    /**
     * Get all the parcours.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ParcoursDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Parcours");
        return parcoursRepository.findAll(pageable).map(parcoursMapper::toDto);
    }

    /**
     * Get one parcours by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParcoursDTO> findOne(Long id) {
        log.debug("Request to get Parcours : {}", id);
        return parcoursRepository.findById(id).map(parcoursMapper::toDto);
    }

    /**
     * Delete the parcours by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Parcours : {}", id);
        parcoursRepository.deleteById(id);
    }
}
