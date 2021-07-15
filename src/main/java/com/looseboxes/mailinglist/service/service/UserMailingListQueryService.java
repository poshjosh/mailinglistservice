package com.looseboxes.mailinglist.service.service;

import com.looseboxes.mailinglist.service.domain.*; // for static metamodels
import com.looseboxes.mailinglist.service.domain.UserMailingList;
import com.looseboxes.mailinglist.service.repository.UserMailingListRepository;
import com.looseboxes.mailinglist.service.service.criteria.UserMailingListCriteria;
import com.looseboxes.mailinglist.service.service.dto.UserMailingListDTO;
import com.looseboxes.mailinglist.service.service.mapper.UserMailingListMapper;
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
 * Service for executing complex queries for {@link UserMailingList} entities in the database.
 * The main input is a {@link UserMailingListCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserMailingListDTO} or a {@link Page} of {@link UserMailingListDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserMailingListQueryService extends QueryService<UserMailingList> {

    private final Logger log = LoggerFactory.getLogger(UserMailingListQueryService.class);

    private final UserMailingListRepository userMailingListRepository;

    private final UserMailingListMapper userMailingListMapper;

    public UserMailingListQueryService(UserMailingListRepository userMailingListRepository, UserMailingListMapper userMailingListMapper) {
        this.userMailingListRepository = userMailingListRepository;
        this.userMailingListMapper = userMailingListMapper;
    }

    /**
     * Return a {@link List} of {@link UserMailingListDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserMailingListDTO> findByCriteria(UserMailingListCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserMailingList> specification = createSpecification(criteria);
        return userMailingListMapper.toDto(userMailingListRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserMailingListDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserMailingListDTO> findByCriteria(UserMailingListCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserMailingList> specification = createSpecification(criteria);
        return userMailingListRepository.findAll(specification, page).map(userMailingListMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserMailingListCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserMailingList> specification = createSpecification(criteria);
        return userMailingListRepository.count(specification);
    }

    /**
     * Function to convert {@link UserMailingListCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserMailingList> createSpecification(UserMailingListCriteria criteria) {
        Specification<UserMailingList> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserMailingList_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), UserMailingList_.status));
            }
            if (criteria.getTimeCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeCreated(), UserMailingList_.timeCreated));
            }
            if (criteria.getTimeModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeModified(), UserMailingList_.timeModified));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserId(),
                            root -> root.join(UserMailingList_.user, JoinType.LEFT).get(MailingListUser_.id)
                        )
                    );
            }
            if (criteria.getMailingListId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMailingListId(),
                            root -> root.join(UserMailingList_.mailingList, JoinType.LEFT).get(MailingList_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
