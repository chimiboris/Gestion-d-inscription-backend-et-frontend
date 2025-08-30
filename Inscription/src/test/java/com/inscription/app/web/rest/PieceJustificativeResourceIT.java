package com.inscription.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.inscription.app.IntegrationTest;
import com.inscription.app.domain.Candidat;
import com.inscription.app.domain.PieceJustificative;
import com.inscription.app.domain.enumeration.Type;
import com.inscription.app.repository.PieceJustificativeRepository;
import com.inscription.app.service.dto.PieceJustificativeDTO;
import com.inscription.app.service.mapper.PieceJustificativeMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PieceJustificativeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PieceJustificativeResourceIT {

    private static final Type DEFAULT_TYPE = Type.CNI_RECTO;
    private static final Type UPDATED_TYPE = Type.CNI_VERSO;

    private static final byte[] DEFAULT_FICHIER = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FICHIER_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_DATE_UPLOAD = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPLOAD = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_VALIDE = false;
    private static final Boolean UPDATED_VALIDE = true;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/piece-justificatives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PieceJustificativeRepository pieceJustificativeRepository;

    @Autowired
    private PieceJustificativeMapper pieceJustificativeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPieceJustificativeMockMvc;

    private PieceJustificative pieceJustificative;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PieceJustificative createEntity(EntityManager em) {
        PieceJustificative pieceJustificative = new PieceJustificative()
            .type(DEFAULT_TYPE)
            .fichier(DEFAULT_FICHIER)
            .fichierContentType(DEFAULT_FICHIER_CONTENT_TYPE)
            .dateUpload(DEFAULT_DATE_UPLOAD)
            .valide(DEFAULT_VALIDE)
            .commentaire(DEFAULT_COMMENTAIRE);
        // Add required entity
        Candidat candidat;
        if (TestUtil.findAll(em, Candidat.class).isEmpty()) {
            candidat = CandidatResourceIT.createEntity(em);
            em.persist(candidat);
            em.flush();
        } else {
            candidat = TestUtil.findAll(em, Candidat.class).get(0);
        }
        pieceJustificative.setCandidat(candidat);
        return pieceJustificative;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PieceJustificative createUpdatedEntity(EntityManager em) {
        PieceJustificative pieceJustificative = new PieceJustificative()
            .type(UPDATED_TYPE)
            .fichier(UPDATED_FICHIER)
            .fichierContentType(UPDATED_FICHIER_CONTENT_TYPE)
            .dateUpload(UPDATED_DATE_UPLOAD)
            .valide(UPDATED_VALIDE)
            .commentaire(UPDATED_COMMENTAIRE);
        // Add required entity
        Candidat candidat;
        if (TestUtil.findAll(em, Candidat.class).isEmpty()) {
            candidat = CandidatResourceIT.createUpdatedEntity(em);
            em.persist(candidat);
            em.flush();
        } else {
            candidat = TestUtil.findAll(em, Candidat.class).get(0);
        }
        pieceJustificative.setCandidat(candidat);
        return pieceJustificative;
    }

    @BeforeEach
    public void initTest() {
        pieceJustificative = createEntity(em);
    }

    @Test
    @Transactional
    void createPieceJustificative() throws Exception {
        int databaseSizeBeforeCreate = pieceJustificativeRepository.findAll().size();
        // Create the PieceJustificative
        PieceJustificativeDTO pieceJustificativeDTO = pieceJustificativeMapper.toDto(pieceJustificative);
        restPieceJustificativeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJustificativeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PieceJustificative in the database
        List<PieceJustificative> pieceJustificativeList = pieceJustificativeRepository.findAll();
        assertThat(pieceJustificativeList).hasSize(databaseSizeBeforeCreate + 1);
        PieceJustificative testPieceJustificative = pieceJustificativeList.get(pieceJustificativeList.size() - 1);
        assertThat(testPieceJustificative.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPieceJustificative.getFichier()).isEqualTo(DEFAULT_FICHIER);
        assertThat(testPieceJustificative.getFichierContentType()).isEqualTo(DEFAULT_FICHIER_CONTENT_TYPE);
        assertThat(testPieceJustificative.getDateUpload()).isEqualTo(DEFAULT_DATE_UPLOAD);
        assertThat(testPieceJustificative.getValide()).isEqualTo(DEFAULT_VALIDE);
        assertThat(testPieceJustificative.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    void createPieceJustificativeWithExistingId() throws Exception {
        // Create the PieceJustificative with an existing ID
        pieceJustificative.setId(1L);
        PieceJustificativeDTO pieceJustificativeDTO = pieceJustificativeMapper.toDto(pieceJustificative);

        int databaseSizeBeforeCreate = pieceJustificativeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPieceJustificativeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJustificativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJustificative in the database
        List<PieceJustificative> pieceJustificativeList = pieceJustificativeRepository.findAll();
        assertThat(pieceJustificativeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pieceJustificativeRepository.findAll().size();
        // set the field null
        pieceJustificative.setType(null);

        // Create the PieceJustificative, which fails.
        PieceJustificativeDTO pieceJustificativeDTO = pieceJustificativeMapper.toDto(pieceJustificative);

        restPieceJustificativeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJustificativeDTO))
            )
            .andExpect(status().isBadRequest());

        List<PieceJustificative> pieceJustificativeList = pieceJustificativeRepository.findAll();
        assertThat(pieceJustificativeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPieceJustificatives() throws Exception {
        // Initialize the database
        pieceJustificativeRepository.saveAndFlush(pieceJustificative);

        // Get all the pieceJustificativeList
        restPieceJustificativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pieceJustificative.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fichierContentType").value(hasItem(DEFAULT_FICHIER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichier").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FICHIER))))
            .andExpect(jsonPath("$.[*].dateUpload").value(hasItem(DEFAULT_DATE_UPLOAD.toString())))
            .andExpect(jsonPath("$.[*].valide").value(hasItem(DEFAULT_VALIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));
    }

    @Test
    @Transactional
    void getPieceJustificative() throws Exception {
        // Initialize the database
        pieceJustificativeRepository.saveAndFlush(pieceJustificative);

        // Get the pieceJustificative
        restPieceJustificativeMockMvc
            .perform(get(ENTITY_API_URL_ID, pieceJustificative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pieceJustificative.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.fichierContentType").value(DEFAULT_FICHIER_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichier").value(Base64.getEncoder().encodeToString(DEFAULT_FICHIER)))
            .andExpect(jsonPath("$.dateUpload").value(DEFAULT_DATE_UPLOAD.toString()))
            .andExpect(jsonPath("$.valide").value(DEFAULT_VALIDE.booleanValue()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE));
    }

    @Test
    @Transactional
    void getNonExistingPieceJustificative() throws Exception {
        // Get the pieceJustificative
        restPieceJustificativeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPieceJustificative() throws Exception {
        // Initialize the database
        pieceJustificativeRepository.saveAndFlush(pieceJustificative);

        int databaseSizeBeforeUpdate = pieceJustificativeRepository.findAll().size();

        // Update the pieceJustificative
        PieceJustificative updatedPieceJustificative = pieceJustificativeRepository.findById(pieceJustificative.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPieceJustificative are not directly saved in db
        em.detach(updatedPieceJustificative);
        updatedPieceJustificative
            .type(UPDATED_TYPE)
            .fichier(UPDATED_FICHIER)
            .fichierContentType(UPDATED_FICHIER_CONTENT_TYPE)
            .dateUpload(UPDATED_DATE_UPLOAD)
            .valide(UPDATED_VALIDE)
            .commentaire(UPDATED_COMMENTAIRE);
        PieceJustificativeDTO pieceJustificativeDTO = pieceJustificativeMapper.toDto(updatedPieceJustificative);

        restPieceJustificativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pieceJustificativeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJustificativeDTO))
            )
            .andExpect(status().isOk());

        // Validate the PieceJustificative in the database
        List<PieceJustificative> pieceJustificativeList = pieceJustificativeRepository.findAll();
        assertThat(pieceJustificativeList).hasSize(databaseSizeBeforeUpdate);
        PieceJustificative testPieceJustificative = pieceJustificativeList.get(pieceJustificativeList.size() - 1);
        assertThat(testPieceJustificative.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPieceJustificative.getFichier()).isEqualTo(UPDATED_FICHIER);
        assertThat(testPieceJustificative.getFichierContentType()).isEqualTo(UPDATED_FICHIER_CONTENT_TYPE);
        assertThat(testPieceJustificative.getDateUpload()).isEqualTo(UPDATED_DATE_UPLOAD);
        assertThat(testPieceJustificative.getValide()).isEqualTo(UPDATED_VALIDE);
        assertThat(testPieceJustificative.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void putNonExistingPieceJustificative() throws Exception {
        int databaseSizeBeforeUpdate = pieceJustificativeRepository.findAll().size();
        pieceJustificative.setId(longCount.incrementAndGet());

        // Create the PieceJustificative
        PieceJustificativeDTO pieceJustificativeDTO = pieceJustificativeMapper.toDto(pieceJustificative);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPieceJustificativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pieceJustificativeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJustificativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJustificative in the database
        List<PieceJustificative> pieceJustificativeList = pieceJustificativeRepository.findAll();
        assertThat(pieceJustificativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPieceJustificative() throws Exception {
        int databaseSizeBeforeUpdate = pieceJustificativeRepository.findAll().size();
        pieceJustificative.setId(longCount.incrementAndGet());

        // Create the PieceJustificative
        PieceJustificativeDTO pieceJustificativeDTO = pieceJustificativeMapper.toDto(pieceJustificative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJustificativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJustificativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJustificative in the database
        List<PieceJustificative> pieceJustificativeList = pieceJustificativeRepository.findAll();
        assertThat(pieceJustificativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPieceJustificative() throws Exception {
        int databaseSizeBeforeUpdate = pieceJustificativeRepository.findAll().size();
        pieceJustificative.setId(longCount.incrementAndGet());

        // Create the PieceJustificative
        PieceJustificativeDTO pieceJustificativeDTO = pieceJustificativeMapper.toDto(pieceJustificative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJustificativeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pieceJustificativeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PieceJustificative in the database
        List<PieceJustificative> pieceJustificativeList = pieceJustificativeRepository.findAll();
        assertThat(pieceJustificativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePieceJustificativeWithPatch() throws Exception {
        // Initialize the database
        pieceJustificativeRepository.saveAndFlush(pieceJustificative);

        int databaseSizeBeforeUpdate = pieceJustificativeRepository.findAll().size();

        // Update the pieceJustificative using partial update
        PieceJustificative partialUpdatedPieceJustificative = new PieceJustificative();
        partialUpdatedPieceJustificative.setId(pieceJustificative.getId());

        partialUpdatedPieceJustificative
            .fichier(UPDATED_FICHIER)
            .fichierContentType(UPDATED_FICHIER_CONTENT_TYPE)
            .valide(UPDATED_VALIDE)
            .commentaire(UPDATED_COMMENTAIRE);

        restPieceJustificativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPieceJustificative.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPieceJustificative))
            )
            .andExpect(status().isOk());

        // Validate the PieceJustificative in the database
        List<PieceJustificative> pieceJustificativeList = pieceJustificativeRepository.findAll();
        assertThat(pieceJustificativeList).hasSize(databaseSizeBeforeUpdate);
        PieceJustificative testPieceJustificative = pieceJustificativeList.get(pieceJustificativeList.size() - 1);
        assertThat(testPieceJustificative.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPieceJustificative.getFichier()).isEqualTo(UPDATED_FICHIER);
        assertThat(testPieceJustificative.getFichierContentType()).isEqualTo(UPDATED_FICHIER_CONTENT_TYPE);
        assertThat(testPieceJustificative.getDateUpload()).isEqualTo(DEFAULT_DATE_UPLOAD);
        assertThat(testPieceJustificative.getValide()).isEqualTo(UPDATED_VALIDE);
        assertThat(testPieceJustificative.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void fullUpdatePieceJustificativeWithPatch() throws Exception {
        // Initialize the database
        pieceJustificativeRepository.saveAndFlush(pieceJustificative);

        int databaseSizeBeforeUpdate = pieceJustificativeRepository.findAll().size();

        // Update the pieceJustificative using partial update
        PieceJustificative partialUpdatedPieceJustificative = new PieceJustificative();
        partialUpdatedPieceJustificative.setId(pieceJustificative.getId());

        partialUpdatedPieceJustificative
            .type(UPDATED_TYPE)
            .fichier(UPDATED_FICHIER)
            .fichierContentType(UPDATED_FICHIER_CONTENT_TYPE)
            .dateUpload(UPDATED_DATE_UPLOAD)
            .valide(UPDATED_VALIDE)
            .commentaire(UPDATED_COMMENTAIRE);

        restPieceJustificativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPieceJustificative.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPieceJustificative))
            )
            .andExpect(status().isOk());

        // Validate the PieceJustificative in the database
        List<PieceJustificative> pieceJustificativeList = pieceJustificativeRepository.findAll();
        assertThat(pieceJustificativeList).hasSize(databaseSizeBeforeUpdate);
        PieceJustificative testPieceJustificative = pieceJustificativeList.get(pieceJustificativeList.size() - 1);
        assertThat(testPieceJustificative.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPieceJustificative.getFichier()).isEqualTo(UPDATED_FICHIER);
        assertThat(testPieceJustificative.getFichierContentType()).isEqualTo(UPDATED_FICHIER_CONTENT_TYPE);
        assertThat(testPieceJustificative.getDateUpload()).isEqualTo(UPDATED_DATE_UPLOAD);
        assertThat(testPieceJustificative.getValide()).isEqualTo(UPDATED_VALIDE);
        assertThat(testPieceJustificative.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingPieceJustificative() throws Exception {
        int databaseSizeBeforeUpdate = pieceJustificativeRepository.findAll().size();
        pieceJustificative.setId(longCount.incrementAndGet());

        // Create the PieceJustificative
        PieceJustificativeDTO pieceJustificativeDTO = pieceJustificativeMapper.toDto(pieceJustificative);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPieceJustificativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pieceJustificativeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pieceJustificativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJustificative in the database
        List<PieceJustificative> pieceJustificativeList = pieceJustificativeRepository.findAll();
        assertThat(pieceJustificativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPieceJustificative() throws Exception {
        int databaseSizeBeforeUpdate = pieceJustificativeRepository.findAll().size();
        pieceJustificative.setId(longCount.incrementAndGet());

        // Create the PieceJustificative
        PieceJustificativeDTO pieceJustificativeDTO = pieceJustificativeMapper.toDto(pieceJustificative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJustificativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pieceJustificativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PieceJustificative in the database
        List<PieceJustificative> pieceJustificativeList = pieceJustificativeRepository.findAll();
        assertThat(pieceJustificativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPieceJustificative() throws Exception {
        int databaseSizeBeforeUpdate = pieceJustificativeRepository.findAll().size();
        pieceJustificative.setId(longCount.incrementAndGet());

        // Create the PieceJustificative
        PieceJustificativeDTO pieceJustificativeDTO = pieceJustificativeMapper.toDto(pieceJustificative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPieceJustificativeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pieceJustificativeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PieceJustificative in the database
        List<PieceJustificative> pieceJustificativeList = pieceJustificativeRepository.findAll();
        assertThat(pieceJustificativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePieceJustificative() throws Exception {
        // Initialize the database
        pieceJustificativeRepository.saveAndFlush(pieceJustificative);

        int databaseSizeBeforeDelete = pieceJustificativeRepository.findAll().size();

        // Delete the pieceJustificative
        restPieceJustificativeMockMvc
            .perform(delete(ENTITY_API_URL_ID, pieceJustificative.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PieceJustificative> pieceJustificativeList = pieceJustificativeRepository.findAll();
        assertThat(pieceJustificativeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
