package com.inscription.app.web.rest;

import com.inscription.app.repository.ParcoursRepository;
import com.inscription.app.service.ParcoursService;
import com.inscription.app.service.dto.ParcoursDTO;
import com.inscription.app.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.inscription.app.domain.Parcours}.
 */
@RestController
@RequestMapping("/api/parcours")
public class ParcoursResource {

    private final Logger log = LoggerFactory.getLogger(ParcoursResource.class);

    private static final String ENTITY_NAME = "parcours";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParcoursService parcoursService;

    private final ParcoursRepository parcoursRepository;

    public ParcoursResource(ParcoursService parcoursService, ParcoursRepository parcoursRepository) {
        this.parcoursService = parcoursService;
        this.parcoursRepository = parcoursRepository;
    }

    /**
     * {@code POST  /parcours} : Create a new parcours.
     *
     * @param parcoursDTO the parcoursDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parcoursDTO, or with status {@code 400 (Bad Request)} if the parcours has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ParcoursDTO> createParcours(@Valid @RequestBody ParcoursDTO parcoursDTO) throws URISyntaxException {
        log.debug("REST request to save Parcours : {}", parcoursDTO);
        if (parcoursDTO.getId() != null) {
            throw new BadRequestAlertException("A new parcours cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParcoursDTO result = parcoursService.save(parcoursDTO);
        return ResponseEntity
            .created(new URI("/api/parcours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parcours/:id} : Updates an existing parcours.
     *
     * @param id the id of the parcoursDTO to save.
     * @param parcoursDTO the parcoursDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parcoursDTO,
     * or with status {@code 400 (Bad Request)} if the parcoursDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parcoursDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ParcoursDTO> updateParcours(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParcoursDTO parcoursDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Parcours : {}, {}", id, parcoursDTO);
        if (parcoursDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parcoursDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parcoursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParcoursDTO result = parcoursService.update(parcoursDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parcoursDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parcours/:id} : Partial updates given fields of an existing parcours, field will ignore if it is null
     *
     * @param id the id of the parcoursDTO to save.
     * @param parcoursDTO the parcoursDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parcoursDTO,
     * or with status {@code 400 (Bad Request)} if the parcoursDTO is not valid,
     * or with status {@code 404 (Not Found)} if the parcoursDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the parcoursDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ParcoursDTO> partialUpdateParcours(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParcoursDTO parcoursDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Parcours partially : {}, {}", id, parcoursDTO);
        if (parcoursDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parcoursDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parcoursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParcoursDTO> result = parcoursService.partialUpdate(parcoursDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parcoursDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /parcours} : get all the parcours.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parcours in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ParcoursDTO>> getAllParcours(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Parcours");
        Page<ParcoursDTO> page = parcoursService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parcours/:id} : get the "id" parcours.
     *
     * @param id the id of the parcoursDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parcoursDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ParcoursDTO> getParcours(@PathVariable("id") Long id) {
        log.debug("REST request to get Parcours : {}", id);
        Optional<ParcoursDTO> parcoursDTO = parcoursService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parcoursDTO);
    }

    /**
     * {@code DELETE  /parcours/:id} : delete the "id" parcours.
     *
     * @param id the id of the parcoursDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParcours(@PathVariable("id") Long id) {
        log.debug("REST request to delete Parcours : {}", id);
        parcoursService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
