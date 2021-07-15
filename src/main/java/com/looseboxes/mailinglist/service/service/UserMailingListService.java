package com.looseboxes.mailinglist.service.service;

import com.looseboxes.mailinglist.service.domain.UserMailingList;
import com.looseboxes.mailinglist.service.repository.UserMailingListRepository;
import com.looseboxes.mailinglist.service.service.dto.UserMailingListDTO;
import com.looseboxes.mailinglist.service.service.mapper.UserMailingListMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserMailingList}.
 */
@Service
@Transactional
public class UserMailingListService {

    private final Logger log = LoggerFactory.getLogger(UserMailingListService.class);

    private final UserMailingListRepository userMailingListRepository;

    private final UserMailingListMapper userMailingListMapper;

    public UserMailingListService(UserMailingListRepository userMailingListRepository, UserMailingListMapper userMailingListMapper) {
        this.userMailingListRepository = userMailingListRepository;
        this.userMailingListMapper = userMailingListMapper;
    }

    /**
     * Save a userMailingList.
     *
     * @param userMailingListDTO the entity to save.
     * @return the persisted entity.
     */
    public UserMailingListDTO save(UserMailingListDTO userMailingListDTO) {
        log.debug("Request to save UserMailingList : {}", userMailingListDTO);
        UserMailingList userMailingList = userMailingListMapper.toEntity(userMailingListDTO);
        userMailingList = userMailingListRepository.save(userMailingList);
        return userMailingListMapper.toDto(userMailingList);
    }

    /**
     * Partially update a userMailingList.
     *
     * @param userMailingListDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserMailingListDTO> partialUpdate(UserMailingListDTO userMailingListDTO) {
        log.debug("Request to partially update UserMailingList : {}", userMailingListDTO);

        return userMailingListRepository
            .findById(userMailingListDTO.getId())
            .map(
                existingUserMailingList -> {
                    userMailingListMapper.partialUpdate(existingUserMailingList, userMailingListDTO);
                    return existingUserMailingList;
                }
            )
            .map(userMailingListRepository::save)
            .map(userMailingListMapper::toDto);
    }

    /**
     * Get all the userMailingLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserMailingListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserMailingLists");
        return userMailingListRepository.findAll(pageable).map(userMailingListMapper::toDto);
    }

    /**
     * Get one userMailingList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserMailingListDTO> findOne(Long id) {
        log.debug("Request to get UserMailingList : {}", id);
        return userMailingListRepository.findById(id).map(userMailingListMapper::toDto);
    }

    /**
     * Delete the userMailingList by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserMailingList : {}", id);
        userMailingListRepository.deleteById(id);
    }
}
