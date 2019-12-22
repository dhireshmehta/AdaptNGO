package org.adapt.web.rest;

import org.adapt.AdaptNgoApp;
import org.adapt.domain.LineItem;
import org.adapt.domain.TypeEnum;
import org.adapt.repository.LineItemRepository;
import org.adapt.service.LineItemService;
import org.adapt.web.rest.errors.ExceptionTranslator;

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
import java.util.List;

import static org.adapt.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LineItemResource} REST controller.
 */
@SpringBootTest(classes = AdaptNgoApp.class)
public class LineItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORIES = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIES = "BBBBBBBBBB";

    private static final String DEFAULT_ROLES = "AAAAAAAAAA";
    private static final String UPDATED_ROLES = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final Integer DEFAULT_VIEW_COUNT = 1;
    private static final Integer UPDATED_VIEW_COUNT = 2;

    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBBB";

    @Autowired
    private LineItemRepository lineItemRepository;

    @Autowired
    private LineItemService lineItemService;

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

    private MockMvc restLineItemMockMvc;

    private LineItem lineItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LineItemResource lineItemResource = new LineItemResource(lineItemService);
        this.restLineItemMockMvc = MockMvcBuilders.standaloneSetup(lineItemResource)
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
    public static LineItem createEntity(EntityManager em) {
        LineItem lineItem = new LineItem()
            .name(DEFAULT_NAME)
            .link(DEFAULT_LINK)
            .categories(DEFAULT_CATEGORIES)
            .roles(DEFAULT_ROLES)
            .desc(DEFAULT_DESC)
            .viewCount(DEFAULT_VIEW_COUNT)
            .type(DEFAULT_TYPE);
        return lineItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LineItem createUpdatedEntity(EntityManager em) {
        LineItem lineItem = new LineItem()
            .name(UPDATED_NAME)
            .link(UPDATED_LINK)
            .categories(UPDATED_CATEGORIES)
            .roles(UPDATED_ROLES)
            .desc(UPDATED_DESC)
            .viewCount(UPDATED_VIEW_COUNT)
            .type(UPDATED_TYPE);
        return lineItem;
    }

    @BeforeEach
    public void initTest() {
        lineItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createLineItem() throws Exception {
        int databaseSizeBeforeCreate = lineItemRepository.findAll().size();

        // Create the LineItem
        restLineItemMockMvc.perform(post("/api/line-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineItem)))
            .andExpect(status().isCreated());

        // Validate the LineItem in the database
        List<LineItem> lineItemList = lineItemRepository.findAll();
        assertThat(lineItemList).hasSize(databaseSizeBeforeCreate + 1);
        LineItem testLineItem = lineItemList.get(lineItemList.size() - 1);
        assertThat(testLineItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLineItem.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testLineItem.getCategories()).isEqualTo(DEFAULT_CATEGORIES);
        assertThat(testLineItem.getRoles()).isEqualTo(DEFAULT_ROLES);
        assertThat(testLineItem.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testLineItem.getViewCount()).isEqualTo(DEFAULT_VIEW_COUNT);
        assertThat(testLineItem.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createLineItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lineItemRepository.findAll().size();

        // Create the LineItem with an existing ID
        lineItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineItemMockMvc.perform(post("/api/line-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineItem)))
            .andExpect(status().isBadRequest());

        // Validate the LineItem in the database
        List<LineItem> lineItemList = lineItemRepository.findAll();
        assertThat(lineItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLineItems() throws Exception {
        // Initialize the database
        lineItemRepository.saveAndFlush(lineItem);

        // Get all the lineItemList
        restLineItemMockMvc.perform(get("/api/line-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].categories").value(hasItem(DEFAULT_CATEGORIES)))
            .andExpect(jsonPath("$.[*].roles").value(hasItem(DEFAULT_ROLES)))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].viewCount").value(hasItem(DEFAULT_VIEW_COUNT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getLineItem() throws Exception {
        // Initialize the database
        lineItemRepository.saveAndFlush(lineItem);

        // Get the lineItem
        restLineItemMockMvc.perform(get("/api/line-items/{id}", lineItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lineItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.categories").value(DEFAULT_CATEGORIES))
            .andExpect(jsonPath("$.roles").value(DEFAULT_ROLES))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC))
            .andExpect(jsonPath("$.viewCount").value(DEFAULT_VIEW_COUNT))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    public void getNonExistingLineItem() throws Exception {
        // Get the lineItem
        restLineItemMockMvc.perform(get("/api/line-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLineItem() throws Exception {
        // Initialize the database
        lineItemService.save(lineItem);

        int databaseSizeBeforeUpdate = lineItemRepository.findAll().size();

        // Update the lineItem
        LineItem updatedLineItem = lineItemRepository.findById(lineItem.getId()).get();
        // Disconnect from session so that the updates on updatedLineItem are not directly saved in db
        em.detach(updatedLineItem);
        updatedLineItem
            .name(UPDATED_NAME)
            .link(UPDATED_LINK)
            .categories(UPDATED_CATEGORIES)
            .roles(UPDATED_ROLES)
            .desc(UPDATED_DESC)
            .viewCount(UPDATED_VIEW_COUNT)
            .type(UPDATED_TYPE);

        restLineItemMockMvc.perform(put("/api/line-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLineItem)))
            .andExpect(status().isOk());

        // Validate the LineItem in the database
        List<LineItem> lineItemList = lineItemRepository.findAll();
        assertThat(lineItemList).hasSize(databaseSizeBeforeUpdate);
        LineItem testLineItem = lineItemList.get(lineItemList.size() - 1);
        assertThat(testLineItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLineItem.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testLineItem.getCategories()).isEqualTo(UPDATED_CATEGORIES);
        assertThat(testLineItem.getRoles()).isEqualTo(UPDATED_ROLES);
        assertThat(testLineItem.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testLineItem.getViewCount()).isEqualTo(UPDATED_VIEW_COUNT);
        assertThat(testLineItem.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingLineItem() throws Exception {
        int databaseSizeBeforeUpdate = lineItemRepository.findAll().size();

        // Create the LineItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineItemMockMvc.perform(put("/api/line-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineItem)))
            .andExpect(status().isBadRequest());

        // Validate the LineItem in the database
        List<LineItem> lineItemList = lineItemRepository.findAll();
        assertThat(lineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLineItem() throws Exception {
        // Initialize the database
        lineItemService.save(lineItem);

        int databaseSizeBeforeDelete = lineItemRepository.findAll().size();

        // Delete the lineItem
        restLineItemMockMvc.perform(delete("/api/line-items/{id}", lineItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LineItem> lineItemList = lineItemRepository.findAll();
        assertThat(lineItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
