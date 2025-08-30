package com.inscription.app.web.rest;

import com.inscription.app.repository.ContactUrgenceRepository;
import com.inscription.app.service.ContactUrgenceService;
import com.inscription.app.service.dto.ContactUrgenceDTO;
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
 * REST controller for managing {@link com.inscription.app.domain.ContactUrgence}.
 */
@RestController
@RequestMapping("/api/contact-urgences")
public class ContactUrgenceResource {

    private final Logger log = LoggerFactory.getLogger(ContactUrgenceResource.class);

    private static final String ENTITY_NAME = "contactUrgence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactUrgenceService contactUrgenceService;

    private final ContactUrgenceRepository contactUrgenceRepository;

    public ContactUrgenceResource(ContactUrgenceService contactUrgenceService, ContactUrgenceRepository contactUrgenceRepository) {
        this.contactUrgenceService = contactUrgenceService;
        this.contactUrgenceRepository = contactUrgenceRepository;
    }

    /**
     * {@code POST  /contact-urgences} : Create a new contactUrgence.
     *
     * @param contactUrgenceDTO the contactUrgenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactUrgenceDTO, or with status {@code 400 (Bad Request)} if the contactUrgence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContactUrgenceDTO> createContactUrgence(@Valid @RequestBody ContactUrgenceDTO contactUrgenceDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContactUrgence : {}", contactUrgenceDTO);
        if (contactUrgenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactUrgence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(contactUrgenceDTO.getCandidat())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        ContactUrgenceDTO result = contactUrgenceService.save(contactUrgenceDTO);
        return ResponseEntity
            .created(new URI("/api/contact-urgences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-urgences/:id} : Updates an existing contactUrgence.
     *
     * @param id the id of the contactUrgenceDTO to save.
     * @param contactUrgenceDTO the contactUrgenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactUrgenceDTO,
     * or with status {@code 400 (Bad Request)} if the contactUrgenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactUrgenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContactUrgenceDTO> updateContactUrgence(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactUrgenceDTO contactUrgenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactUrgence : {}, {}", id, contactUrgenceDTO);
        if (contactUrgenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactUrgenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactUrgenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactUrgenceDTO result = contactUrgenceService.update(contactUrgenceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactUrgenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contact-urgences/:id} : Partial updates given fields of an existing contactUrgence, field will ignore if it is null
     *
     * @param id the id of the contactUrgenceDTO to save.
     * @param contactUrgenceDTO the contactUrgenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactUrgenceDTO,
     * or with status {@code 400 (Bad Request)} if the contactUrgenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactUrgenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactUrgenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactUrgenceDTO> partialUpdateContactUrgence(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactUrgenceDTO contactUrgenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactUrgence partially : {}, {}", id, contactUrgenceDTO);
        if (contactUrgenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactUrgenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactUrgenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactUrgenceDTO> result = contactUrgenceService.partialUpdate(contactUrgenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactUrgenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-urgences} : get all the contactUrgences.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactUrgences in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContactUrgenceDTO>> getAllContactUrgences(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ContactUrgences");
        Page<ContactUrgenceDTO> page = contactUrgenceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contact-urgences/:id} : get the "id" contactUrgence.
     *
     * @param id the id of the contactUrgenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactUrgenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContactUrgenceDTO> getContactUrgence(@PathVariable("id") Long id) {
        log.debug("REST request to get ContactUrgence : {}", id);
        Optional<ContactUrgenceDTO> contactUrgenceDTO = contactUrgenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactUrgenceDTO);
    }

    /**
     * {@code DELETE  /contact-urgences/:id} : delete the "id" contactUrgence.
     *
     * @param id the id of the contactUrgenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactUrgence(@PathVariable("id") Long id) {
        log.debug("REST request to delete ContactUrgence : {}", id);
        contactUrgenceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
