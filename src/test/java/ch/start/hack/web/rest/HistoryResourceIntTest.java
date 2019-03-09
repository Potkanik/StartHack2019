package ch.start.hack.web.rest;

import ch.start.hack.StartHack2019App;

import ch.start.hack.domain.History;
import ch.start.hack.repository.HistoryRepository;
import ch.start.hack.service.HistoryService;
import ch.start.hack.service.dto.HistoryDTO;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static ch.start.hack.web.rest.TestUtil.sameInstant;
import static ch.start.hack.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.start.hack.domain.enumeration.CupAction;
/**
 * Test class for the HistoryResource REST controller.
 *
 * @see HistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartHack2019App.class)
public class HistoryResourceIntTest {

    private static final CupAction DEFAULT_ACTION = CupAction.Created;
    private static final CupAction UPDATED_ACTION = CupAction.Taken;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private HistoryMapper historyMapper;

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

    private MockMvc restHistoryMockMvc;

    private History history;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HistoryResource historyResource = new HistoryResource(historyService);
        this.restHistoryMockMvc = MockMvcBuilders.standaloneSetup(historyResource)
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
    public static History createEntity(EntityManager em) {
        History history = new History()
            .action(DEFAULT_ACTION)
            .date(DEFAULT_DATE);
        return history;
    }

    @Before
    public void initTest() {
        history = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistory() throws Exception {
        int databaseSizeBeforeCreate = historyRepository.findAll().size();

        // Create the History
        HistoryDTO historyDTO = historyMapper.toDto(history);
        restHistoryMockMvc.perform(post("/api/histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isCreated());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeCreate + 1);
        History testHistory = historyList.get(historyList.size() - 1);
        assertThat(testHistory.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testHistory.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historyRepository.findAll().size();

        // Create the History with an existing ID
        history.setId(1L);
        HistoryDTO historyDTO = historyMapper.toDto(history);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoryMockMvc.perform(post("/api/histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHistories() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get all the historyList
        restHistoryMockMvc.perform(get("/api/histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(history.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }
    
    @Test
    @Transactional
    public void getHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get the history
        restHistoryMockMvc.perform(get("/api/histories/{id}", history.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(history.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingHistory() throws Exception {
        // Get the history
        restHistoryMockMvc.perform(get("/api/histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        int databaseSizeBeforeUpdate = historyRepository.findAll().size();

        // Update the history
        History updatedHistory = historyRepository.findById(history.getId()).get();
        // Disconnect from session so that the updates on updatedHistory are not directly saved in db
        em.detach(updatedHistory);
        updatedHistory
            .action(UPDATED_ACTION)
            .date(UPDATED_DATE);
        HistoryDTO historyDTO = historyMapper.toDto(updatedHistory);

        restHistoryMockMvc.perform(put("/api/histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isOk());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);
        History testHistory = historyList.get(historyList.size() - 1);
        assertThat(testHistory.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testHistory.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingHistory() throws Exception {
        int databaseSizeBeforeUpdate = historyRepository.findAll().size();

        // Create the History
        HistoryDTO historyDTO = historyMapper.toDto(history);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoryMockMvc.perform(put("/api/histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the History in the database
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        int databaseSizeBeforeDelete = historyRepository.findAll().size();

        // Get the history
        restHistoryMockMvc.perform(delete("/api/histories/{id}", history.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<History> historyList = historyRepository.findAll();
        assertThat(historyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(History.class);
        History history1 = new History();
        history1.setId(1L);
        History history2 = new History();
        history2.setId(history1.getId());
        assertThat(history1).isEqualTo(history2);
        history2.setId(2L);
        assertThat(history1).isNotEqualTo(history2);
        history1.setId(null);
        assertThat(history1).isNotEqualTo(history2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoryDTO.class);
        HistoryDTO historyDTO1 = new HistoryDTO();
        historyDTO1.setId(1L);
        HistoryDTO historyDTO2 = new HistoryDTO();
        assertThat(historyDTO1).isNotEqualTo(historyDTO2);
        historyDTO2.setId(historyDTO1.getId());
        assertThat(historyDTO1).isEqualTo(historyDTO2);
        historyDTO2.setId(2L);
        assertThat(historyDTO1).isNotEqualTo(historyDTO2);
        historyDTO1.setId(null);
        assertThat(historyDTO1).isNotEqualTo(historyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(historyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(historyMapper.fromId(null)).isNull();
    }
}
