package com.looseboxes.mailinglist.service.service;

import com.looseboxes.mailinglist.service.domain.*; // for static metamodels
import com.looseboxes.mailinglist.service.domain.MailingList;
import com.looseboxes.mailinglist.service.repository.MailingListRepository;
import com.looseboxes.mailinglist.service.service.criteria.MailingListCriteria;
import com.looseboxes.mailinglist.service.service.dto.MailingListDTO;
import com.looseboxes.mailinglist.service.service.mapper.MailingListMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MailingList} entities in the database.
 * The main input is a {@link MailingListCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MailingListDTO} or a {@link Page} of {@link MailingListDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MailingListQueryService extends QueryService<MailingList> {

    private final Logger log = LoggerFactory.getLogger(MailingListQueryService.class);

    private final MailingListRepository mailingListRepository;

    private final MailingListMapper mailingListMapper;

    public MailingListQueryService(MailingListRepository mailingListRepository, MailingListMapper mailingListMapper) {
        this.mailingListRepository = mailingListRepository;
        this.mailingListMapper = mailingListMapper;
    }

    /**
     * Return a {@link List} of {@link MailingListDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MailingListDTO> findByCriteria(MailingListCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MailingList> specification = createSpecification(criteria);
        return mailingListMapper.toDto(mailingListRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MailingListDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MailingListDTO> findByCriteria(MailingListCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MailingList> specification = createSpecification(criteria);
        return mailingListRepository.findAll(specification, page).map(mailingListMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MailingListCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MailingList> specification = createSpecification(criteria);
        return mailingListRepository.count(specification);
    }

    /**
     * Function to convert {@link MailingListCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MailingList> createSpecification(MailingListCriteria criteria) {
        Specification<MailingList> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MailingList_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MailingList_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), MailingList_.description));
            }
            if (criteria.getTimeCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeCreated(), MailingList_.timeCreated));
            }
            if (criteria.getTimeModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeModified(), MailingList_.timeModified));
            }
        }
        return specification;
    }
}
