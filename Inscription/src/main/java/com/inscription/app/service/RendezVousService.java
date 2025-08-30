package com.inscription.app.service;

import com.inscription.app.domain.RendezVous;
import com.inscription.app.repository.RendezVousRepository;
import com.inscription.app.service.dto.RendezVousDTO;
import com.inscription.app.service.mapper.RendezVousMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.inscription.app.domain.RendezVous}.
 */
@Service
@Transactional
public class RendezVousService {

    private final Logger log = LoggerFactory.getLogger(RendezVousService.class);

    private final RendezVousRepository rendezVousRepository;

    private final RendezVousMapper rendezVousMapper;

    public RendezVousService(RendezVousRepository rendezVousRepository, RendezVousMapper rendezVousMapper) {
        this.rendezVousRepository = rendezVousRepository;
        this.rendezVousMapper = rendezVousMapper;
    }

    /**
     * Save a rendezVous.
     *
     * @param rendezVousDTO the entity to save.
     * @return the persisted entity.
     */
    public RendezVousDTO save(RendezVousDTO rendezVousDTO) {
        log.debug("Request to save RendezVous : {}", rendezVousDTO);
        RendezVous rendezVous = rendezVousMapper.toEntity(rendezVousDTO);
        rendezVous = rendezVousRepository.save(rendezVous);
        return rendezVousMapper.toDto(rendezVous);
    }

    /**
     * Update a rendezVous.
     *
     * @param rendezVousDTO the entity to save.
     * @return the persisted entity.
     */
    public RendezVousDTO update(RendezVousDTO rendezVousDTO) {
        log.debug("Request to update RendezVous : {}", rendezVousDTO);
        RendezVous rendezVous = rendezVousMapper.toEntity(rendezVousDTO);
        rendezVous = rendezVousRepository.save(rendezVous);
        return rendezVousMapper.toDto(rendezVous);
    }

    /**
     * Partially update a rendezVous.
     *
     * @param rendezVousDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RendezVousDTO> partialUpdate(RendezVousDTO rendezVousDTO) {
        log.debug("Request to partially update RendezVous : {}", rendezVousDTO);

        return rendezVousRepository
            .findById(rendezVousDTO.getId())
            .map(existingRendezVous -> {
                rendezVousMapper.partialUpdate(existingRendezVous, rendezVousDTO);

                return existingRendezVous;
            })
            .map(rendezVousRepository::save)
            .map(rendezVousMapper::toDto);
    }

    /**
     * Get all the rendezVous.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RendezVousDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RendezVous");
        return rendezVousRepository.findAll(pageable).map(rendezVousMapper::toDto);
    }

    /**
     * Get one rendezVous by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RendezVousDTO> findOne(Long id) {
        log.debug("Request to get RendezVous : {}", id);
        return rendezVousRepository.findById(id).map(rendezVousMapper::toDto);
    }

    /**
     * Delete the rendezVous by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RendezVous : {}", id);
        rendezVousRepository.deleteById(id);
    }
}
