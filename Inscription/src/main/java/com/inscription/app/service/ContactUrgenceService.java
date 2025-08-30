package com.inscription.app.service;

import com.inscription.app.domain.ContactUrgence;
import com.inscription.app.repository.CandidatRepository;
import com.inscription.app.repository.ContactUrgenceRepository;
import com.inscription.app.service.dto.ContactUrgenceDTO;
import com.inscription.app.service.mapper.ContactUrgenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.inscription.app.domain.ContactUrgence}.
 */
@Service
@Transactional
public class ContactUrgenceService {

    private final Logger log = LoggerFactory.getLogger(ContactUrgenceService.class);

    private final ContactUrgenceRepository contactUrgenceRepository;

    private final ContactUrgenceMapper contactUrgenceMapper;

    private final CandidatRepository candidatRepository;

    public ContactUrgenceService(
        ContactUrgenceRepository contactUrgenceRepository,
        ContactUrgenceMapper contactUrgenceMapper,
        CandidatRepository candidatRepository
    ) {
        this.contactUrgenceRepository = contactUrgenceRepository;
        this.contactUrgenceMapper = contactUrgenceMapper;
        this.candidatRepository = candidatRepository;
    }

    /**
     * Save a contactUrgence.
     *
     * @param contactUrgenceDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactUrgenceDTO save(ContactUrgenceDTO contactUrgenceDTO) {
        log.debug("Request to save ContactUrgence : {}", contactUrgenceDTO);
        ContactUrgence contactUrgence = contactUrgenceMapper.toEntity(contactUrgenceDTO);
        Long candidatId = contactUrgenceDTO.getCandidat().getId();
        candidatRepository.findById(candidatId).ifPresent(contactUrgence::candidat);
        contactUrgence = contactUrgenceRepository.save(contactUrgence);
        return contactUrgenceMapper.toDto(contactUrgence);
    }

    /**
     * Update a contactUrgence.
     *
     * @param contactUrgenceDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactUrgenceDTO update(ContactUrgenceDTO contactUrgenceDTO) {
        log.debug("Request to update ContactUrgence : {}", contactUrgenceDTO);
        ContactUrgence contactUrgence = contactUrgenceMapper.toEntity(contactUrgenceDTO);
        Long candidatId = contactUrgenceDTO.getCandidat().getId();
        candidatRepository.findById(candidatId).ifPresent(contactUrgence::candidat);
        contactUrgence = contactUrgenceRepository.save(contactUrgence);
        return contactUrgenceMapper.toDto(contactUrgence);
    }

    /**
     * Partially update a contactUrgence.
     *
     * @param contactUrgenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactUrgenceDTO> partialUpdate(ContactUrgenceDTO contactUrgenceDTO) {
        log.debug("Request to partially update ContactUrgence : {}", contactUrgenceDTO);

        return contactUrgenceRepository
            .findById(contactUrgenceDTO.getId())
            .map(existingContactUrgence -> {
                contactUrgenceMapper.partialUpdate(existingContactUrgence, contactUrgenceDTO);

                return existingContactUrgence;
            })
            .map(contactUrgenceRepository::save)
            .map(contactUrgenceMapper::toDto);
    }

    /**
     * Get all the contactUrgences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactUrgenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactUrgences");
        return contactUrgenceRepository.findAll(pageable).map(contactUrgenceMapper::toDto);
    }

    /**
     * Get one contactUrgence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactUrgenceDTO> findOne(Long id) {
        log.debug("Request to get ContactUrgence : {}", id);
        return contactUrgenceRepository.findById(id).map(contactUrgenceMapper::toDto);
    }

    /**
     * Delete the contactUrgence by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactUrgence : {}", id);
        contactUrgenceRepository.deleteById(id);
    }
}
