package com.inscription.app.service;

import com.inscription.app.domain.PieceJustificative;
import com.inscription.app.repository.PieceJustificativeRepository;
import com.inscription.app.service.dto.PieceJustificativeDTO;
import com.inscription.app.service.mapper.PieceJustificativeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.inscription.app.domain.PieceJustificative}.
 */
@Service
@Transactional
public class PieceJustificativeService {

    private final Logger log = LoggerFactory.getLogger(PieceJustificativeService.class);

    private final PieceJustificativeRepository pieceJustificativeRepository;

    private final PieceJustificativeMapper pieceJustificativeMapper;

    public PieceJustificativeService(
        PieceJustificativeRepository pieceJustificativeRepository,
        PieceJustificativeMapper pieceJustificativeMapper
    ) {
        this.pieceJustificativeRepository = pieceJustificativeRepository;
        this.pieceJustificativeMapper = pieceJustificativeMapper;
    }

    /**
     * Save a pieceJustificative.
     *
     * @param pieceJustificativeDTO the entity to save.
     * @return the persisted entity.
     */
    public PieceJustificativeDTO save(PieceJustificativeDTO pieceJustificativeDTO) {
        log.debug("Request to save PieceJustificative : {}", pieceJustificativeDTO);
        PieceJustificative pieceJustificative = pieceJustificativeMapper.toEntity(pieceJustificativeDTO);
        pieceJustificative = pieceJustificativeRepository.save(pieceJustificative);
        return pieceJustificativeMapper.toDto(pieceJustificative);
    }

    /**
     * Update a pieceJustificative.
     *
     * @param pieceJustificativeDTO the entity to save.
     * @return the persisted entity.
     */
    public PieceJustificativeDTO update(PieceJustificativeDTO pieceJustificativeDTO) {
        log.debug("Request to update PieceJustificative : {}", pieceJustificativeDTO);
        PieceJustificative pieceJustificative = pieceJustificativeMapper.toEntity(pieceJustificativeDTO);
        pieceJustificative = pieceJustificativeRepository.save(pieceJustificative);
        return pieceJustificativeMapper.toDto(pieceJustificative);
    }

    /**
     * Partially update a pieceJustificative.
     *
     * @param pieceJustificativeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PieceJustificativeDTO> partialUpdate(PieceJustificativeDTO pieceJustificativeDTO) {
        log.debug("Request to partially update PieceJustificative : {}", pieceJustificativeDTO);

        return pieceJustificativeRepository
            .findById(pieceJustificativeDTO.getId())
            .map(existingPieceJustificative -> {
                pieceJustificativeMapper.partialUpdate(existingPieceJustificative, pieceJustificativeDTO);

                return existingPieceJustificative;
            })
            .map(pieceJustificativeRepository::save)
            .map(pieceJustificativeMapper::toDto);
    }

    /**
     * Get all the pieceJustificatives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PieceJustificativeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PieceJustificatives");
        return pieceJustificativeRepository.findAll(pageable).map(pieceJustificativeMapper::toDto);
    }

    /**
     * Get one pieceJustificative by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PieceJustificativeDTO> findOne(Long id) {
        log.debug("Request to get PieceJustificative : {}", id);
        return pieceJustificativeRepository.findById(id).map(pieceJustificativeMapper::toDto);
    }

    /**
     * Delete the pieceJustificative by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PieceJustificative : {}", id);
        pieceJustificativeRepository.deleteById(id);
    }
}
