package com.looseboxes.mailinglist.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.looseboxes.mailinglist.service.IntegrationTest;
import com.looseboxes.mailinglist.service.domain.MailingList;
import com.looseboxes.mailinglist.service.domain.MailingListUser;
import com.looseboxes.mailinglist.service.domain.UserMailingList;
import com.looseboxes.mailinglist.service.domain.enumeration.MailingListUserStatus;
import com.looseboxes.mailinglist.service.repository.UserMailingListRepository;
import com.looseboxes.mailinglist.service.service.criteria.UserMailingListCriteria;
import com.looseboxes.mailinglist.service.service.dto.UserMailingListDTO;
import com.looseboxes.mailinglist.service.service.mapper.UserMailingListMapper;
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
 * Integration tests for the {@link UserMailingListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserMailingListResourceIT {

    private static final MailingListUserStatus DEFAULT_STATUS = MailingListUserStatus.Unverified;
    private static final MailingListUserStatus UPDATED_STATUS = MailingListUserStatus.Verified;

    private static final Instant DEFAULT_TIME_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TIME_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-mailing-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserMailingListRepository userMailingListRepository;

    @Autowired
    private UserMailingListMapper userMailingListMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserMailingListMockMvc;

    private UserMailingList userMailingList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMailingList createEntity(EntityManager em) {
        UserMailingList userMailingList = new UserMailingList()
            .status(DEFAULT_STATUS)
            .timeCreated(DEFAULT_TIME_CREATED)
            .timeModified(DEFAULT_TIME_MODIFIED);
        // Add required entity
        MailingListUser mailingListUser;
        if (TestUtil.findAll(em, MailingListUser.class).isEmpty()) {
            mailingListUser = MailingListUserResourceIT.createEntity(em);
            em.persist(mailingListUser);
            em.flush();
        } else {
            mailingListUser = TestUtil.findAll(em, MailingListUser.class).get(0);
        }
        userMailingList.setUser(mailingListUser);
        // Add required entity
        MailingList mailingList;
        if (TestUtil.findAll(em, MailingList.class).isEmpty()) {
            mailingList = MailingListResourceIT.createEntity(em);
            em.persist(mailingList);
            em.flush();
        } else {
            mailingList = TestUtil.findAll(em, MailingList.class).get(0);
        }
        userMailingList.setMailingList(mailingList);
        return userMailingList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMailingList createUpdatedEntity(EntityManager em) {
        UserMailingList userMailingList = new UserMailingList()
            .status(UPDATED_STATUS)
            .timeCreated(UPDATED_TIME_CREATED)
            .timeModified(UPDATED_TIME_MODIFIED);
        // Add required entity
        MailingListUser mailingListUser;
        if (TestUtil.findAll(em, MailingListUser.class).isEmpty()) {
            mailingListUser = MailingListUserResourceIT.createUpdatedEntity(em);
            em.persist(mailingListUser);
            em.flush();
        } else {
            mailingListUser = TestUtil.findAll(em, MailingListUser.class).get(0);
        }
        userMailingList.setUser(mailingListUser);
        // Add required entity
        MailingList mailingList;
        if (TestUtil.findAll(em, MailingList.class).isEmpty()) {
            mailingList = MailingListResourceIT.createUpdatedEntity(em);
            em.persist(mailingList);
            em.flush();
        } else {
            mailingList = TestUtil.findAll(em, MailingList.class).get(0);
        }
        userMailingList.setMailingList(mailingList);
        return userMailingList;
    }

    @BeforeEach
    public void initTest() {
        userMailingList = createEntity(em);
    }

    @Test
    @Transactional
    void createUserMailingList() throws Exception {
        int databaseSizeBeforeCreate = userMailingListRepository.findAll().size();
        // Create the UserMailingList
        UserMailingListDTO userMailingListDTO = userMailingListMapper.toDto(userMailingList);
        restUserMailingListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMailingListDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserMailingList in the database
        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeCreate + 1);
        UserMailingList testUserMailingList = userMailingListList.get(userMailingListList.size() - 1);
        assertThat(testUserMailingList.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUserMailingList.getTimeCreated()).isEqualTo(DEFAULT_TIME_CREATED);
        assertThat(testUserMailingList.getTimeModified()).isEqualTo(DEFAULT_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void createUserMailingListWithExistingId() throws Exception {
        // Create the UserMailingList with an existing ID
        userMailingList.setId(1L);
        UserMailingListDTO userMailingListDTO = userMailingListMapper.toDto(userMailingList);

        int databaseSizeBeforeCreate = userMailingListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMailingListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMailingListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMailingList in the database
        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMailingListRepository.findAll().size();
        // set the field null
        userMailingList.setStatus(null);

        // Create the UserMailingList, which fails.
        UserMailingListDTO userMailingListDTO = userMailingListMapper.toDto(userMailingList);

        restUserMailingListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMailingListDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMailingListRepository.findAll().size();
        // set the field null
        userMailingList.setTimeCreated(null);

        // Create the UserMailingList, which fails.
        UserMailingListDTO userMailingListDTO = userMailingListMapper.toDto(userMailingList);

        restUserMailingListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMailingListDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMailingListRepository.findAll().size();
        // set the field null
        userMailingList.setTimeModified(null);

        // Create the UserMailingList, which fails.
        UserMailingListDTO userMailingListDTO = userMailingListMapper.toDto(userMailingList);

        restUserMailingListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMailingListDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserMailingLists() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        // Get all the userMailingListList
        restUserMailingListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userMailingList.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].timeCreated").value(hasItem(DEFAULT_TIME_CREATED.toString())))
            .andExpect(jsonPath("$.[*].timeModified").value(hasItem(DEFAULT_TIME_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getUserMailingList() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        // Get the userMailingList
        restUserMailingListMockMvc
            .perform(get(ENTITY_API_URL_ID, userMailingList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userMailingList.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.timeCreated").value(DEFAULT_TIME_CREATED.toString()))
            .andExpect(jsonPath("$.timeModified").value(DEFAULT_TIME_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getUserMailingListsByIdFiltering() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        Long id = userMailingList.getId();

        defaultUserMailingListShouldBeFound("id.equals=" + id);
        defaultUserMailingListShouldNotBeFound("id.notEquals=" + id);

        defaultUserMailingListShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserMailingListShouldNotBeFound("id.greaterThan=" + id);

        defaultUserMailingListShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserMailingListShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserMailingListsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        // Get all the userMailingListList where status equals to DEFAULT_STATUS
        defaultUserMailingListShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the userMailingListList where status equals to UPDATED_STATUS
        defaultUserMailingListShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserMailingListsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        // Get all the userMailingListList where status not equals to DEFAULT_STATUS
        defaultUserMailingListShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the userMailingListList where status not equals to UPDATED_STATUS
        defaultUserMailingListShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserMailingListsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        // Get all the userMailingListList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultUserMailingListShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the userMailingListList where status equals to UPDATED_STATUS
        defaultUserMailingListShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserMailingListsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        // Get all the userMailingListList where status is not null
        defaultUserMailingListShouldBeFound("status.specified=true");

        // Get all the userMailingListList where status is null
        defaultUserMailingListShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllUserMailingListsByTimeCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        // Get all the userMailingListList where timeCreated equals to DEFAULT_TIME_CREATED
        defaultUserMailingListShouldBeFound("timeCreated.equals=" + DEFAULT_TIME_CREATED);

        // Get all the userMailingListList where timeCreated equals to UPDATED_TIME_CREATED
        defaultUserMailingListShouldNotBeFound("timeCreated.equals=" + UPDATED_TIME_CREATED);
    }

    @Test
    @Transactional
    void getAllUserMailingListsByTimeCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        // Get all the userMailingListList where timeCreated not equals to DEFAULT_TIME_CREATED
        defaultUserMailingListShouldNotBeFound("timeCreated.notEquals=" + DEFAULT_TIME_CREATED);

        // Get all the userMailingListList where timeCreated not equals to UPDATED_TIME_CREATED
        defaultUserMailingListShouldBeFound("timeCreated.notEquals=" + UPDATED_TIME_CREATED);
    }

    @Test
    @Transactional
    void getAllUserMailingListsByTimeCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        // Get all the userMailingListList where timeCreated in DEFAULT_TIME_CREATED or UPDATED_TIME_CREATED
        defaultUserMailingListShouldBeFound("timeCreated.in=" + DEFAULT_TIME_CREATED + "," + UPDATED_TIME_CREATED);

        // Get all the userMailingListList where timeCreated equals to UPDATED_TIME_CREATED
        defaultUserMailingListShouldNotBeFound("timeCreated.in=" + UPDATED_TIME_CREATED);
    }

    @Test
    @Transactional
    void getAllUserMailingListsByTimeCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        // Get all the userMailingListList where timeCreated is not null
        defaultUserMailingListShouldBeFound("timeCreated.specified=true");

        // Get all the userMailingListList where timeCreated is null
        defaultUserMailingListShouldNotBeFound("timeCreated.specified=false");
    }

    @Test
    @Transactional
    void getAllUserMailingListsByTimeModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        // Get all the userMailingListList where timeModified equals to DEFAULT_TIME_MODIFIED
        defaultUserMailingListShouldBeFound("timeModified.equals=" + DEFAULT_TIME_MODIFIED);

        // Get all the userMailingListList where timeModified equals to UPDATED_TIME_MODIFIED
        defaultUserMailingListShouldNotBeFound("timeModified.equals=" + UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void getAllUserMailingListsByTimeModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        // Get all the userMailingListList where timeModified not equals to DEFAULT_TIME_MODIFIED
        defaultUserMailingListShouldNotBeFound("timeModified.notEquals=" + DEFAULT_TIME_MODIFIED);

        // Get all the userMailingListList where timeModified not equals to UPDATED_TIME_MODIFIED
        defaultUserMailingListShouldBeFound("timeModified.notEquals=" + UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void getAllUserMailingListsByTimeModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        // Get all the userMailingListList where timeModified in DEFAULT_TIME_MODIFIED or UPDATED_TIME_MODIFIED
        defaultUserMailingListShouldBeFound("timeModified.in=" + DEFAULT_TIME_MODIFIED + "," + UPDATED_TIME_MODIFIED);

        // Get all the userMailingListList where timeModified equals to UPDATED_TIME_MODIFIED
        defaultUserMailingListShouldNotBeFound("timeModified.in=" + UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void getAllUserMailingListsByTimeModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        // Get all the userMailingListList where timeModified is not null
        defaultUserMailingListShouldBeFound("timeModified.specified=true");

        // Get all the userMailingListList where timeModified is null
        defaultUserMailingListShouldNotBeFound("timeModified.specified=false");
    }

    @Test
    @Transactional
    void getAllUserMailingListsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);
        MailingListUser user = MailingListUserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userMailingList.setUser(user);
        userMailingListRepository.saveAndFlush(userMailingList);
        Long userId = user.getId();

        // Get all the userMailingListList where user equals to userId
        defaultUserMailingListShouldBeFound("userId.equals=" + userId);

        // Get all the userMailingListList where user equals to (userId + 1)
        defaultUserMailingListShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllUserMailingListsByMailingListIsEqualToSomething() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);
        MailingList mailingList = MailingListResourceIT.createEntity(em);
        em.persist(mailingList);
        em.flush();
        userMailingList.setMailingList(mailingList);
        userMailingListRepository.saveAndFlush(userMailingList);
        Long mailingListId = mailingList.getId();

        // Get all the userMailingListList where mailingList equals to mailingListId
        defaultUserMailingListShouldBeFound("mailingListId.equals=" + mailingListId);

        // Get all the userMailingListList where mailingList equals to (mailingListId + 1)
        defaultUserMailingListShouldNotBeFound("mailingListId.equals=" + (mailingListId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserMailingListShouldBeFound(String filter) throws Exception {
        restUserMailingListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userMailingList.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].timeCreated").value(hasItem(DEFAULT_TIME_CREATED.toString())))
            .andExpect(jsonPath("$.[*].timeModified").value(hasItem(DEFAULT_TIME_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restUserMailingListMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserMailingListShouldNotBeFound(String filter) throws Exception {
        restUserMailingListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserMailingListMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserMailingList() throws Exception {
        // Get the userMailingList
        restUserMailingListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserMailingList() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        int databaseSizeBeforeUpdate = userMailingListRepository.findAll().size();

        // Update the userMailingList
        UserMailingList updatedUserMailingList = userMailingListRepository.findById(userMailingList.getId()).get();
        // Disconnect from session so that the updates on updatedUserMailingList are not directly saved in db
        em.detach(updatedUserMailingList);
        updatedUserMailingList.status(UPDATED_STATUS).timeCreated(UPDATED_TIME_CREATED).timeModified(UPDATED_TIME_MODIFIED);
        UserMailingListDTO userMailingListDTO = userMailingListMapper.toDto(updatedUserMailingList);

        restUserMailingListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userMailingListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userMailingListDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserMailingList in the database
        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeUpdate);
        UserMailingList testUserMailingList = userMailingListList.get(userMailingListList.size() - 1);
        assertThat(testUserMailingList.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserMailingList.getTimeCreated()).isEqualTo(UPDATED_TIME_CREATED);
        assertThat(testUserMailingList.getTimeModified()).isEqualTo(UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingUserMailingList() throws Exception {
        int databaseSizeBeforeUpdate = userMailingListRepository.findAll().size();
        userMailingList.setId(count.incrementAndGet());

        // Create the UserMailingList
        UserMailingListDTO userMailingListDTO = userMailingListMapper.toDto(userMailingList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMailingListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userMailingListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userMailingListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMailingList in the database
        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserMailingList() throws Exception {
        int databaseSizeBeforeUpdate = userMailingListRepository.findAll().size();
        userMailingList.setId(count.incrementAndGet());

        // Create the UserMailingList
        UserMailingListDTO userMailingListDTO = userMailingListMapper.toDto(userMailingList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMailingListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userMailingListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMailingList in the database
        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserMailingList() throws Exception {
        int databaseSizeBeforeUpdate = userMailingListRepository.findAll().size();
        userMailingList.setId(count.incrementAndGet());

        // Create the UserMailingList
        UserMailingListDTO userMailingListDTO = userMailingListMapper.toDto(userMailingList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMailingListMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMailingListDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserMailingList in the database
        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserMailingListWithPatch() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        int databaseSizeBeforeUpdate = userMailingListRepository.findAll().size();

        // Update the userMailingList using partial update
        UserMailingList partialUpdatedUserMailingList = new UserMailingList();
        partialUpdatedUserMailingList.setId(userMailingList.getId());

        partialUpdatedUserMailingList.status(UPDATED_STATUS).timeCreated(UPDATED_TIME_CREATED);

        restUserMailingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserMailingList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserMailingList))
            )
            .andExpect(status().isOk());

        // Validate the UserMailingList in the database
        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeUpdate);
        UserMailingList testUserMailingList = userMailingListList.get(userMailingListList.size() - 1);
        assertThat(testUserMailingList.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserMailingList.getTimeCreated()).isEqualTo(UPDATED_TIME_CREATED);
        assertThat(testUserMailingList.getTimeModified()).isEqualTo(DEFAULT_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateUserMailingListWithPatch() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        int databaseSizeBeforeUpdate = userMailingListRepository.findAll().size();

        // Update the userMailingList using partial update
        UserMailingList partialUpdatedUserMailingList = new UserMailingList();
        partialUpdatedUserMailingList.setId(userMailingList.getId());

        partialUpdatedUserMailingList.status(UPDATED_STATUS).timeCreated(UPDATED_TIME_CREATED).timeModified(UPDATED_TIME_MODIFIED);

        restUserMailingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserMailingList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserMailingList))
            )
            .andExpect(status().isOk());

        // Validate the UserMailingList in the database
        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeUpdate);
        UserMailingList testUserMailingList = userMailingListList.get(userMailingListList.size() - 1);
        assertThat(testUserMailingList.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserMailingList.getTimeCreated()).isEqualTo(UPDATED_TIME_CREATED);
        assertThat(testUserMailingList.getTimeModified()).isEqualTo(UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingUserMailingList() throws Exception {
        int databaseSizeBeforeUpdate = userMailingListRepository.findAll().size();
        userMailingList.setId(count.incrementAndGet());

        // Create the UserMailingList
        UserMailingListDTO userMailingListDTO = userMailingListMapper.toDto(userMailingList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMailingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userMailingListDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userMailingListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMailingList in the database
        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserMailingList() throws Exception {
        int databaseSizeBeforeUpdate = userMailingListRepository.findAll().size();
        userMailingList.setId(count.incrementAndGet());

        // Create the UserMailingList
        UserMailingListDTO userMailingListDTO = userMailingListMapper.toDto(userMailingList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMailingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userMailingListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMailingList in the database
        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserMailingList() throws Exception {
        int databaseSizeBeforeUpdate = userMailingListRepository.findAll().size();
        userMailingList.setId(count.incrementAndGet());

        // Create the UserMailingList
        UserMailingListDTO userMailingListDTO = userMailingListMapper.toDto(userMailingList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMailingListMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userMailingListDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserMailingList in the database
        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserMailingList() throws Exception {
        // Initialize the database
        userMailingListRepository.saveAndFlush(userMailingList);

        int databaseSizeBeforeDelete = userMailingListRepository.findAll().size();

        // Delete the userMailingList
        restUserMailingListMockMvc
            .perform(delete(ENTITY_API_URL_ID, userMailingList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserMailingList> userMailingListList = userMailingListRepository.findAll();
        assertThat(userMailingListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
