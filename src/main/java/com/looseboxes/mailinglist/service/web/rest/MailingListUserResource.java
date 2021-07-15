package com.looseboxes.mailinglist.service.web.rest;

import com.looseboxes.mailinglist.service.repository.MailingListUserRepository;
import com.looseboxes.mailinglist.service.service.MailingListUserQueryService;
import com.looseboxes.mailinglist.service.service.MailingListUserService;
import com.looseboxes.mailinglist.service.service.criteria.MailingListUserCriteria;
import com.looseboxes.mailinglist.service.service.dto.MailingListUserDTO;
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
 * REST controller for managing {@link com.looseboxes.mailinglist.service.domain.MailingListUser}.
 */
@RestController
@RequestMapping("/api")
public class MailingListUserResource {

    private final Logger log = LoggerFactory.getLogger(MailingListUserResource.class);

    private static final String ENTITY_NAME = "mailingListUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MailingListUserService mailingListUserService;

    private final MailingListUserRepository mailingListUserRepository;

    private final MailingListUserQueryService mailingListUserQueryService;

    public MailingListUserResource(
        MailingListUserService mailingListUserService,
        MailingListUserRepository mailingListUserRepository,
        MailingListUserQueryService mailingListUserQueryService
    ) {
        this.mailingListUserService = mailingListUserService;
        this.mailingListUserRepository = mailingListUserRepository;
        this.mailingListUserQueryService = mailingListUserQueryService;
    }

    /**
     * {@code POST  /mailing-list-users} : Create a new mailingListUser.
     *
     * @param mailingListUserDTO the mailingListUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mailingListUserDTO, or with status {@code 400 (Bad Request)} if the mailingListUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mailing-list-users")
    public ResponseEntity<MailingListUserDTO> createMailingListUser(@Valid @RequestBody MailingListUserDTO mailingListUserDTO)
        throws URISyntaxException {
        log.debug("REST request to save MailingListUser : {}", mailingListUserDTO);
        if (mailingListUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new mailingListUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MailingListUserDTO result = mailingListUserService.save(mailingListUserDTO);
        return ResponseEntity
            .created(new URI("/api/mailing-list-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mailing-list-users/:id} : Updates an existing mailingListUser.
     *
     * @param id the id of the mailingListUserDTO to save.
     * @param mailingListUserDTO the mailingListUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mailingListUserDTO,
     * or with status {@code 400 (Bad Request)} if the mailingListUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mailingListUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mailing-list-users/{id}")
    public ResponseEntity<MailingListUserDTO> updateMailingListUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MailingListUserDTO mailingListUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MailingListUser : {}, {}", id, mailingListUserDTO);
        if (mailingListUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mailingListUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mailingListUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MailingListUserDTO result = mailingListUserService.save(mailingListUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mailingListUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mailing-list-users/:id} : Partial updates given fields of an existing mailingListUser, field will ignore if it is null
     *
     * @param id the id of the mailingListUserDTO to save.
     * @param mailingListUserDTO the mailingListUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mailingListUserDTO,
     * or with status {@code 400 (Bad Request)} if the mailingListUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mailingListUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mailingListUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mailing-list-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MailingListUserDTO> partialUpdateMailingListUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MailingListUserDTO mailingListUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MailingListUser partially : {}, {}", id, mailingListUserDTO);
        if (mailingListUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mailingListUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mailingListUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MailingListUserDTO> result = mailingListUserService.partialUpdate(mailingListUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mailingListUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mailing-list-users} : get all the mailingListUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mailingListUsers in body.
     */
    @GetMapping("/mailing-list-users")
    public ResponseEntity<List<MailingListUserDTO>> getAllMailingListUsers(MailingListUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MailingListUsers by criteria: {}", criteria);
        Page<MailingListUserDTO> page = mailingListUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mailing-list-users/count} : count all the mailingListUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/mailing-list-users/count")
    public ResponseEntity<Long> countMailingListUsers(MailingListUserCriteria criteria) {
        log.debug("REST request to count MailingListUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(mailingListUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mailing-list-users/:id} : get the "id" mailingListUser.
     *
     * @param id the id of the mailingListUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mailingListUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mailing-list-users/{id}")
    public ResponseEntity<MailingListUserDTO> getMailingListUser(@PathVariable Long id) {
        log.debug("REST request to get MailingListUser : {}", id);
        Optional<MailingListUserDTO> mailingListUserDTO = mailingListUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mailingListUserDTO);
    }

    /**
     * {@code DELETE  /mailing-list-users/:id} : delete the "id" mailingListUser.
     *
     * @param id the id of the mailingListUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mailing-list-users/{id}")
    public ResponseEntity<Void> deleteMailingListUser(@PathVariable Long id) {
        log.debug("REST request to delete MailingListUser : {}", id);
        mailingListUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
