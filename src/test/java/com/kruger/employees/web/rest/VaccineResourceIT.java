package com.kruger.employees.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kruger.employees.IntegrationTest;
import com.kruger.employees.domain.ApplicationUser;
import com.kruger.employees.domain.Vaccine;
import com.kruger.employees.domain.enumeration.VaccineType;
import com.kruger.employees.repository.VaccineRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VaccineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VaccineResourceIT {

    private static final VaccineType DEFAULT_VACCINE_TYPE = VaccineType.SPUTNIK;
    private static final VaccineType UPDATED_VACCINE_TYPE = VaccineType.AZTRAZENECA;

    private static final LocalDate DEFAULT_VACCINATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VACCINATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_DOSES = 1;
    private static final Integer UPDATED_DOSES = 2;

    private static final String ENTITY_API_URL = "/api/vaccines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVaccineMockMvc;

    private Vaccine vaccine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vaccine createEntity(EntityManager em) {
        Vaccine vaccine = new Vaccine().vaccineType(DEFAULT_VACCINE_TYPE).vaccinationDate(DEFAULT_VACCINATION_DATE).doses(DEFAULT_DOSES);
        // Add required entity
        ApplicationUser applicationUser;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            applicationUser = ApplicationUserResourceIT.createEntity(em);
            em.persist(applicationUser);
            em.flush();
        } else {
            applicationUser = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        vaccine.setApplicationUser(applicationUser);
        return vaccine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vaccine createUpdatedEntity(EntityManager em) {
        Vaccine vaccine = new Vaccine().vaccineType(UPDATED_VACCINE_TYPE).vaccinationDate(UPDATED_VACCINATION_DATE).doses(UPDATED_DOSES);
        // Add required entity
        ApplicationUser applicationUser;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            applicationUser = ApplicationUserResourceIT.createUpdatedEntity(em);
            em.persist(applicationUser);
            em.flush();
        } else {
            applicationUser = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        vaccine.setApplicationUser(applicationUser);
        return vaccine;
    }

    @BeforeEach
    public void initTest() {
        vaccine = createEntity(em);
    }

    @Test
    @Transactional
    void createVaccine() throws Exception {
        int databaseSizeBeforeCreate = vaccineRepository.findAll().size();
        // Create the Vaccine
        restVaccineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccine)))
            .andExpect(status().isCreated());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeCreate + 1);
        Vaccine testVaccine = vaccineList.get(vaccineList.size() - 1);
        assertThat(testVaccine.getVaccineType()).isEqualTo(DEFAULT_VACCINE_TYPE);
        assertThat(testVaccine.getVaccinationDate()).isEqualTo(DEFAULT_VACCINATION_DATE);
        assertThat(testVaccine.getDoses()).isEqualTo(DEFAULT_DOSES);
    }

    @Test
    @Transactional
    void createVaccineWithExistingId() throws Exception {
        // Create the Vaccine with an existing ID
        vaccine.setId(1L);

        int databaseSizeBeforeCreate = vaccineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVaccineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccine)))
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVaccineTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccineRepository.findAll().size();
        // set the field null
        vaccine.setVaccineType(null);

        // Create the Vaccine, which fails.

        restVaccineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccine)))
            .andExpect(status().isBadRequest());

        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVaccinationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccineRepository.findAll().size();
        // set the field null
        vaccine.setVaccinationDate(null);

        // Create the Vaccine, which fails.

        restVaccineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccine)))
            .andExpect(status().isBadRequest());

        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDosesIsRequired() throws Exception {
        int databaseSizeBeforeTest = vaccineRepository.findAll().size();
        // set the field null
        vaccine.setDoses(null);

        // Create the Vaccine, which fails.

        restVaccineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccine)))
            .andExpect(status().isBadRequest());

        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVaccines() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get all the vaccineList
        restVaccineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaccine.getId().intValue())))
            .andExpect(jsonPath("$.[*].vaccineType").value(hasItem(DEFAULT_VACCINE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].vaccinationDate").value(hasItem(DEFAULT_VACCINATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].doses").value(hasItem(DEFAULT_DOSES)));
    }

    @Test
    @Transactional
    void getVaccine() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        // Get the vaccine
        restVaccineMockMvc
            .perform(get(ENTITY_API_URL_ID, vaccine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vaccine.getId().intValue()))
            .andExpect(jsonPath("$.vaccineType").value(DEFAULT_VACCINE_TYPE.toString()))
            .andExpect(jsonPath("$.vaccinationDate").value(DEFAULT_VACCINATION_DATE.toString()))
            .andExpect(jsonPath("$.doses").value(DEFAULT_DOSES));
    }

    @Test
    @Transactional
    void getNonExistingVaccine() throws Exception {
        // Get the vaccine
        restVaccineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVaccine() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();

        // Update the vaccine
        Vaccine updatedVaccine = vaccineRepository.findById(vaccine.getId()).get();
        // Disconnect from session so that the updates on updatedVaccine are not directly saved in db
        em.detach(updatedVaccine);
        updatedVaccine.vaccineType(UPDATED_VACCINE_TYPE).vaccinationDate(UPDATED_VACCINATION_DATE).doses(UPDATED_DOSES);

        restVaccineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVaccine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVaccine))
            )
            .andExpect(status().isOk());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
        Vaccine testVaccine = vaccineList.get(vaccineList.size() - 1);
        assertThat(testVaccine.getVaccineType()).isEqualTo(UPDATED_VACCINE_TYPE);
        assertThat(testVaccine.getVaccinationDate()).isEqualTo(UPDATED_VACCINATION_DATE);
        assertThat(testVaccine.getDoses()).isEqualTo(UPDATED_DOSES);
    }

    @Test
    @Transactional
    void putNonExistingVaccine() throws Exception {
        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();
        vaccine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVaccine() throws Exception {
        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();
        vaccine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVaccine() throws Exception {
        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();
        vaccine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVaccineWithPatch() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();

        // Update the vaccine using partial update
        Vaccine partialUpdatedVaccine = new Vaccine();
        partialUpdatedVaccine.setId(vaccine.getId());

        partialUpdatedVaccine.vaccineType(UPDATED_VACCINE_TYPE).doses(UPDATED_DOSES);

        restVaccineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaccine))
            )
            .andExpect(status().isOk());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
        Vaccine testVaccine = vaccineList.get(vaccineList.size() - 1);
        assertThat(testVaccine.getVaccineType()).isEqualTo(UPDATED_VACCINE_TYPE);
        assertThat(testVaccine.getVaccinationDate()).isEqualTo(DEFAULT_VACCINATION_DATE);
        assertThat(testVaccine.getDoses()).isEqualTo(UPDATED_DOSES);
    }

    @Test
    @Transactional
    void fullUpdateVaccineWithPatch() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();

        // Update the vaccine using partial update
        Vaccine partialUpdatedVaccine = new Vaccine();
        partialUpdatedVaccine.setId(vaccine.getId());

        partialUpdatedVaccine.vaccineType(UPDATED_VACCINE_TYPE).vaccinationDate(UPDATED_VACCINATION_DATE).doses(UPDATED_DOSES);

        restVaccineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaccine))
            )
            .andExpect(status().isOk());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
        Vaccine testVaccine = vaccineList.get(vaccineList.size() - 1);
        assertThat(testVaccine.getVaccineType()).isEqualTo(UPDATED_VACCINE_TYPE);
        assertThat(testVaccine.getVaccinationDate()).isEqualTo(UPDATED_VACCINATION_DATE);
        assertThat(testVaccine.getDoses()).isEqualTo(UPDATED_DOSES);
    }

    @Test
    @Transactional
    void patchNonExistingVaccine() throws Exception {
        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();
        vaccine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vaccine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVaccine() throws Exception {
        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();
        vaccine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVaccine() throws Exception {
        int databaseSizeBeforeUpdate = vaccineRepository.findAll().size();
        vaccine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vaccine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vaccine in the database
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVaccine() throws Exception {
        // Initialize the database
        vaccineRepository.saveAndFlush(vaccine);

        int databaseSizeBeforeDelete = vaccineRepository.findAll().size();

        // Delete the vaccine
        restVaccineMockMvc
            .perform(delete(ENTITY_API_URL_ID, vaccine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vaccine> vaccineList = vaccineRepository.findAll();
        assertThat(vaccineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
