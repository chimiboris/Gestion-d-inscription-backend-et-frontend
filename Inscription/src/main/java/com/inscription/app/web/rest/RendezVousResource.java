package com.inscription.app.web.rest;

import com.inscription.app.repository.RendezVousRepository;
import com.inscription.app.service.RendezVousService;
import com.inscription.app.service.dto.RendezVousDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.inscription.app.domain.RendezVous}.
 */
@RestController
@RequestMapping("/api/rendez-vous")
public class RendezVousResource {

    private final Logger log = LoggerFactory.getLogger(RendezVousResource.class);

    private static final String ENTITY_NAME = "rendezVous";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RendezVousService rendezVousService;

    private final RendezVousRepository rendezVousRepository;

    public RendezVousResource(RendezVousService rendezVousService, RendezVousRepository rendezVousRepository) {
        this.rendezVousService = rendezVousService;
        this.rendezVousRepository = rendezVousRepository;
    }

    @PostMapping("/public-rendez-vous")
    public ResponseEntity<RendezVousDTO> prendreRendezVous(@Valid @RequestBody RendezVousDTO rendezVousDTO) {
        log.debug("Public prise de rendez-vous : {}", rendezVousDTO);
        RendezVousDTO result = rendezVousService.save(rendezVousDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    /**
     * {@code POST  /rendez-vous} : Create a new rendezVous.
     *
     * @param rendezVousDTO the rendezVousDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rendezVousDTO, or with status {@code 400 (Bad Request)} if the rendezVous has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RendezVousDTO> createRendezVous(@Valid @RequestBody RendezVousDTO rendezVousDTO) throws URISyntaxException {
        log.debug("REST request to save RendezVous : {}", rendezVousDTO);
        if (rendezVousDTO.getId() != null) {
            throw new BadRequestAlertException("A new rendezVous cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RendezVousDTO result = rendezVousService.save(rendezVousDTO);
        return ResponseEntity
            .created(new URI("/api/rendez-vous/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rendez-vous/:id} : Updates an existing rendezVous.
     *
     * @param id the id of the rendezVousDTO to save.
     * @param rendezVousDTO the rendezVousDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rendezVousDTO,
     * or with status {@code 400 (Bad Request)} if the rendezVousDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rendezVousDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RendezVousDTO> updateRendezVous(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RendezVousDTO rendezVousDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RendezVous : {}, {}", id, rendezVousDTO);
        if (rendezVousDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rendezVousDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rendezVousRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RendezVousDTO result = rendezVousService.update(rendezVousDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rendezVousDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rendez-vous/:id} : Partial updates given fields of an existing rendezVous, field will ignore if it is null
     *
     * @param id the id of the rendezVousDTO to save.
     * @param rendezVousDTO the rendezVousDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rendezVousDTO,
     * or with status {@code 400 (Bad Request)} if the rendezVousDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rendezVousDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rendezVousDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RendezVousDTO> partialUpdateRendezVous(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RendezVousDTO rendezVousDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RendezVous partially : {}, {}", id, rendezVousDTO);
        if (rendezVousDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rendezVousDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rendezVousRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RendezVousDTO> result = rendezVousService.partialUpdate(rendezVousDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rendezVousDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rendez-vous} : get all the rendezVous.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rendezVous in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RendezVousDTO>> getAllRendezVous(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of RendezVous");
        Page<RendezVousDTO> page = rendezVousService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rendez-vous/:id} : get the "id" rendezVous.
     *
     * @param id the id of the rendezVousDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rendezVousDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RendezVousDTO> getRendezVous(@PathVariable("id") Long id) {
        log.debug("REST request to get RendezVous : {}", id);
        Optional<RendezVousDTO> rendezVousDTO = rendezVousService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rendezVousDTO);
    }

    /**
     * {@code DELETE  /rendez-vous/:id} : delete the "id" rendezVous.
     *
     * @param id the id of the rendezVousDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRendezVous(@PathVariable("id") Long id) {
        log.debug("REST request to delete RendezVous : {}", id);
        rendezVousService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
