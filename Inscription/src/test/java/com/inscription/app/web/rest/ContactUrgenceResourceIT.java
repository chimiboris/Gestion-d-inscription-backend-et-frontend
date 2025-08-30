package com.inscription.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.inscription.app.IntegrationTest;
import com.inscription.app.domain.Candidat;
import com.inscription.app.domain.ContactUrgence;
import com.inscription.app.repository.ContactUrgenceRepository;
import com.inscription.app.service.dto.ContactUrgenceDTO;
import com.inscription.app.service.mapper.ContactUrgenceMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ContactUrgenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactUrgenceResourceIT {

    private static final String DEFAULT_NOM_COMPLET = "AAAAAAAAAA";
    private static final String UPDATED_NOM_COMPLET = "BBBBBBBBBB";

    private static final String DEFAULT_LIEN_PARENTE = "AAAAAAAAAA";
    private static final String UPDATED_LIEN_PARENTE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contact-urgences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactUrgenceRepository contactUrgenceRepository;

    @Autowired
    private ContactUrgenceMapper contactUrgenceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactUrgenceMockMvc;

    private ContactUrgence contactUrgence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactUrgence createEntity(EntityManager em) {
        ContactUrgence contactUrgence = new ContactUrgence()
            .nomComplet(DEFAULT_NOM_COMPLET)
            .lienParente(DEFAULT_LIEN_PARENTE)
            .telephone(DEFAULT_TELEPHONE)
            .email(DEFAULT_EMAIL);
        // Add required entity
        Candidat candidat;
        if (TestUtil.findAll(em, Candidat.class).isEmpty()) {
            candidat = CandidatResourceIT.createEntity(em);
            em.persist(candidat);
            em.flush();
        } else {
            candidat = TestUtil.findAll(em, Candidat.class).get(0);
        }
        contactUrgence.setCandidat(candidat);
        return contactUrgence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactUrgence createUpdatedEntity(EntityManager em) {
        ContactUrgence contactUrgence = new ContactUrgence()
            .nomComplet(UPDATED_NOM_COMPLET)
            .lienParente(UPDATED_LIEN_PARENTE)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL);
        // Add required entity
        Candidat candidat;
        candidat = CandidatResourceIT.createUpdatedEntity(em);
        em.persist(candidat);
        em.flush();
        contactUrgence.setCandidat(candidat);
        return contactUrgence;
    }

    @BeforeEach
    public void initTest() {
        contactUrgence = createEntity(em);
    }

    @Test
    @Transactional
    void createContactUrgence() throws Exception {
        int databaseSizeBeforeCreate = contactUrgenceRepository.findAll().size();
        // Create the ContactUrgence
        ContactUrgenceDTO contactUrgenceDTO = contactUrgenceMapper.toDto(contactUrgence);
        restContactUrgenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactUrgenceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactUrgence in the database
        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeCreate + 1);
        ContactUrgence testContactUrgence = contactUrgenceList.get(contactUrgenceList.size() - 1);
        assertThat(testContactUrgence.getNomComplet()).isEqualTo(DEFAULT_NOM_COMPLET);
        assertThat(testContactUrgence.getLienParente()).isEqualTo(DEFAULT_LIEN_PARENTE);
        assertThat(testContactUrgence.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testContactUrgence.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the id for MapsId, the ids must be same
        assertThat(testContactUrgence.getId()).isEqualTo(contactUrgenceDTO.getCandidat().getId());
    }

    @Test
    @Transactional
    void createContactUrgenceWithExistingId() throws Exception {
        // Create the ContactUrgence with an existing ID
        contactUrgence.setId(1L);
        ContactUrgenceDTO contactUrgenceDTO = contactUrgenceMapper.toDto(contactUrgence);

        int databaseSizeBeforeCreate = contactUrgenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactUrgenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactUrgenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactUrgence in the database
        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updateContactUrgenceMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        contactUrgenceRepository.saveAndFlush(contactUrgence);
        int databaseSizeBeforeCreate = contactUrgenceRepository.findAll().size();
        // Add a new parent entity
        Candidat candidat = CandidatResourceIT.createUpdatedEntity(em);
        em.persist(candidat);
        em.flush();

        // Load the contactUrgence
        ContactUrgence updatedContactUrgence = contactUrgenceRepository.findById(contactUrgence.getId()).orElseThrow();
        assertThat(updatedContactUrgence).isNotNull();
        // Disconnect from session so that the updates on updatedContactUrgence are not directly saved in db
        em.detach(updatedContactUrgence);

        // Update the Candidat with new association value
        updatedContactUrgence.setCandidat(candidat);
        ContactUrgenceDTO updatedContactUrgenceDTO = contactUrgenceMapper.toDto(updatedContactUrgence);
        assertThat(updatedContactUrgenceDTO).isNotNull();

        // Update the entity
        restContactUrgenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContactUrgenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContactUrgenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactUrgence in the database
        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeCreate);
        ContactUrgence testContactUrgence = contactUrgenceList.get(contactUrgenceList.size() - 1);
        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testContactUrgence.getId()).isEqualTo(testContactUrgence.getCandidat().getId());
    }

    @Test
    @Transactional
    void checkNomCompletIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactUrgenceRepository.findAll().size();
        // set the field null
        contactUrgence.setNomComplet(null);

        // Create the ContactUrgence, which fails.
        ContactUrgenceDTO contactUrgenceDTO = contactUrgenceMapper.toDto(contactUrgence);

        restContactUrgenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactUrgenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLienParenteIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactUrgenceRepository.findAll().size();
        // set the field null
        contactUrgence.setLienParente(null);

        // Create the ContactUrgence, which fails.
        ContactUrgenceDTO contactUrgenceDTO = contactUrgenceMapper.toDto(contactUrgence);

        restContactUrgenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactUrgenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactUrgenceRepository.findAll().size();
        // set the field null
        contactUrgence.setTelephone(null);

        // Create the ContactUrgence, which fails.
        ContactUrgenceDTO contactUrgenceDTO = contactUrgenceMapper.toDto(contactUrgence);

        restContactUrgenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactUrgenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContactUrgences() throws Exception {
        // Initialize the database
        contactUrgenceRepository.saveAndFlush(contactUrgence);

        // Get all the contactUrgenceList
        restContactUrgenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactUrgence.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomComplet").value(hasItem(DEFAULT_NOM_COMPLET)))
            .andExpect(jsonPath("$.[*].lienParente").value(hasItem(DEFAULT_LIEN_PARENTE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getContactUrgence() throws Exception {
        // Initialize the database
        contactUrgenceRepository.saveAndFlush(contactUrgence);

        // Get the contactUrgence
        restContactUrgenceMockMvc
            .perform(get(ENTITY_API_URL_ID, contactUrgence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactUrgence.getId().intValue()))
            .andExpect(jsonPath("$.nomComplet").value(DEFAULT_NOM_COMPLET))
            .andExpect(jsonPath("$.lienParente").value(DEFAULT_LIEN_PARENTE))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingContactUrgence() throws Exception {
        // Get the contactUrgence
        restContactUrgenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContactUrgence() throws Exception {
        // Initialize the database
        contactUrgenceRepository.saveAndFlush(contactUrgence);

        int databaseSizeBeforeUpdate = contactUrgenceRepository.findAll().size();

        // Update the contactUrgence
        ContactUrgence updatedContactUrgence = contactUrgenceRepository.findById(contactUrgence.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContactUrgence are not directly saved in db
        em.detach(updatedContactUrgence);
        updatedContactUrgence
            .nomComplet(UPDATED_NOM_COMPLET)
            .lienParente(UPDATED_LIEN_PARENTE)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL);
        ContactUrgenceDTO contactUrgenceDTO = contactUrgenceMapper.toDto(updatedContactUrgence);

        restContactUrgenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactUrgenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactUrgenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactUrgence in the database
        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeUpdate);
        ContactUrgence testContactUrgence = contactUrgenceList.get(contactUrgenceList.size() - 1);
        assertThat(testContactUrgence.getNomComplet()).isEqualTo(UPDATED_NOM_COMPLET);
        assertThat(testContactUrgence.getLienParente()).isEqualTo(UPDATED_LIEN_PARENTE);
        assertThat(testContactUrgence.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testContactUrgence.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingContactUrgence() throws Exception {
        int databaseSizeBeforeUpdate = contactUrgenceRepository.findAll().size();
        contactUrgence.setId(longCount.incrementAndGet());

        // Create the ContactUrgence
        ContactUrgenceDTO contactUrgenceDTO = contactUrgenceMapper.toDto(contactUrgence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactUrgenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactUrgenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactUrgenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactUrgence in the database
        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactUrgence() throws Exception {
        int databaseSizeBeforeUpdate = contactUrgenceRepository.findAll().size();
        contactUrgence.setId(longCount.incrementAndGet());

        // Create the ContactUrgence
        ContactUrgenceDTO contactUrgenceDTO = contactUrgenceMapper.toDto(contactUrgence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactUrgenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactUrgenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactUrgence in the database
        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactUrgence() throws Exception {
        int databaseSizeBeforeUpdate = contactUrgenceRepository.findAll().size();
        contactUrgence.setId(longCount.incrementAndGet());

        // Create the ContactUrgence
        ContactUrgenceDTO contactUrgenceDTO = contactUrgenceMapper.toDto(contactUrgence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactUrgenceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactUrgenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactUrgence in the database
        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactUrgenceWithPatch() throws Exception {
        // Initialize the database
        contactUrgenceRepository.saveAndFlush(contactUrgence);

        int databaseSizeBeforeUpdate = contactUrgenceRepository.findAll().size();

        // Update the contactUrgence using partial update
        ContactUrgence partialUpdatedContactUrgence = new ContactUrgence();
        partialUpdatedContactUrgence.setId(contactUrgence.getId());

        partialUpdatedContactUrgence.nomComplet(UPDATED_NOM_COMPLET).lienParente(UPDATED_LIEN_PARENTE);

        restContactUrgenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactUrgence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactUrgence))
            )
            .andExpect(status().isOk());

        // Validate the ContactUrgence in the database
        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeUpdate);
        ContactUrgence testContactUrgence = contactUrgenceList.get(contactUrgenceList.size() - 1);
        assertThat(testContactUrgence.getNomComplet()).isEqualTo(UPDATED_NOM_COMPLET);
        assertThat(testContactUrgence.getLienParente()).isEqualTo(UPDATED_LIEN_PARENTE);
        assertThat(testContactUrgence.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testContactUrgence.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateContactUrgenceWithPatch() throws Exception {
        // Initialize the database
        contactUrgenceRepository.saveAndFlush(contactUrgence);

        int databaseSizeBeforeUpdate = contactUrgenceRepository.findAll().size();

        // Update the contactUrgence using partial update
        ContactUrgence partialUpdatedContactUrgence = new ContactUrgence();
        partialUpdatedContactUrgence.setId(contactUrgence.getId());

        partialUpdatedContactUrgence
            .nomComplet(UPDATED_NOM_COMPLET)
            .lienParente(UPDATED_LIEN_PARENTE)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL);

        restContactUrgenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactUrgence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactUrgence))
            )
            .andExpect(status().isOk());

        // Validate the ContactUrgence in the database
        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeUpdate);
        ContactUrgence testContactUrgence = contactUrgenceList.get(contactUrgenceList.size() - 1);
        assertThat(testContactUrgence.getNomComplet()).isEqualTo(UPDATED_NOM_COMPLET);
        assertThat(testContactUrgence.getLienParente()).isEqualTo(UPDATED_LIEN_PARENTE);
        assertThat(testContactUrgence.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testContactUrgence.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingContactUrgence() throws Exception {
        int databaseSizeBeforeUpdate = contactUrgenceRepository.findAll().size();
        contactUrgence.setId(longCount.incrementAndGet());

        // Create the ContactUrgence
        ContactUrgenceDTO contactUrgenceDTO = contactUrgenceMapper.toDto(contactUrgence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactUrgenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactUrgenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactUrgenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactUrgence in the database
        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactUrgence() throws Exception {
        int databaseSizeBeforeUpdate = contactUrgenceRepository.findAll().size();
        contactUrgence.setId(longCount.incrementAndGet());

        // Create the ContactUrgence
        ContactUrgenceDTO contactUrgenceDTO = contactUrgenceMapper.toDto(contactUrgence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactUrgenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactUrgenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactUrgence in the database
        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactUrgence() throws Exception {
        int databaseSizeBeforeUpdate = contactUrgenceRepository.findAll().size();
        contactUrgence.setId(longCount.incrementAndGet());

        // Create the ContactUrgence
        ContactUrgenceDTO contactUrgenceDTO = contactUrgenceMapper.toDto(contactUrgence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactUrgenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactUrgenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactUrgence in the database
        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactUrgence() throws Exception {
        // Initialize the database
        contactUrgenceRepository.saveAndFlush(contactUrgence);

        int databaseSizeBeforeDelete = contactUrgenceRepository.findAll().size();

        // Delete the contactUrgence
        restContactUrgenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactUrgence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactUrgence> contactUrgenceList = contactUrgenceRepository.findAll();
        assertThat(contactUrgenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
