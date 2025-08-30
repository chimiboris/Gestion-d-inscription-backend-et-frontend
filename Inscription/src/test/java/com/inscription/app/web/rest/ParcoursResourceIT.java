package com.inscription.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.inscription.app.IntegrationTest;
import com.inscription.app.domain.Candidat;
import com.inscription.app.domain.Parcours;
import com.inscription.app.domain.enumeration.NiveauEtude;
import com.inscription.app.repository.ParcoursRepository;
import com.inscription.app.service.dto.ParcoursDTO;
import com.inscription.app.service.mapper.ParcoursMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ParcoursResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParcoursResourceIT {

    private static final String DEFAULT_ETABLISSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_ETABLISSEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIALISATION = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALISATION = "BBBBBBBBBB";

    private static final NiveauEtude DEFAULT_NIVEAU = NiveauEtude.BACCALAUREAT;
    private static final NiveauEtude UPDATED_NIVEAU = NiveauEtude.LICENCE;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/parcours";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParcoursRepository parcoursRepository;

    @Autowired
    private ParcoursMapper parcoursMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParcoursMockMvc;

    private Parcours parcours;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parcours createEntity(EntityManager em) {
        Parcours parcours = new Parcours()
            .etablissement(DEFAULT_ETABLISSEMENT)
            .specialisation(DEFAULT_SPECIALISATION)
            .niveau(DEFAULT_NIVEAU)
            .commentaire(DEFAULT_COMMENTAIRE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN);
        // Add required entity
        Candidat candidat;
        if (TestUtil.findAll(em, Candidat.class).isEmpty()) {
            candidat = CandidatResourceIT.createEntity(em);
            em.persist(candidat);
            em.flush();
        } else {
            candidat = TestUtil.findAll(em, Candidat.class).get(0);
        }
        parcours.setCandidat(candidat);
        return parcours;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parcours createUpdatedEntity(EntityManager em) {
        Parcours parcours = new Parcours()
            .etablissement(UPDATED_ETABLISSEMENT)
            .specialisation(UPDATED_SPECIALISATION)
            .niveau(UPDATED_NIVEAU)
            .commentaire(UPDATED_COMMENTAIRE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        // Add required entity
        Candidat candidat;
        if (TestUtil.findAll(em, Candidat.class).isEmpty()) {
            candidat = CandidatResourceIT.createUpdatedEntity(em);
            em.persist(candidat);
            em.flush();
        } else {
            candidat = TestUtil.findAll(em, Candidat.class).get(0);
        }
        parcours.setCandidat(candidat);
        return parcours;
    }

    @BeforeEach
    public void initTest() {
        parcours = createEntity(em);
    }

    @Test
    @Transactional
    void createParcours() throws Exception {
        int databaseSizeBeforeCreate = parcoursRepository.findAll().size();
        // Create the Parcours
        ParcoursDTO parcoursDTO = parcoursMapper.toDto(parcours);
        restParcoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcoursDTO)))
            .andExpect(status().isCreated());

        // Validate the Parcours in the database
        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeCreate + 1);
        Parcours testParcours = parcoursList.get(parcoursList.size() - 1);
        assertThat(testParcours.getEtablissement()).isEqualTo(DEFAULT_ETABLISSEMENT);
        assertThat(testParcours.getSpecialisation()).isEqualTo(DEFAULT_SPECIALISATION);
        assertThat(testParcours.getNiveau()).isEqualTo(DEFAULT_NIVEAU);
        assertThat(testParcours.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testParcours.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testParcours.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void createParcoursWithExistingId() throws Exception {
        // Create the Parcours with an existing ID
        parcours.setId(1L);
        ParcoursDTO parcoursDTO = parcoursMapper.toDto(parcours);

        int databaseSizeBeforeCreate = parcoursRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParcoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcoursDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Parcours in the database
        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEtablissementIsRequired() throws Exception {
        int databaseSizeBeforeTest = parcoursRepository.findAll().size();
        // set the field null
        parcours.setEtablissement(null);

        // Create the Parcours, which fails.
        ParcoursDTO parcoursDTO = parcoursMapper.toDto(parcours);

        restParcoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcoursDTO)))
            .andExpect(status().isBadRequest());

        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpecialisationIsRequired() throws Exception {
        int databaseSizeBeforeTest = parcoursRepository.findAll().size();
        // set the field null
        parcours.setSpecialisation(null);

        // Create the Parcours, which fails.
        ParcoursDTO parcoursDTO = parcoursMapper.toDto(parcours);

        restParcoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcoursDTO)))
            .andExpect(status().isBadRequest());

        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNiveauIsRequired() throws Exception {
        int databaseSizeBeforeTest = parcoursRepository.findAll().size();
        // set the field null
        parcours.setNiveau(null);

        // Create the Parcours, which fails.
        ParcoursDTO parcoursDTO = parcoursMapper.toDto(parcours);

        restParcoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcoursDTO)))
            .andExpect(status().isBadRequest());

        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = parcoursRepository.findAll().size();
        // set the field null
        parcours.setDateDebut(null);

        // Create the Parcours, which fails.
        ParcoursDTO parcoursDTO = parcoursMapper.toDto(parcours);

        restParcoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcoursDTO)))
            .andExpect(status().isBadRequest());

        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = parcoursRepository.findAll().size();
        // set the field null
        parcours.setDateFin(null);

        // Create the Parcours, which fails.
        ParcoursDTO parcoursDTO = parcoursMapper.toDto(parcours);

        restParcoursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcoursDTO)))
            .andExpect(status().isBadRequest());

        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParcours() throws Exception {
        // Initialize the database
        parcoursRepository.saveAndFlush(parcours);

        // Get all the parcoursList
        restParcoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parcours.getId().intValue())))
            .andExpect(jsonPath("$.[*].etablissement").value(hasItem(DEFAULT_ETABLISSEMENT)))
            .andExpect(jsonPath("$.[*].specialisation").value(hasItem(DEFAULT_SPECIALISATION)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    void getParcours() throws Exception {
        // Initialize the database
        parcoursRepository.saveAndFlush(parcours);

        // Get the parcours
        restParcoursMockMvc
            .perform(get(ENTITY_API_URL_ID, parcours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parcours.getId().intValue()))
            .andExpect(jsonPath("$.etablissement").value(DEFAULT_ETABLISSEMENT))
            .andExpect(jsonPath("$.specialisation").value(DEFAULT_SPECIALISATION))
            .andExpect(jsonPath("$.niveau").value(DEFAULT_NIVEAU.toString()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingParcours() throws Exception {
        // Get the parcours
        restParcoursMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParcours() throws Exception {
        // Initialize the database
        parcoursRepository.saveAndFlush(parcours);

        int databaseSizeBeforeUpdate = parcoursRepository.findAll().size();

        // Update the parcours
        Parcours updatedParcours = parcoursRepository.findById(parcours.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedParcours are not directly saved in db
        em.detach(updatedParcours);
        updatedParcours
            .etablissement(UPDATED_ETABLISSEMENT)
            .specialisation(UPDATED_SPECIALISATION)
            .niveau(UPDATED_NIVEAU)
            .commentaire(UPDATED_COMMENTAIRE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        ParcoursDTO parcoursDTO = parcoursMapper.toDto(updatedParcours);

        restParcoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parcoursDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parcoursDTO))
            )
            .andExpect(status().isOk());

        // Validate the Parcours in the database
        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeUpdate);
        Parcours testParcours = parcoursList.get(parcoursList.size() - 1);
        assertThat(testParcours.getEtablissement()).isEqualTo(UPDATED_ETABLISSEMENT);
        assertThat(testParcours.getSpecialisation()).isEqualTo(UPDATED_SPECIALISATION);
        assertThat(testParcours.getNiveau()).isEqualTo(UPDATED_NIVEAU);
        assertThat(testParcours.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testParcours.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testParcours.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void putNonExistingParcours() throws Exception {
        int databaseSizeBeforeUpdate = parcoursRepository.findAll().size();
        parcours.setId(longCount.incrementAndGet());

        // Create the Parcours
        ParcoursDTO parcoursDTO = parcoursMapper.toDto(parcours);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParcoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parcoursDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parcoursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parcours in the database
        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParcours() throws Exception {
        int databaseSizeBeforeUpdate = parcoursRepository.findAll().size();
        parcours.setId(longCount.incrementAndGet());

        // Create the Parcours
        ParcoursDTO parcoursDTO = parcoursMapper.toDto(parcours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParcoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parcoursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parcours in the database
        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParcours() throws Exception {
        int databaseSizeBeforeUpdate = parcoursRepository.findAll().size();
        parcours.setId(longCount.incrementAndGet());

        // Create the Parcours
        ParcoursDTO parcoursDTO = parcoursMapper.toDto(parcours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParcoursMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcoursDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parcours in the database
        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParcoursWithPatch() throws Exception {
        // Initialize the database
        parcoursRepository.saveAndFlush(parcours);

        int databaseSizeBeforeUpdate = parcoursRepository.findAll().size();

        // Update the parcours using partial update
        Parcours partialUpdatedParcours = new Parcours();
        partialUpdatedParcours.setId(parcours.getId());

        partialUpdatedParcours.specialisation(UPDATED_SPECIALISATION);

        restParcoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParcours.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParcours))
            )
            .andExpect(status().isOk());

        // Validate the Parcours in the database
        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeUpdate);
        Parcours testParcours = parcoursList.get(parcoursList.size() - 1);
        assertThat(testParcours.getEtablissement()).isEqualTo(DEFAULT_ETABLISSEMENT);
        assertThat(testParcours.getSpecialisation()).isEqualTo(UPDATED_SPECIALISATION);
        assertThat(testParcours.getNiveau()).isEqualTo(DEFAULT_NIVEAU);
        assertThat(testParcours.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testParcours.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testParcours.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void fullUpdateParcoursWithPatch() throws Exception {
        // Initialize the database
        parcoursRepository.saveAndFlush(parcours);

        int databaseSizeBeforeUpdate = parcoursRepository.findAll().size();

        // Update the parcours using partial update
        Parcours partialUpdatedParcours = new Parcours();
        partialUpdatedParcours.setId(parcours.getId());

        partialUpdatedParcours
            .etablissement(UPDATED_ETABLISSEMENT)
            .specialisation(UPDATED_SPECIALISATION)
            .niveau(UPDATED_NIVEAU)
            .commentaire(UPDATED_COMMENTAIRE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restParcoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParcours.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParcours))
            )
            .andExpect(status().isOk());

        // Validate the Parcours in the database
        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeUpdate);
        Parcours testParcours = parcoursList.get(parcoursList.size() - 1);
        assertThat(testParcours.getEtablissement()).isEqualTo(UPDATED_ETABLISSEMENT);
        assertThat(testParcours.getSpecialisation()).isEqualTo(UPDATED_SPECIALISATION);
        assertThat(testParcours.getNiveau()).isEqualTo(UPDATED_NIVEAU);
        assertThat(testParcours.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testParcours.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testParcours.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingParcours() throws Exception {
        int databaseSizeBeforeUpdate = parcoursRepository.findAll().size();
        parcours.setId(longCount.incrementAndGet());

        // Create the Parcours
        ParcoursDTO parcoursDTO = parcoursMapper.toDto(parcours);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParcoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parcoursDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parcoursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parcours in the database
        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParcours() throws Exception {
        int databaseSizeBeforeUpdate = parcoursRepository.findAll().size();
        parcours.setId(longCount.incrementAndGet());

        // Create the Parcours
        ParcoursDTO parcoursDTO = parcoursMapper.toDto(parcours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParcoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parcoursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parcours in the database
        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParcours() throws Exception {
        int databaseSizeBeforeUpdate = parcoursRepository.findAll().size();
        parcours.setId(longCount.incrementAndGet());

        // Create the Parcours
        ParcoursDTO parcoursDTO = parcoursMapper.toDto(parcours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParcoursMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parcoursDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parcours in the database
        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParcours() throws Exception {
        // Initialize the database
        parcoursRepository.saveAndFlush(parcours);

        int databaseSizeBeforeDelete = parcoursRepository.findAll().size();

        // Delete the parcours
        restParcoursMockMvc
            .perform(delete(ENTITY_API_URL_ID, parcours.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parcours> parcoursList = parcoursRepository.findAll();
        assertThat(parcoursList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
