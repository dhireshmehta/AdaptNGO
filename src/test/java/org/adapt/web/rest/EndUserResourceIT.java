package org.adapt.web.rest;

import org.adapt.AdaptNgoApp;
import org.adapt.domain.EndUser;
import org.adapt.repository.EndUserRepository;
import org.adapt.service.EndUserService;
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
 * Integration tests for the {@link EndUserResource} REST controller.
 */
@SpringBootTest(classes = AdaptNgoApp.class)
public class EndUserResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ROLES = "AAAAAAAAAA";
    private static final String UPDATED_ROLES = "BBBBBBBBBB";

    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private EndUserService endUserService;

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

    private MockMvc restEndUserMockMvc;

    private EndUser endUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EndUserResource endUserResource = new EndUserResource(endUserService);
        this.restEndUserMockMvc = MockMvcBuilders.standaloneSetup(endUserResource)
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
    public static EndUser createEntity(EntityManager em) {
        EndUser endUser = new EndUser()
            .email(DEFAULT_EMAIL)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .roles(DEFAULT_ROLES);
        return endUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EndUser createUpdatedEntity(EntityManager em) {
        EndUser endUser = new EndUser()
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .roles(UPDATED_ROLES);
        return endUser;
    }

    @BeforeEach
    public void initTest() {
        endUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createEndUser() throws Exception {
        int databaseSizeBeforeCreate = endUserRepository.findAll().size();

        // Create the EndUser
        restEndUserMockMvc.perform(post("/api/end-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endUser)))
            .andExpect(status().isCreated());

        // Validate the EndUser in the database
        List<EndUser> endUserList = endUserRepository.findAll();
        assertThat(endUserList).hasSize(databaseSizeBeforeCreate + 1);
        EndUser testEndUser = endUserList.get(endUserList.size() - 1);
        assertThat(testEndUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEndUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEndUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEndUser.getRoles()).isEqualTo(DEFAULT_ROLES);
    }

    @Test
    @Transactional
    public void createEndUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = endUserRepository.findAll().size();

        // Create the EndUser with an existing ID
        endUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEndUserMockMvc.perform(post("/api/end-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endUser)))
            .andExpect(status().isBadRequest());

        // Validate the EndUser in the database
        List<EndUser> endUserList = endUserRepository.findAll();
        assertThat(endUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEndUsers() throws Exception {
        // Initialize the database
        endUserRepository.saveAndFlush(endUser);

        // Get all the endUserList
        restEndUserMockMvc.perform(get("/api/end-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].roles").value(hasItem(DEFAULT_ROLES)));
    }
    
    @Test
    @Transactional
    public void getEndUser() throws Exception {
        // Initialize the database
        endUserRepository.saveAndFlush(endUser);

        // Get the endUser
        restEndUserMockMvc.perform(get("/api/end-users/{id}", endUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(endUser.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.roles").value(DEFAULT_ROLES));
    }

    @Test
    @Transactional
    public void getNonExistingEndUser() throws Exception {
        // Get the endUser
        restEndUserMockMvc.perform(get("/api/end-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEndUser() throws Exception {
        // Initialize the database
        endUserService.save(endUser);

        int databaseSizeBeforeUpdate = endUserRepository.findAll().size();

        // Update the endUser
        EndUser updatedEndUser = endUserRepository.findById(endUser.getId()).get();
        // Disconnect from session so that the updates on updatedEndUser are not directly saved in db
        em.detach(updatedEndUser);
        updatedEndUser
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .roles(UPDATED_ROLES);

        restEndUserMockMvc.perform(put("/api/end-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEndUser)))
            .andExpect(status().isOk());

        // Validate the EndUser in the database
        List<EndUser> endUserList = endUserRepository.findAll();
        assertThat(endUserList).hasSize(databaseSizeBeforeUpdate);
        EndUser testEndUser = endUserList.get(endUserList.size() - 1);
        assertThat(testEndUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEndUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEndUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEndUser.getRoles()).isEqualTo(UPDATED_ROLES);
    }

    @Test
    @Transactional
    public void updateNonExistingEndUser() throws Exception {
        int databaseSizeBeforeUpdate = endUserRepository.findAll().size();

        // Create the EndUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEndUserMockMvc.perform(put("/api/end-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endUser)))
            .andExpect(status().isBadRequest());

        // Validate the EndUser in the database
        List<EndUser> endUserList = endUserRepository.findAll();
        assertThat(endUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEndUser() throws Exception {
        // Initialize the database
        endUserService.save(endUser);

        int databaseSizeBeforeDelete = endUserRepository.findAll().size();

        // Delete the endUser
        restEndUserMockMvc.perform(delete("/api/end-users/{id}", endUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EndUser> endUserList = endUserRepository.findAll();
        assertThat(endUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
