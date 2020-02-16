package eu.cipster.bizio.web.rest;

import eu.cipster.bizio.BizioApp;
import eu.cipster.bizio.domain.LedgerItem;
import eu.cipster.bizio.repository.LedgerItemRepository;
import eu.cipster.bizio.service.LedgerItemService;
import eu.cipster.bizio.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static eu.cipster.bizio.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import eu.cipster.bizio.domain.enumeration.LedgerType;
/**
 * Integration tests for the {@link LedgerItemResource} REST controller.
 */
@SpringBootTest(classes = BizioApp.class)
public class LedgerItemResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DOCUMENT = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT = "BBBBBBBBBB";

    private static final String DEFAULT_EXPLANATION = "AAAAAAAAAA";
    private static final String UPDATED_EXPLANATION = "BBBBBBBBBB";

    private static final LedgerType DEFAULT_TYPE = LedgerType.INCOME_CASH;
    private static final LedgerType UPDATED_TYPE = LedgerType.INCOME_BANK;

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    @Autowired
    private LedgerItemRepository ledgerItemRepository;

    @Autowired
    private LedgerItemService ledgerItemService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restLedgerItemMockMvc;

    private LedgerItem ledgerItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LedgerItemResource ledgerItemResource = new LedgerItemResource(ledgerItemService);
        this.restLedgerItemMockMvc = MockMvcBuilders.standaloneSetup(ledgerItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LedgerItem createEntity(EntityManager em) {
        LedgerItem ledgerItem = new LedgerItem()
            .date(DEFAULT_DATE)
            .document(DEFAULT_DOCUMENT)
            .explanation(DEFAULT_EXPLANATION)
            .type(DEFAULT_TYPE)
            .value(DEFAULT_VALUE)
            .year(DEFAULT_YEAR);
        return ledgerItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LedgerItem createUpdatedEntity(EntityManager em) {
        LedgerItem ledgerItem = new LedgerItem()
            .date(UPDATED_DATE)
            .document(UPDATED_DOCUMENT)
            .explanation(UPDATED_EXPLANATION)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .year(UPDATED_YEAR);
        return ledgerItem;
    }

    @BeforeEach
    public void initTest() {
        ledgerItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createLedgerItem() throws Exception {
        int databaseSizeBeforeCreate = ledgerItemRepository.findAll().size();

        // Create the LedgerItem
        restLedgerItemMockMvc.perform(post("/api/ledger-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ledgerItem)))
            .andExpect(status().isCreated());

        // Validate the LedgerItem in the database
        List<LedgerItem> ledgerItemList = ledgerItemRepository.findAll();
        assertThat(ledgerItemList).hasSize(databaseSizeBeforeCreate + 1);
        LedgerItem testLedgerItem = ledgerItemList.get(ledgerItemList.size() - 1);
        assertThat(testLedgerItem.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testLedgerItem.getDocument()).isEqualTo(DEFAULT_DOCUMENT);
        assertThat(testLedgerItem.getExplanation()).isEqualTo(DEFAULT_EXPLANATION);
        assertThat(testLedgerItem.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testLedgerItem.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testLedgerItem.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void createLedgerItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ledgerItemRepository.findAll().size();

        // Create the LedgerItem with an existing ID
        ledgerItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLedgerItemMockMvc.perform(post("/api/ledger-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ledgerItem)))
            .andExpect(status().isBadRequest());

        // Validate the LedgerItem in the database
        List<LedgerItem> ledgerItemList = ledgerItemRepository.findAll();
        assertThat(ledgerItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = ledgerItemRepository.findAll().size();
        // set the field null
        ledgerItem.setDate(null);

        // Create the LedgerItem, which fails.

        restLedgerItemMockMvc.perform(post("/api/ledger-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ledgerItem)))
            .andExpect(status().isBadRequest());

        List<LedgerItem> ledgerItemList = ledgerItemRepository.findAll();
        assertThat(ledgerItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDocumentIsRequired() throws Exception {
        int databaseSizeBeforeTest = ledgerItemRepository.findAll().size();
        // set the field null
        ledgerItem.setDocument(null);

        // Create the LedgerItem, which fails.

        restLedgerItemMockMvc.perform(post("/api/ledger-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ledgerItem)))
            .andExpect(status().isBadRequest());

        List<LedgerItem> ledgerItemList = ledgerItemRepository.findAll();
        assertThat(ledgerItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExplanationIsRequired() throws Exception {
        int databaseSizeBeforeTest = ledgerItemRepository.findAll().size();
        // set the field null
        ledgerItem.setExplanation(null);

        // Create the LedgerItem, which fails.

        restLedgerItemMockMvc.perform(post("/api/ledger-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ledgerItem)))
            .andExpect(status().isBadRequest());

        List<LedgerItem> ledgerItemList = ledgerItemRepository.findAll();
        assertThat(ledgerItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ledgerItemRepository.findAll().size();
        // set the field null
        ledgerItem.setType(null);

        // Create the LedgerItem, which fails.

        restLedgerItemMockMvc.perform(post("/api/ledger-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ledgerItem)))
            .andExpect(status().isBadRequest());

        List<LedgerItem> ledgerItemList = ledgerItemRepository.findAll();
        assertThat(ledgerItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = ledgerItemRepository.findAll().size();
        // set the field null
        ledgerItem.setValue(null);

        // Create the LedgerItem, which fails.

        restLedgerItemMockMvc.perform(post("/api/ledger-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ledgerItem)))
            .andExpect(status().isBadRequest());

        List<LedgerItem> ledgerItemList = ledgerItemRepository.findAll();
        assertThat(ledgerItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = ledgerItemRepository.findAll().size();
        // set the field null
        ledgerItem.setYear(null);

        // Create the LedgerItem, which fails.

        restLedgerItemMockMvc.perform(post("/api/ledger-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ledgerItem)))
            .andExpect(status().isBadRequest());

        List<LedgerItem> ledgerItemList = ledgerItemRepository.findAll();
        assertThat(ledgerItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLedgerItems() throws Exception {
        // Initialize the database
        ledgerItemRepository.saveAndFlush(ledgerItem);

        // Get all the ledgerItemList
        restLedgerItemMockMvc.perform(get("/api/ledger-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ledgerItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].document").value(hasItem(DEFAULT_DOCUMENT)))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }
    
    @Test
    @Transactional
    public void getLedgerItem() throws Exception {
        // Initialize the database
        ledgerItemRepository.saveAndFlush(ledgerItem);

        // Get the ledgerItem
        restLedgerItemMockMvc.perform(get("/api/ledger-items/{id}", ledgerItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ledgerItem.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.document").value(DEFAULT_DOCUMENT))
            .andExpect(jsonPath("$.explanation").value(DEFAULT_EXPLANATION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    public void getNonExistingLedgerItem() throws Exception {
        // Get the ledgerItem
        restLedgerItemMockMvc.perform(get("/api/ledger-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLedgerItem() throws Exception {
        // Initialize the database
        ledgerItemService.save(ledgerItem);

        int databaseSizeBeforeUpdate = ledgerItemRepository.findAll().size();

        // Update the ledgerItem
        LedgerItem updatedLedgerItem = ledgerItemRepository.findById(ledgerItem.getId()).get();
        // Disconnect from session so that the updates on updatedLedgerItem are not directly saved in db
        em.detach(updatedLedgerItem);
        updatedLedgerItem
            .date(UPDATED_DATE)
            .document(UPDATED_DOCUMENT)
            .explanation(UPDATED_EXPLANATION)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .year(UPDATED_YEAR);

        restLedgerItemMockMvc.perform(put("/api/ledger-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLedgerItem)))
            .andExpect(status().isOk());

        // Validate the LedgerItem in the database
        List<LedgerItem> ledgerItemList = ledgerItemRepository.findAll();
        assertThat(ledgerItemList).hasSize(databaseSizeBeforeUpdate);
        LedgerItem testLedgerItem = ledgerItemList.get(ledgerItemList.size() - 1);
        assertThat(testLedgerItem.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testLedgerItem.getDocument()).isEqualTo(UPDATED_DOCUMENT);
        assertThat(testLedgerItem.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testLedgerItem.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testLedgerItem.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testLedgerItem.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void updateNonExistingLedgerItem() throws Exception {
        int databaseSizeBeforeUpdate = ledgerItemRepository.findAll().size();

        // Create the LedgerItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLedgerItemMockMvc.perform(put("/api/ledger-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ledgerItem)))
            .andExpect(status().isBadRequest());

        // Validate the LedgerItem in the database
        List<LedgerItem> ledgerItemList = ledgerItemRepository.findAll();
        assertThat(ledgerItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLedgerItem() throws Exception {
        // Initialize the database
        ledgerItemService.save(ledgerItem);

        int databaseSizeBeforeDelete = ledgerItemRepository.findAll().size();

        // Delete the ledgerItem
        restLedgerItemMockMvc.perform(delete("/api/ledger-items/{id}", ledgerItem.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LedgerItem> ledgerItemList = ledgerItemRepository.findAll();
        assertThat(ledgerItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
