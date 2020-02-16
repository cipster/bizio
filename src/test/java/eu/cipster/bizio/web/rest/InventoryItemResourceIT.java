package eu.cipster.bizio.web.rest;

import eu.cipster.bizio.BizioApp;
import eu.cipster.bizio.domain.InventoryItem;
import eu.cipster.bizio.repository.InventoryItemRepository;
import eu.cipster.bizio.service.InventoryItemService;
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
import java.util.List;

import static eu.cipster.bizio.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InventoryItemResource} REST controller.
 */
@SpringBootTest(classes = BizioApp.class)
public class InventoryItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private InventoryItemService inventoryItemService;

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

    private MockMvc restInventoryItemMockMvc;

    private InventoryItem inventoryItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InventoryItemResource inventoryItemResource = new InventoryItemResource(inventoryItemService);
        this.restInventoryItemMockMvc = MockMvcBuilders.standaloneSetup(inventoryItemResource)
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
    public static InventoryItem createEntity(EntityManager em) {
        InventoryItem inventoryItem = new InventoryItem()
            .name(DEFAULT_NAME)
            .value(DEFAULT_VALUE);
        return inventoryItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryItem createUpdatedEntity(EntityManager em) {
        InventoryItem inventoryItem = new InventoryItem()
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE);
        return inventoryItem;
    }

    @BeforeEach
    public void initTest() {
        inventoryItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createInventoryItem() throws Exception {
        int databaseSizeBeforeCreate = inventoryItemRepository.findAll().size();

        // Create the InventoryItem
        restInventoryItemMockMvc.perform(post("/api/inventory-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inventoryItem)))
            .andExpect(status().isCreated());

        // Validate the InventoryItem in the database
        List<InventoryItem> inventoryItemList = inventoryItemRepository.findAll();
        assertThat(inventoryItemList).hasSize(databaseSizeBeforeCreate + 1);
        InventoryItem testInventoryItem = inventoryItemList.get(inventoryItemList.size() - 1);
        assertThat(testInventoryItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInventoryItem.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createInventoryItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inventoryItemRepository.findAll().size();

        // Create the InventoryItem with an existing ID
        inventoryItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryItemMockMvc.perform(post("/api/inventory-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inventoryItem)))
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        List<InventoryItem> inventoryItemList = inventoryItemRepository.findAll();
        assertThat(inventoryItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryItemRepository.findAll().size();
        // set the field null
        inventoryItem.setName(null);

        // Create the InventoryItem, which fails.

        restInventoryItemMockMvc.perform(post("/api/inventory-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inventoryItem)))
            .andExpect(status().isBadRequest());

        List<InventoryItem> inventoryItemList = inventoryItemRepository.findAll();
        assertThat(inventoryItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryItemRepository.findAll().size();
        // set the field null
        inventoryItem.setValue(null);

        // Create the InventoryItem, which fails.

        restInventoryItemMockMvc.perform(post("/api/inventory-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inventoryItem)))
            .andExpect(status().isBadRequest());

        List<InventoryItem> inventoryItemList = inventoryItemRepository.findAll();
        assertThat(inventoryItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInventoryItems() throws Exception {
        // Initialize the database
        inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList
        restInventoryItemMockMvc.perform(get("/api/inventory-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())));
    }
    
    @Test
    @Transactional
    public void getInventoryItem() throws Exception {
        // Initialize the database
        inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get the inventoryItem
        restInventoryItemMockMvc.perform(get("/api/inventory-items/{id}", inventoryItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventoryItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInventoryItem() throws Exception {
        // Get the inventoryItem
        restInventoryItemMockMvc.perform(get("/api/inventory-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInventoryItem() throws Exception {
        // Initialize the database
        inventoryItemService.save(inventoryItem);

        int databaseSizeBeforeUpdate = inventoryItemRepository.findAll().size();

        // Update the inventoryItem
        InventoryItem updatedInventoryItem = inventoryItemRepository.findById(inventoryItem.getId()).get();
        // Disconnect from session so that the updates on updatedInventoryItem are not directly saved in db
        em.detach(updatedInventoryItem);
        updatedInventoryItem
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE);

        restInventoryItemMockMvc.perform(put("/api/inventory-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInventoryItem)))
            .andExpect(status().isOk());

        // Validate the InventoryItem in the database
        List<InventoryItem> inventoryItemList = inventoryItemRepository.findAll();
        assertThat(inventoryItemList).hasSize(databaseSizeBeforeUpdate);
        InventoryItem testInventoryItem = inventoryItemList.get(inventoryItemList.size() - 1);
        assertThat(testInventoryItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInventoryItem.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingInventoryItem() throws Exception {
        int databaseSizeBeforeUpdate = inventoryItemRepository.findAll().size();

        // Create the InventoryItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc.perform(put("/api/inventory-items")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inventoryItem)))
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        List<InventoryItem> inventoryItemList = inventoryItemRepository.findAll();
        assertThat(inventoryItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInventoryItem() throws Exception {
        // Initialize the database
        inventoryItemService.save(inventoryItem);

        int databaseSizeBeforeDelete = inventoryItemRepository.findAll().size();

        // Delete the inventoryItem
        restInventoryItemMockMvc.perform(delete("/api/inventory-items/{id}", inventoryItem.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InventoryItem> inventoryItemList = inventoryItemRepository.findAll();
        assertThat(inventoryItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
