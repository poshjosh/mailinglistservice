package com.looseboxes.mailinglist.service.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.looseboxes.mailinglist.service.IntegrationTest;
import com.looseboxes.mailinglist.service.domain.MailingList;
import com.looseboxes.mailinglist.service.repository.MailingListRepository;
import com.looseboxes.mailinglist.service.service.criteria.MailingListCriteria;
import com.looseboxes.mailinglist.service.service.dto.MailingListDTO;
import com.looseboxes.mailinglist.service.service.mapper.MailingListMapper;
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
 * Integration tests for the {@link MailingListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MailingListResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIME_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TIME_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/mailing-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MailingListRepository mailingListRepository;

    @Autowired
    private MailingListMapper mailingListMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMailingListMockMvc;

    private MailingList mailingList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MailingList createEntity(EntityManager em) {
        MailingList mailingList = new MailingList()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .timeCreated(DEFAULT_TIME_CREATED)
            .timeModified(DEFAULT_TIME_MODIFIED);
        return mailingList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MailingList createUpdatedEntity(EntityManager em) {
        MailingList mailingList = new MailingList()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .timeCreated(UPDATED_TIME_CREATED)
            .timeModified(UPDATED_TIME_MODIFIED);
        return mailingList;
    }

    @BeforeEach
    public void initTest() {
        mailingList = createEntity(em);
    }

    @Test
    @Transactional
    void createMailingList() throws Exception {
        int databaseSizeBeforeCreate = mailingListRepository.findAll().size();
        // Create the MailingList
        MailingListDTO mailingListDTO = mailingListMapper.toDto(mailingList);
        restMailingListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mailingListDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MailingList in the database
        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeCreate + 1);
        MailingList testMailingList = mailingListList.get(mailingListList.size() - 1);
        assertThat(testMailingList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMailingList.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMailingList.getTimeCreated()).isEqualTo(DEFAULT_TIME_CREATED);
        assertThat(testMailingList.getTimeModified()).isEqualTo(DEFAULT_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void createMailingListWithExistingId() throws Exception {
        // Create the MailingList with an existing ID
        mailingList.setId(1L);
        MailingListDTO mailingListDTO = mailingListMapper.toDto(mailingList);

        int databaseSizeBeforeCreate = mailingListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMailingListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mailingListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MailingList in the database
        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailingListRepository.findAll().size();
        // set the field null
        mailingList.setName(null);

        // Create the MailingList, which fails.
        MailingListDTO mailingListDTO = mailingListMapper.toDto(mailingList);

        restMailingListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mailingListDTO))
            )
            .andExpect(status().isBadRequest());

        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailingListRepository.findAll().size();
        // set the field null
        mailingList.setTimeCreated(null);

        // Create the MailingList, which fails.
        MailingListDTO mailingListDTO = mailingListMapper.toDto(mailingList);

        restMailingListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mailingListDTO))
            )
            .andExpect(status().isBadRequest());

        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = mailingListRepository.findAll().size();
        // set the field null
        mailingList.setTimeModified(null);

        // Create the MailingList, which fails.
        MailingListDTO mailingListDTO = mailingListMapper.toDto(mailingList);

        restMailingListMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mailingListDTO))
            )
            .andExpect(status().isBadRequest());

        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMailingLists() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList
        restMailingListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mailingList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].timeCreated").value(hasItem(DEFAULT_TIME_CREATED.toString())))
            .andExpect(jsonPath("$.[*].timeModified").value(hasItem(DEFAULT_TIME_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getMailingList() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get the mailingList
        restMailingListMockMvc
            .perform(get(ENTITY_API_URL_ID, mailingList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mailingList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.timeCreated").value(DEFAULT_TIME_CREATED.toString()))
            .andExpect(jsonPath("$.timeModified").value(DEFAULT_TIME_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getMailingListsByIdFiltering() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        Long id = mailingList.getId();

        defaultMailingListShouldBeFound("id.equals=" + id);
        defaultMailingListShouldNotBeFound("id.notEquals=" + id);

        defaultMailingListShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMailingListShouldNotBeFound("id.greaterThan=" + id);

        defaultMailingListShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMailingListShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMailingListsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where name equals to DEFAULT_NAME
        defaultMailingListShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the mailingListList where name equals to UPDATED_NAME
        defaultMailingListShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where name not equals to DEFAULT_NAME
        defaultMailingListShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the mailingListList where name not equals to UPDATED_NAME
        defaultMailingListShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMailingListShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the mailingListList where name equals to UPDATED_NAME
        defaultMailingListShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where name is not null
        defaultMailingListShouldBeFound("name.specified=true");

        // Get all the mailingListList where name is null
        defaultMailingListShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMailingListsByNameContainsSomething() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where name contains DEFAULT_NAME
        defaultMailingListShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the mailingListList where name contains UPDATED_NAME
        defaultMailingListShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where name does not contain DEFAULT_NAME
        defaultMailingListShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the mailingListList where name does not contain UPDATED_NAME
        defaultMailingListShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMailingListsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where description equals to DEFAULT_DESCRIPTION
        defaultMailingListShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the mailingListList where description equals to UPDATED_DESCRIPTION
        defaultMailingListShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMailingListsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where description not equals to DEFAULT_DESCRIPTION
        defaultMailingListShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the mailingListList where description not equals to UPDATED_DESCRIPTION
        defaultMailingListShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMailingListsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMailingListShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the mailingListList where description equals to UPDATED_DESCRIPTION
        defaultMailingListShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMailingListsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where description is not null
        defaultMailingListShouldBeFound("description.specified=true");

        // Get all the mailingListList where description is null
        defaultMailingListShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMailingListsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where description contains DEFAULT_DESCRIPTION
        defaultMailingListShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the mailingListList where description contains UPDATED_DESCRIPTION
        defaultMailingListShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMailingListsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where description does not contain DEFAULT_DESCRIPTION
        defaultMailingListShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the mailingListList where description does not contain UPDATED_DESCRIPTION
        defaultMailingListShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMailingListsByTimeCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where timeCreated equals to DEFAULT_TIME_CREATED
        defaultMailingListShouldBeFound("timeCreated.equals=" + DEFAULT_TIME_CREATED);

        // Get all the mailingListList where timeCreated equals to UPDATED_TIME_CREATED
        defaultMailingListShouldNotBeFound("timeCreated.equals=" + UPDATED_TIME_CREATED);
    }

    @Test
    @Transactional
    void getAllMailingListsByTimeCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where timeCreated not equals to DEFAULT_TIME_CREATED
        defaultMailingListShouldNotBeFound("timeCreated.notEquals=" + DEFAULT_TIME_CREATED);

        // Get all the mailingListList where timeCreated not equals to UPDATED_TIME_CREATED
        defaultMailingListShouldBeFound("timeCreated.notEquals=" + UPDATED_TIME_CREATED);
    }

    @Test
    @Transactional
    void getAllMailingListsByTimeCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where timeCreated in DEFAULT_TIME_CREATED or UPDATED_TIME_CREATED
        defaultMailingListShouldBeFound("timeCreated.in=" + DEFAULT_TIME_CREATED + "," + UPDATED_TIME_CREATED);

        // Get all the mailingListList where timeCreated equals to UPDATED_TIME_CREATED
        defaultMailingListShouldNotBeFound("timeCreated.in=" + UPDATED_TIME_CREATED);
    }

    @Test
    @Transactional
    void getAllMailingListsByTimeCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where timeCreated is not null
        defaultMailingListShouldBeFound("timeCreated.specified=true");

        // Get all the mailingListList where timeCreated is null
        defaultMailingListShouldNotBeFound("timeCreated.specified=false");
    }

    @Test
    @Transactional
    void getAllMailingListsByTimeModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where timeModified equals to DEFAULT_TIME_MODIFIED
        defaultMailingListShouldBeFound("timeModified.equals=" + DEFAULT_TIME_MODIFIED);

        // Get all the mailingListList where timeModified equals to UPDATED_TIME_MODIFIED
        defaultMailingListShouldNotBeFound("timeModified.equals=" + UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void getAllMailingListsByTimeModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where timeModified not equals to DEFAULT_TIME_MODIFIED
        defaultMailingListShouldNotBeFound("timeModified.notEquals=" + DEFAULT_TIME_MODIFIED);

        // Get all the mailingListList where timeModified not equals to UPDATED_TIME_MODIFIED
        defaultMailingListShouldBeFound("timeModified.notEquals=" + UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void getAllMailingListsByTimeModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where timeModified in DEFAULT_TIME_MODIFIED or UPDATED_TIME_MODIFIED
        defaultMailingListShouldBeFound("timeModified.in=" + DEFAULT_TIME_MODIFIED + "," + UPDATED_TIME_MODIFIED);

        // Get all the mailingListList where timeModified equals to UPDATED_TIME_MODIFIED
        defaultMailingListShouldNotBeFound("timeModified.in=" + UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void getAllMailingListsByTimeModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingListList where timeModified is not null
        defaultMailingListShouldBeFound("timeModified.specified=true");

        // Get all the mailingListList where timeModified is null
        defaultMailingListShouldNotBeFound("timeModified.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMailingListShouldBeFound(String filter) throws Exception {
        restMailingListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mailingList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].timeCreated").value(hasItem(DEFAULT_TIME_CREATED.toString())))
            .andExpect(jsonPath("$.[*].timeModified").value(hasItem(DEFAULT_TIME_MODIFIED.toString())));

        // Check, that the count call also returns 1
        restMailingListMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMailingListShouldNotBeFound(String filter) throws Exception {
        restMailingListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMailingListMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMailingList() throws Exception {
        // Get the mailingList
        restMailingListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMailingList() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        int databaseSizeBeforeUpdate = mailingListRepository.findAll().size();

        // Update the mailingList
        MailingList updatedMailingList = mailingListRepository.findById(mailingList.getId()).get();
        // Disconnect from session so that the updates on updatedMailingList are not directly saved in db
        em.detach(updatedMailingList);
        updatedMailingList
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .timeCreated(UPDATED_TIME_CREATED)
            .timeModified(UPDATED_TIME_MODIFIED);
        MailingListDTO mailingListDTO = mailingListMapper.toDto(updatedMailingList);

        restMailingListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mailingListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mailingListDTO))
            )
            .andExpect(status().isOk());

        // Validate the MailingList in the database
        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeUpdate);
        MailingList testMailingList = mailingListList.get(mailingListList.size() - 1);
        assertThat(testMailingList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMailingList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMailingList.getTimeCreated()).isEqualTo(UPDATED_TIME_CREATED);
        assertThat(testMailingList.getTimeModified()).isEqualTo(UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingMailingList() throws Exception {
        int databaseSizeBeforeUpdate = mailingListRepository.findAll().size();
        mailingList.setId(count.incrementAndGet());

        // Create the MailingList
        MailingListDTO mailingListDTO = mailingListMapper.toDto(mailingList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMailingListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mailingListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mailingListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MailingList in the database
        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMailingList() throws Exception {
        int databaseSizeBeforeUpdate = mailingListRepository.findAll().size();
        mailingList.setId(count.incrementAndGet());

        // Create the MailingList
        MailingListDTO mailingListDTO = mailingListMapper.toDto(mailingList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMailingListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mailingListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MailingList in the database
        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMailingList() throws Exception {
        int databaseSizeBeforeUpdate = mailingListRepository.findAll().size();
        mailingList.setId(count.incrementAndGet());

        // Create the MailingList
        MailingListDTO mailingListDTO = mailingListMapper.toDto(mailingList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMailingListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mailingListDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MailingList in the database
        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMailingListWithPatch() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        int databaseSizeBeforeUpdate = mailingListRepository.findAll().size();

        // Update the mailingList using partial update
        MailingList partialUpdatedMailingList = new MailingList();
        partialUpdatedMailingList.setId(mailingList.getId());

        partialUpdatedMailingList.name(UPDATED_NAME);

        restMailingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMailingList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMailingList))
            )
            .andExpect(status().isOk());

        // Validate the MailingList in the database
        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeUpdate);
        MailingList testMailingList = mailingListList.get(mailingListList.size() - 1);
        assertThat(testMailingList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMailingList.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMailingList.getTimeCreated()).isEqualTo(DEFAULT_TIME_CREATED);
        assertThat(testMailingList.getTimeModified()).isEqualTo(DEFAULT_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateMailingListWithPatch() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        int databaseSizeBeforeUpdate = mailingListRepository.findAll().size();

        // Update the mailingList using partial update
        MailingList partialUpdatedMailingList = new MailingList();
        partialUpdatedMailingList.setId(mailingList.getId());

        partialUpdatedMailingList
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .timeCreated(UPDATED_TIME_CREATED)
            .timeModified(UPDATED_TIME_MODIFIED);

        restMailingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMailingList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMailingList))
            )
            .andExpect(status().isOk());

        // Validate the MailingList in the database
        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeUpdate);
        MailingList testMailingList = mailingListList.get(mailingListList.size() - 1);
        assertThat(testMailingList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMailingList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMailingList.getTimeCreated()).isEqualTo(UPDATED_TIME_CREATED);
        assertThat(testMailingList.getTimeModified()).isEqualTo(UPDATED_TIME_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingMailingList() throws Exception {
        int databaseSizeBeforeUpdate = mailingListRepository.findAll().size();
        mailingList.setId(count.incrementAndGet());

        // Create the MailingList
        MailingListDTO mailingListDTO = mailingListMapper.toDto(mailingList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMailingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mailingListDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mailingListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MailingList in the database
        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMailingList() throws Exception {
        int databaseSizeBeforeUpdate = mailingListRepository.findAll().size();
        mailingList.setId(count.incrementAndGet());

        // Create the MailingList
        MailingListDTO mailingListDTO = mailingListMapper.toDto(mailingList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMailingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mailingListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MailingList in the database
        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMailingList() throws Exception {
        int databaseSizeBeforeUpdate = mailingListRepository.findAll().size();
        mailingList.setId(count.incrementAndGet());

        // Create the MailingList
        MailingListDTO mailingListDTO = mailingListMapper.toDto(mailingList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMailingListMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mailingListDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MailingList in the database
        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMailingList() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        int databaseSizeBeforeDelete = mailingListRepository.findAll().size();

        // Delete the mailingList
        restMailingListMockMvc
            .perform(delete(ENTITY_API_URL_ID, mailingList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MailingList> mailingListList = mailingListRepository.findAll();
        assertThat(mailingListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
