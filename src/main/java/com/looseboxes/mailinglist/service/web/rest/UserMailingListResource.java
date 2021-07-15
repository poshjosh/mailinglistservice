package com.looseboxes.mailinglist.service.web.rest;

import com.looseboxes.mailinglist.service.repository.UserMailingListRepository;
import com.looseboxes.mailinglist.service.service.UserMailingListQueryService;
import com.looseboxes.mailinglist.service.service.UserMailingListService;
import com.looseboxes.mailinglist.service.service.criteria.UserMailingListCriteria;
import com.looseboxes.mailinglist.service.service.dto.UserMailingListDTO;
import com.looseboxes.mailinglist.service.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.looseboxes.mailinglist.service.domain.UserMailingList}.
 */
@RestController
@RequestMapping("/api")
public class UserMailingListResource {

    private final Logger log = LoggerFactory.getLogger(UserMailingListResource.class);

    private static final String ENTITY_NAME = "userMailingList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserMailingListService userMailingListService;

    private final UserMailingListRepository userMailingListRepository;

    private final UserMailingListQueryService userMailingListQueryService;

    public UserMailingListResource(
        UserMailingListService userMailingListService,
        UserMailingListRepository userMailingListRepository,
        UserMailingListQueryService userMailingListQueryService
    ) {
        this.userMailingListService = userMailingListService;
        this.userMailingListRepository = userMailingListRepository;
        this.userMailingListQueryService = userMailingListQueryService;
    }

    /**
     * {@code POST  /user-mailing-lists} : Create a new userMailingList.
     *
     * @param userMailingListDTO the userMailingListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userMailingListDTO, or with status {@code 400 (Bad Request)} if the userMailingList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-mailing-lists")
    public ResponseEntity<UserMailingListDTO> createUserMailingList(@Valid @RequestBody UserMailingListDTO userMailingListDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserMailingList : {}", userMailingListDTO);
        if (userMailingListDTO.getId() != null) {
            throw new BadRequestAlertException("A new userMailingList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserMailingListDTO result = userMailingListService.save(userMailingListDTO);
        return ResponseEntity
            .created(new URI("/api/user-mailing-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-mailing-lists/:id} : Updates an existing userMailingList.
     *
     * @param id the id of the userMailingListDTO to save.
     * @param userMailingListDTO the userMailingListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userMailingListDTO,
     * or with status {@code 400 (Bad Request)} if the userMailingListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userMailingListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-mailing-lists/{id}")
    public ResponseEntity<UserMailingListDTO> updateUserMailingList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserMailingListDTO userMailingListDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserMailingList : {}, {}", id, userMailingListDTO);
        if (userMailingListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userMailingListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userMailingListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserMailingListDTO result = userMailingListService.save(userMailingListDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userMailingListDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-mailing-lists/:id} : Partial updates given fields of an existing userMailingList, field will ignore if it is null
     *
     * @param id the id of the userMailingListDTO to save.
     * @param userMailingListDTO the userMailingListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userMailingListDTO,
     * or with status {@code 400 (Bad Request)} if the userMailingListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userMailingListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userMailingListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-mailing-lists/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserMailingListDTO> partialUpdateUserMailingList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserMailingListDTO userMailingListDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserMailingList partially : {}, {}", id, userMailingListDTO);
        if (userMailingListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userMailingListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userMailingListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserMailingListDTO> result = userMailingListService.partialUpdate(userMailingListDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userMailingListDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-mailing-lists} : get all the userMailingLists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userMailingLists in body.
     */
    @GetMapping("/user-mailing-lists")
    public ResponseEntity<List<UserMailingListDTO>> getAllUserMailingLists(UserMailingListCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UserMailingLists by criteria: {}", criteria);
        Page<UserMailingListDTO> page = userMailingListQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-mailing-lists/count} : count all the userMailingLists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/user-mailing-lists/count")
    public ResponseEntity<Long> countUserMailingLists(UserMailingListCriteria criteria) {
        log.debug("REST request to count UserMailingLists by criteria: {}", criteria);
        return ResponseEntity.ok().body(userMailingListQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-mailing-lists/:id} : get the "id" userMailingList.
     *
     * @param id the id of the userMailingListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userMailingListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-mailing-lists/{id}")
    public ResponseEntity<UserMailingListDTO> getUserMailingList(@PathVariable Long id) {
        log.debug("REST request to get UserMailingList : {}", id);
        Optional<UserMailingListDTO> userMailingListDTO = userMailingListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userMailingListDTO);
    }

    /**
     * {@code DELETE  /user-mailing-lists/:id} : delete the "id" userMailingList.
     *
     * @param id the id of the userMailingListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-mailing-lists/{id}")
    public ResponseEntity<Void> deleteUserMailingList(@PathVariable Long id) {
        log.debug("REST request to delete UserMailingList : {}", id);
        userMailingListService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
