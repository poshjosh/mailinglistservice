package com.looseboxes.mailinglist.service.service;

import com.looseboxes.mailinglist.service.domain.*; // for static metamodels
import com.looseboxes.mailinglist.service.domain.MailingListUser;
import com.looseboxes.mailinglist.service.repository.MailingListUserRepository;
import com.looseboxes.mailinglist.service.service.criteria.MailingListUserCriteria;
import com.looseboxes.mailinglist.service.service.dto.MailingListUserDTO;
import com.looseboxes.mailinglist.service.service.mapper.MailingListUserMapper;
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
 * Service for executing complex queries for {@link MailingListUser} entities in the database.
 * The main input is a {@link MailingListUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MailingListUserDTO} or a {@link Page} of {@link MailingListUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MailingListUserQueryService extends QueryService<MailingListUser> {

    private final Logger log = LoggerFactory.getLogger(MailingListUserQueryService.class);

    private final MailingListUserRepository mailingListUserRepository;

    private final MailingListUserMapper mailingListUserMapper;

    public MailingListUserQueryService(MailingListUserRepository mailingListUserRepository, MailingListUserMapper mailingListUserMapper) {
        this.mailingListUserRepository = mailingListUserRepository;
        this.mailingListUserMapper = mailingListUserMapper;
    }

    /**
     * Return a {@link List} of {@link MailingListUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MailingListUserDTO> findByCriteria(MailingListUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MailingListUser> specification = createSpecification(criteria);
        return mailingListUserMapper.toDto(mailingListUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MailingListUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MailingListUserDTO> findByCriteria(MailingListUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MailingListUser> specification = createSpecification(criteria);
        return mailingListUserRepository.findAll(specification, page).map(mailingListUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MailingListUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MailingListUser> specification = createSpecification(criteria);
        return mailingListUserRepository.count(specification);
    }

    /**
     * Function to convert {@link MailingListUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MailingListUser> createSpecification(MailingListUserCriteria criteria) {
        Specification<MailingListUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MailingListUser_.id));
            }
            if (criteria.getUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsername(), MailingListUser_.username));
            }
            if (criteria.getEmailAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailAddress(), MailingListUser_.emailAddress));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), MailingListUser_.lastName));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), MailingListUser_.firstName));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), MailingListUser_.status));
            }
            if (criteria.getTimeCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeCreated(), MailingListUser_.timeCreated));
            }
            if (criteria.getTimeModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeModified(), MailingListUser_.timeModified));
            }
        }
        return specification;
    }
}
