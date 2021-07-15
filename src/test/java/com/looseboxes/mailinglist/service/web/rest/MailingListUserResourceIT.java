package com.looseboxes.mailinglist.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.looseboxes.mailinglist.service.IntegrationTest;
import com.looseboxes.mailinglist.service.domain.MailingListUser;
import com.looseboxes.mailinglist.service.domain.enumeration.MailingListUserStatus;
import com.looseboxes.mailinglist.service.repository.MailingListUserRepository;
import com.looseboxes.mailinglist.service.service.criteria.MailingListUserCriteria;
import com.looseboxes.mailinglist.service.service.dto.MailingListUserDTO;
import com.looseboxes.mailinglist.service.service.mapper.MailingListUserMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link MailingListUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MailingListUserResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ADDRESS = "_\"8@t.N?1Y";
    private static final String UPDATED_EMAIL_ADDRESS = "~k[dh;@xw&h\\..a^1T";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final MailingListUserStatus DEFAULT_STATUS = MailingListUserStatus.Unverified;
    private static final MailingListUserStatus UPDATED_STATUS = MailingListUserStatus.Verified;

    private static final Instant DEFAULT_TIME_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TIME_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/mailing-list-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MailingListUserRepository mailingListUserRepository;

    @Autowired
    private MailingListUserMapper mailingListUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMailingListUserMockMvc;

    private MailingListUser mailingListUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MailingListUser createEntity(EntityManager em) {
        MailingListUser mailingListUser = new MailingListUser()
            .username(DEFAULT_USERNAME)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .lastName(DEFAULT_LAST_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .status(DEFAULT_STATUS)
            .timeCreated(DEFAULT_TIME_CREATED)
            .timeModified(DEFAULT_TIME_MODIFIED);
        return mailingListUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MailingListUser createUpdatedEntity(EntityManager em) {
        MailingListUser mailingListUser = new MailingListUser()
            .username(UPDATED_USERNAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .status(UPDATED_STATUS)
            .timeCreated(UPDATED_TIME_CREATED)
            .timeModified(UPDATED_TIME_MODIFIED);
        return mailingListUser;
    }

    @BeforeEach
    public void initTest() {
        mailingListUser = createEntity(em);
    }

    @Test
    @Transactional
    void createMailingListUser() throws Exception {
        int databaseSizeBeforeCreate = mailingListUserRepository.findAll().size();
        // Create the MailingListUser
        MailingListUserDTO mailingListUserDTO = mailingListUserMapper.toDto(mailingListUser);
        restMailingListUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mailingListUserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MailingListUser in the database
        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeCreate + 1);
        MailingListUser testMailingListUser = mailingListUserList.get(mailingListUserList.size() - 1);
        assertThat(testMailingListUser.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testMailingListUser.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testMailingListUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testMailingListUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMailingListUser.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMailingListUser.getTimeCreated()).isEqualTo(DEFAULT_TIME_CREATED);
        assertThat(testMailingListUser.getTimeModified()).isEqualTo(DEFAULT_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void createMailingListUserWithExistingId() throws Exception {
        // Create the MailingListUser with an existing ID
        mailingListUser.setId(1L);
        MailingListUserDTO mailingListUserDTO = mailingListUserMapper.toDto(mailingListUser);

        int databaseSizeBeforeCreate = mailingListUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMailingListUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mailingListUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MailingListUser in the database
        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailingListUserRepository.findAll().size();
        // set the field null
        mailingListUser.setStatus(null);

        // Create the MailingListUser, which fails.
        MailingListUserDTO mailingListUserDTO = mailingListUserMapper.toDto(mailingListUser);

        restMailingListUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mailingListUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailingListUserRepository.findAll().size();
        // set the field null
        mailingListUser.setTimeCreated(null);

        // Create the MailingListUser, which fails.
        MailingListUserDTO mailingListUserDTO = mailingListUserMapper.toDto(mailingListUser);

        restMailingListUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mailingListUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailingListUserRepository.findAll().size();
        // set the field null
        mailingListUser.setTimeModified(null);

        // Create the MailingListUser, which fails.
        MailingListUserDTO mailingListUserDTO = mailingListUserMapper.toDto(mailingListUser);

        restMailingListUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mailingListUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMailingListUsers() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList
        restMailingListUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mailingListUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].timeCreated").value(hasItem(DEFAULT_TIME_CREATED.toString())))
            .andExpect(jsonPath("$.[*].timeModified").value(hasItem(DEFAULT_TIME_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getMailingListUser() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get the mailingListUser
        restMailingListUserMockMvc
            .perform(get(ENTITY_API_URL_ID, mailingListUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mailingListUser.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.timeCreated").value(DEFAULT_TIME_CREATED.toString()))
            .andExpect(jsonPath("$.timeModified").value(DEFAULT_TIME_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getMailingListUsersByIdFiltering() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        Long id = mailingListUser.getId();

        defaultMailingListUserShouldBeFound("id.equals=" + id);
        defaultMailingListUserShouldNotBeFound("id.notEquals=" + id);

        defaultMailingListUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMailingListUserShouldNotBeFound("id.greaterThan=" + id);

        defaultMailingListUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMailingListUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where username equals to DEFAULT_USERNAME
        defaultMailingListUserShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the mailingListUserList where username equals to UPDATED_USERNAME
        defaultMailingListUserShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByUsernameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where username not equals to DEFAULT_USERNAME
        defaultMailingListUserShouldNotBeFound("username.notEquals=" + DEFAULT_USERNAME);

        // Get all the mailingListUserList where username not equals to UPDATED_USERNAME
        defaultMailingListUserShouldBeFound("username.notEquals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultMailingListUserShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the mailingListUserList where username equals to UPDATED_USERNAME
        defaultMailingListUserShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where username is not null
        defaultMailingListUserShouldBeFound("username.specified=true");

        // Get all the mailingListUserList where username is null
        defaultMailingListUserShouldNotBeFound("username.specified=false");
    }

    @Test
    @Transactional
    void getAllMailingListUsersByUsernameContainsSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where username contains DEFAULT_USERNAME
        defaultMailingListUserShouldBeFound("username.contains=" + DEFAULT_USERNAME);

        // Get all the mailingListUserList where username contains UPDATED_USERNAME
        defaultMailingListUserShouldNotBeFound("username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where username does not contain DEFAULT_USERNAME
        defaultMailingListUserShouldNotBeFound("username.doesNotContain=" + DEFAULT_USERNAME);

        // Get all the mailingListUserList where username does not contain UPDATED_USERNAME
        defaultMailingListUserShouldBeFound("username.doesNotContain=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByEmailAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where emailAddress equals to DEFAULT_EMAIL_ADDRESS
        defaultMailingListUserShouldBeFound("emailAddress.equals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the mailingListUserList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultMailingListUserShouldNotBeFound("emailAddress.equals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByEmailAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where emailAddress not equals to DEFAULT_EMAIL_ADDRESS
        defaultMailingListUserShouldNotBeFound("emailAddress.notEquals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the mailingListUserList where emailAddress not equals to UPDATED_EMAIL_ADDRESS
        defaultMailingListUserShouldBeFound("emailAddress.notEquals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByEmailAddressIsInShouldWork() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where emailAddress in DEFAULT_EMAIL_ADDRESS or UPDATED_EMAIL_ADDRESS
        defaultMailingListUserShouldBeFound("emailAddress.in=" + DEFAULT_EMAIL_ADDRESS + "," + UPDATED_EMAIL_ADDRESS);

        // Get all the mailingListUserList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultMailingListUserShouldNotBeFound("emailAddress.in=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByEmailAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where emailAddress is not null
        defaultMailingListUserShouldBeFound("emailAddress.specified=true");

        // Get all the mailingListUserList where emailAddress is null
        defaultMailingListUserShouldNotBeFound("emailAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllMailingListUsersByEmailAddressContainsSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where emailAddress contains DEFAULT_EMAIL_ADDRESS
        defaultMailingListUserShouldBeFound("emailAddress.contains=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the mailingListUserList where emailAddress contains UPDATED_EMAIL_ADDRESS
        defaultMailingListUserShouldNotBeFound("emailAddress.contains=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByEmailAddressNotContainsSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where emailAddress does not contain DEFAULT_EMAIL_ADDRESS
        defaultMailingListUserShouldNotBeFound("emailAddress.doesNotContain=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the mailingListUserList where emailAddress does not contain UPDATED_EMAIL_ADDRESS
        defaultMailingListUserShouldBeFound("emailAddress.doesNotContain=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where lastName equals to DEFAULT_LAST_NAME
        defaultMailingListUserShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the mailingListUserList where lastName equals to UPDATED_LAST_NAME
        defaultMailingListUserShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where lastName not equals to DEFAULT_LAST_NAME
        defaultMailingListUserShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the mailingListUserList where lastName not equals to UPDATED_LAST_NAME
        defaultMailingListUserShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultMailingListUserShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the mailingListUserList where lastName equals to UPDATED_LAST_NAME
        defaultMailingListUserShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where lastName is not null
        defaultMailingListUserShouldBeFound("lastName.specified=true");

        // Get all the mailingListUserList where lastName is null
        defaultMailingListUserShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllMailingListUsersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where lastName contains DEFAULT_LAST_NAME
        defaultMailingListUserShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the mailingListUserList where lastName contains UPDATED_LAST_NAME
        defaultMailingListUserShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where lastName does not contain DEFAULT_LAST_NAME
        defaultMailingListUserShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the mailingListUserList where lastName does not contain UPDATED_LAST_NAME
        defaultMailingListUserShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where firstName equals to DEFAULT_FIRST_NAME
        defaultMailingListUserShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the mailingListUserList where firstName equals to UPDATED_FIRST_NAME
        defaultMailingListUserShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where firstName not equals to DEFAULT_FIRST_NAME
        defaultMailingListUserShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the mailingListUserList where firstName not equals to UPDATED_FIRST_NAME
        defaultMailingListUserShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultMailingListUserShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the mailingListUserList where firstName equals to UPDATED_FIRST_NAME
        defaultMailingListUserShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where firstName is not null
        defaultMailingListUserShouldBeFound("firstName.specified=true");

        // Get all the mailingListUserList where firstName is null
        defaultMailingListUserShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllMailingListUsersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where firstName contains DEFAULT_FIRST_NAME
        defaultMailingListUserShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the mailingListUserList where firstName contains UPDATED_FIRST_NAME
        defaultMailingListUserShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultMailingListUserShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the mailingListUserList where firstName does not contain UPDATED_FIRST_NAME
        defaultMailingListUserShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where status equals to DEFAULT_STATUS
        defaultMailingListUserShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the mailingListUserList where status equals to UPDATED_STATUS
        defaultMailingListUserShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where status not equals to DEFAULT_STATUS
        defaultMailingListUserShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the mailingListUserList where status not equals to UPDATED_STATUS
        defaultMailingListUserShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultMailingListUserShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the mailingListUserList where status equals to UPDATED_STATUS
        defaultMailingListUserShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where status is not null
        defaultMailingListUserShouldBeFound("status.specified=true");

        // Get all the mailingListUserList where status is null
        defaultMailingListUserShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllMailingListUsersByTimeCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where timeCreated equals to DEFAULT_TIME_CREATED
        defaultMailingListUserShouldBeFound("timeCreated.equals=" + DEFAULT_TIME_CREATED);

        // Get all the mailingListUserList where timeCreated equals to UPDATED_TIME_CREATED
        defaultMailingListUserShouldNotBeFound("timeCreated.equals=" + UPDATED_TIME_CREATED);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByTimeCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where timeCreated not equals to DEFAULT_TIME_CREATED
        defaultMailingListUserShouldNotBeFound("timeCreated.notEquals=" + DEFAULT_TIME_CREATED);

        // Get all the mailingListUserList where timeCreated not equals to UPDATED_TIME_CREATED
        defaultMailingListUserShouldBeFound("timeCreated.notEquals=" + UPDATED_TIME_CREATED);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByTimeCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where timeCreated in DEFAULT_TIME_CREATED or UPDATED_TIME_CREATED
        defaultMailingListUserShouldBeFound("timeCreated.in=" + DEFAULT_TIME_CREATED + "," + UPDATED_TIME_CREATED);

        // Get all the mailingListUserList where timeCreated equals to UPDATED_TIME_CREATED
        defaultMailingListUserShouldNotBeFound("timeCreated.in=" + UPDATED_TIME_CREATED);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByTimeCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where timeCreated is not null
        defaultMailingListUserShouldBeFound("timeCreated.specified=true");

        // Get all the mailingListUserList where timeCreated is null
        defaultMailingListUserShouldNotBeFound("timeCreated.specified=false");
    }

    @Test
    @Transactional
    void getAllMailingListUsersByTimeModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where timeModified equals to DEFAULT_TIME_MODIFIED
        defaultMailingListUserShouldBeFound("timeModified.equals=" + DEFAULT_TIME_MODIFIED);

        // Get all the mailingListUserList where timeModified equals to UPDATED_TIME_MODIFIED
        defaultMailingListUserShouldNotBeFound("timeModified.equals=" + UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByTimeModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where timeModified not equals to DEFAULT_TIME_MODIFIED
        defaultMailingListUserShouldNotBeFound("timeModified.notEquals=" + DEFAULT_TIME_MODIFIED);

        // Get all the mailingListUserList where timeModified not equals to UPDATED_TIME_MODIFIED
        defaultMailingListUserShouldBeFound("timeModified.notEquals=" + UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByTimeModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where timeModified in DEFAULT_TIME_MODIFIED or UPDATED_TIME_MODIFIED
        defaultMailingListUserShouldBeFound("timeModified.in=" + DEFAULT_TIME_MODIFIED + "," + UPDATED_TIME_MODIFIED);

        // Get all the mailingListUserList where timeModified equals to UPDATED_TIME_MODIFIED
        defaultMailingListUserShouldNotBeFound("timeModified.in=" + UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void getAllMailingListUsersByTimeModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        // Get all the mailingListUserList where timeModified is not null
        defaultMailingListUserShouldBeFound("timeModified.specified=true");

        // Get all the mailingListUserList where timeModified is null
        defaultMailingListUserShouldNotBeFound("timeModified.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMailingListUserShouldBeFound(String filter) throws Exception {
        restMailingListUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mailingListUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].timeCreated").value(hasItem(DEFAULT_TIME_CREATED.toString())))
            .andExpect(jsonPath("$.[*].timeModified").value(hasItem(DEFAULT_TIME_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restMailingListUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMailingListUserShouldNotBeFound(String filter) throws Exception {
        restMailingListUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMailingListUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMailingListUser() throws Exception {
        // Get the mailingListUser
        restMailingListUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMailingListUser() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        int databaseSizeBeforeUpdate = mailingListUserRepository.findAll().size();

        // Update the mailingListUser
        MailingListUser updatedMailingListUser = mailingListUserRepository.findById(mailingListUser.getId()).get();
        // Disconnect from session so that the updates on updatedMailingListUser are not directly saved in db
        em.detach(updatedMailingListUser);
        updatedMailingListUser
            .username(UPDATED_USERNAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .status(UPDATED_STATUS)
            .timeCreated(UPDATED_TIME_CREATED)
            .timeModified(UPDATED_TIME_MODIFIED);
        MailingListUserDTO mailingListUserDTO = mailingListUserMapper.toDto(updatedMailingListUser);

        restMailingListUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mailingListUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mailingListUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the MailingListUser in the database
        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeUpdate);
        MailingListUser testMailingListUser = mailingListUserList.get(mailingListUserList.size() - 1);
        assertThat(testMailingListUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testMailingListUser.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testMailingListUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testMailingListUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMailingListUser.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMailingListUser.getTimeCreated()).isEqualTo(UPDATED_TIME_CREATED);
        assertThat(testMailingListUser.getTimeModified()).isEqualTo(UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingMailingListUser() throws Exception {
        int databaseSizeBeforeUpdate = mailingListUserRepository.findAll().size();
        mailingListUser.setId(count.incrementAndGet());

        // Create the MailingListUser
        MailingListUserDTO mailingListUserDTO = mailingListUserMapper.toDto(mailingListUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMailingListUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mailingListUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mailingListUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MailingListUser in the database
        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMailingListUser() throws Exception {
        int databaseSizeBeforeUpdate = mailingListUserRepository.findAll().size();
        mailingListUser.setId(count.incrementAndGet());

        // Create the MailingListUser
        MailingListUserDTO mailingListUserDTO = mailingListUserMapper.toDto(mailingListUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMailingListUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mailingListUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MailingListUser in the database
        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMailingListUser() throws Exception {
        int databaseSizeBeforeUpdate = mailingListUserRepository.findAll().size();
        mailingListUser.setId(count.incrementAndGet());

        // Create the MailingListUser
        MailingListUserDTO mailingListUserDTO = mailingListUserMapper.toDto(mailingListUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMailingListUserMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mailingListUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MailingListUser in the database
        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMailingListUserWithPatch() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        int databaseSizeBeforeUpdate = mailingListUserRepository.findAll().size();

        // Update the mailingListUser using partial update
        MailingListUser partialUpdatedMailingListUser = new MailingListUser();
        partialUpdatedMailingListUser.setId(mailingListUser.getId());

        partialUpdatedMailingListUser
            .username(UPDATED_USERNAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .status(UPDATED_STATUS)
            .timeCreated(UPDATED_TIME_CREATED)
            .timeModified(UPDATED_TIME_MODIFIED);

        restMailingListUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMailingListUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMailingListUser))
            )
            .andExpect(status().isOk());

        // Validate the MailingListUser in the database
        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeUpdate);
        MailingListUser testMailingListUser = mailingListUserList.get(mailingListUserList.size() - 1);
        assertThat(testMailingListUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testMailingListUser.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testMailingListUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testMailingListUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMailingListUser.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMailingListUser.getTimeCreated()).isEqualTo(UPDATED_TIME_CREATED);
        assertThat(testMailingListUser.getTimeModified()).isEqualTo(UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateMailingListUserWithPatch() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        int databaseSizeBeforeUpdate = mailingListUserRepository.findAll().size();

        // Update the mailingListUser using partial update
        MailingListUser partialUpdatedMailingListUser = new MailingListUser();
        partialUpdatedMailingListUser.setId(mailingListUser.getId());

        partialUpdatedMailingListUser
            .username(UPDATED_USERNAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .status(UPDATED_STATUS)
            .timeCreated(UPDATED_TIME_CREATED)
            .timeModified(UPDATED_TIME_MODIFIED);

        restMailingListUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMailingListUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMailingListUser))
            )
            .andExpect(status().isOk());

        // Validate the MailingListUser in the database
        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeUpdate);
        MailingListUser testMailingListUser = mailingListUserList.get(mailingListUserList.size() - 1);
        assertThat(testMailingListUser.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testMailingListUser.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testMailingListUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testMailingListUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMailingListUser.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMailingListUser.getTimeCreated()).isEqualTo(UPDATED_TIME_CREATED);
        assertThat(testMailingListUser.getTimeModified()).isEqualTo(UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingMailingListUser() throws Exception {
        int databaseSizeBeforeUpdate = mailingListUserRepository.findAll().size();
        mailingListUser.setId(count.incrementAndGet());

        // Create the MailingListUser
        MailingListUserDTO mailingListUserDTO = mailingListUserMapper.toDto(mailingListUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMailingListUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mailingListUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mailingListUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MailingListUser in the database
        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMailingListUser() throws Exception {
        int databaseSizeBeforeUpdate = mailingListUserRepository.findAll().size();
        mailingListUser.setId(count.incrementAndGet());

        // Create the MailingListUser
        MailingListUserDTO mailingListUserDTO = mailingListUserMapper.toDto(mailingListUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMailingListUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mailingListUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MailingListUser in the database
        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMailingListUser() throws Exception {
        int databaseSizeBeforeUpdate = mailingListUserRepository.findAll().size();
        mailingListUser.setId(count.incrementAndGet());

        // Create the MailingListUser
        MailingListUserDTO mailingListUserDTO = mailingListUserMapper.toDto(mailingListUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMailingListUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mailingListUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MailingListUser in the database
        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMailingListUser() throws Exception {
        // Initialize the database
        mailingListUserRepository.saveAndFlush(mailingListUser);

        int databaseSizeBeforeDelete = mailingListUserRepository.findAll().size();

        // Delete the mailingListUser
        restMailingListUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, mailingListUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MailingListUser> mailingListUserList = mailingListUserRepository.findAll();
        assertThat(mailingListUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
