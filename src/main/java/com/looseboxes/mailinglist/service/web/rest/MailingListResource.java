package com.looseboxes.mailinglist.service.web.rest;

import com.looseboxes.mailinglist.service.repository.MailingListRepository;
import com.looseboxes.mailinglist.service.service.MailingListQueryService;
import com.looseboxes.mailinglist.service.service.MailingListService;
import com.looseboxes.mailinglist.service.service.criteria.MailingListCriteria;
import com.looseboxes.mailinglist.service.service.dto.MailingListDTO;
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
 * REST controller for managing {@link com.looseboxes.mailinglist.service.domain.MailingList}.
 */
@RestController
@RequestMapping("/api")
public class MailingListResource {

    private final Logger log = LoggerFactory.getLogger(MailingListResource.class);

    private static final String ENTITY_NAME = "mailingList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MailingListService mailingListService;

    private final MailingListRepository mailingListRepository;

    private final MailingListQueryService mailingListQueryService;

    public MailingListResource(
        MailingListService mailingListService,
        MailingListRepository mailingListRepository,
        MailingListQueryService mailingListQueryService
    ) {
        this.mailingListService = mailingListService;
        this.mailingListRepository = mailingListRepository;
        this.mailingListQueryService = mailingListQueryService;
    }

    /**
     * {@code POST  /mailing-lists} : Create a new mailingList.
     *
     * @param mailingListDTO the mailingListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mailingListDTO, or with status {@code 400 (Bad Request)} if the mailingList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mailing-lists")
    public ResponseEntity<MailingListDTO> createMailingList(@Valid @RequestBody MailingListDTO mailingListDTO) throws URISyntaxException {
        log.debug("REST request to save MailingList : {}", mailingListDTO);
        if (mailingListDTO.getId() != null) {
            throw new BadRequestAlertException("A new mailingList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MailingListDTO result = mailingListService.save(mailingListDTO);
        return ResponseEntity
            .created(new URI("/api/mailing-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mailing-lists/:id} : Updates an existing mailingList.
     *
     * @param id the id of the mailingListDTO to save.
     * @param mailingListDTO the mailingListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mailingListDTO,
     * or with status {@code 400 (Bad Request)} if the mailingListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mailingListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mailing-lists/{id}")
    public ResponseEntity<MailingListDTO> updateMailingList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MailingListDTO mailingListDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MailingList : {}, {}", id, mailingListDTO);
        if (mailingListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mailingListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mailingListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MailingListDTO result = mailingListService.save(mailingListDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mailingListDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mailing-lists/:id} : Partial updates given fields of an existing mailingList, field will ignore if it is null
     *
     * @param id the id of the mailingListDTO to save.
     * @param mailingListDTO the mailingListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mailingListDTO,
     * or with status {@code 400 (Bad Request)} if the mailingListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mailingListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mailingListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mailing-lists/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MailingListDTO> partialUpdateMailingList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MailingListDTO mailingListDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MailingList partially : {}, {}", id, mailingListDTO);
        if (mailingListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mailingListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mailingListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MailingListDTO> result = mailingListService.partialUpdate(mailingListDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mailingListDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mailing-lists} : get all the mailingLists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mailingLists in body.
     */
    @GetMapping("/mailing-lists")
    public ResponseEntity<List<MailingListDTO>> getAllMailingLists(MailingListCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MailingLists by criteria: {}", criteria);
        Page<MailingListDTO> page = mailingListQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mailing-lists/count} : count all the mailingLists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/mailing-lists/count")
    public ResponseEntity<Long> countMailingLists(MailingListCriteria criteria) {
        log.debug("REST request to count MailingLists by criteria: {}", criteria);
        return ResponseEntity.ok().body(mailingListQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mailing-lists/:id} : get the "id" mailingList.
     *
     * @param id the id of the mailingListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mailingListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mailing-lists/{id}")
    public ResponseEntity<MailingListDTO> getMailingList(@PathVariable Long id) {
        log.debug("REST request to get MailingList : {}", id);
        Optional<MailingListDTO> mailingListDTO = mailingListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mailingListDTO);
    }

    /**
     * {@code DELETE  /mailing-lists/:id} : delete the "id" mailingList.
     *
     * @param id the id of the mailingListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mailing-lists/{id}")
    public ResponseEntity<Void> deleteMailingList(@PathVariable Long id) {
        log.debug("REST request to delete MailingList : {}", id);
        mailingListService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
