package ch.start.hack.web.rest;

import ch.start.hack.StartHack2019App;

import ch.start.hack.domain.Cup;
import ch.start.hack.repository.CupRepository;
import ch.start.hack.service.CupService;
import ch.start.hack.service.HistoryService;
import ch.start.hack.service.dto.CupDTO;
import ch.start.hack.service.mapper.CupMapper;
import ch.start.hack.service.mapper.HistoryMapper;
import ch.start.hack.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static ch.start.hack.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.start.hack.domain.enumeration.CupStatus;
/**
 * Test class for the CupResource REST controller.
 *
 * @see CupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartHack2019App.class)
public class CupResourceIntTest {

    private static final String DEFAULT_QR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_QR_CODE = "BBBBBBBBBB";

    private static final CupStatus DEFAULT_STATUS = CupStatus.InStore;
    private static final CupStatus UPDATED_STATUS = CupStatus.InUse;

    @Autowired
    private CupRepository cupRepository;

    @Autowired
    private CupMapper cupMapper;

    @Autowired
    private HistoryMapper historyMapper;

    @Autowired
    private CupService cupService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCupMockMvc;

    private Cup cup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CupResource cupResource = new CupResource(cupService, historyService, cupMapper, historyMapper);
        this.restCupMockMvc = MockMvcBuilders.standaloneSetup(cupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cup createEntity(EntityManager em) {
        Cup cup = new Cup()
            .qrCode(DEFAULT_QR_CODE)
            .status(DEFAULT_STATUS);
        return cup;
    }

    @Before
    public void initTest() {
        cup = createEntity(em);
    }

    @Test
    @Transactional
    public void createCup() throws Exception {
        int databaseSizeBeforeCreate = cupRepository.findAll().size();

        // Create the Cup
        CupDTO cupDTO = cupMapper.toDto(cup);
        restCupMockMvc.perform(post("/api/cups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cupDTO)))
            .andExpect(status().isCreated());

        // Validate the Cup in the database
        List<Cup> cupList = cupRepository.findAll();
        assertThat(cupList).hasSize(databaseSizeBeforeCreate + 1);
        Cup testCup = cupList.get(cupList.size() - 1);
        assertThat(testCup.getQrCode()).isEqualTo(DEFAULT_QR_CODE);
        assertThat(testCup.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createCupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cupRepository.findAll().size();

        // Create the Cup with an existing ID
        cup.setId(1L);
        CupDTO cupDTO = cupMapper.toDto(cup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCupMockMvc.perform(post("/api/cups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cup in the database
        List<Cup> cupList = cupRepository.findAll();
        assertThat(cupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCups() throws Exception {
        // Initialize the database
        cupRepository.saveAndFlush(cup);

        // Get all the cupList
        restCupMockMvc.perform(get("/api/cups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cup.getId().intValue())))
            .andExpect(jsonPath("$.[*].qrCode").value(hasItem(DEFAULT_QR_CODE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getCup() throws Exception {
        // Initialize the database
        cupRepository.saveAndFlush(cup);

        // Get the cup
        restCupMockMvc.perform(get("/api/cups/{id}", cup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cup.getId().intValue()))
            .andExpect(jsonPath("$.qrCode").value(DEFAULT_QR_CODE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCup() throws Exception {
        // Get the cup
        restCupMockMvc.perform(get("/api/cups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCup() throws Exception {
        // Initialize the database
        cupRepository.saveAndFlush(cup);

        int databaseSizeBeforeUpdate = cupRepository.findAll().size();

        // Update the cup
        Cup updatedCup = cupRepository.findById(cup.getId()).get();
        // Disconnect from session so that the updates on updatedCup are not directly saved in db
        em.detach(updatedCup);
        updatedCup
            .qrCode(UPDATED_QR_CODE)
            .status(UPDATED_STATUS);
        CupDTO cupDTO = cupMapper.toDto(updatedCup);

        restCupMockMvc.perform(put("/api/cups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cupDTO)))
            .andExpect(status().isOk());

        // Validate the Cup in the database
        List<Cup> cupList = cupRepository.findAll();
        assertThat(cupList).hasSize(databaseSizeBeforeUpdate);
        Cup testCup = cupList.get(cupList.size() - 1);
        assertThat(testCup.getQrCode()).isEqualTo(UPDATED_QR_CODE);
        assertThat(testCup.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingCup() throws Exception {
        int databaseSizeBeforeUpdate = cupRepository.findAll().size();

        // Create the Cup
        CupDTO cupDTO = cupMapper.toDto(cup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCupMockMvc.perform(put("/api/cups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cup in the database
        List<Cup> cupList = cupRepository.findAll();
        assertThat(cupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCup() throws Exception {
        // Initialize the database
        cupRepository.saveAndFlush(cup);

        int databaseSizeBeforeDelete = cupRepository.findAll().size();

        // Get the cup
        restCupMockMvc.perform(delete("/api/cups/{id}", cup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cup> cupList = cupRepository.findAll();
        assertThat(cupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cup.class);
        Cup cup1 = new Cup();
        cup1.setId(1L);
        Cup cup2 = new Cup();
        cup2.setId(cup1.getId());
        assertThat(cup1).isEqualTo(cup2);
        cup2.setId(2L);
        assertThat(cup1).isNotEqualTo(cup2);
        cup1.setId(null);
        assertThat(cup1).isNotEqualTo(cup2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CupDTO.class);
        CupDTO cupDTO1 = new CupDTO();
        cupDTO1.setId(1L);
        CupDTO cupDTO2 = new CupDTO();
        assertThat(cupDTO1).isNotEqualTo(cupDTO2);
        cupDTO2.setId(cupDTO1.getId());
        assertThat(cupDTO1).isEqualTo(cupDTO2);
        cupDTO2.setId(2L);
        assertThat(cupDTO1).isNotEqualTo(cupDTO2);
        cupDTO1.setId(null);
        assertThat(cupDTO1).isNotEqualTo(cupDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cupMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cupMapper.fromId(null)).isNull();
    }
}
