package com.looseboxes.mailinglist.service.service;

import com.looseboxes.mailinglist.service.domain.MailingList;
import com.looseboxes.mailinglist.service.repository.MailingListRepository;
import com.looseboxes.mailinglist.service.service.dto.MailingListDTO;
import com.looseboxes.mailinglist.service.service.mapper.MailingListMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MailingList}.
 */
@Service
@Transactional
public class MailingListService {

    private final Logger log = LoggerFactory.getLogger(MailingListService.class);

    private final MailingListRepository mailingListRepository;

    private final MailingListMapper mailingListMapper;

    public MailingListService(MailingListRepository mailingListRepository, MailingListMapper mailingListMapper) {
        this.mailingListRepository = mailingListRepository;
        this.mailingListMapper = mailingListMapper;
    }

    /**
     * Save a mailingList.
     *
     * @param mailingListDTO the entity to save.
     * @return the persisted entity.
     */
    public MailingListDTO save(MailingListDTO mailingListDTO) {
        log.debug("Request to save MailingList : {}", mailingListDTO);
        MailingList mailingList = mailingListMapper.toEntity(mailingListDTO);
        mailingList = mailingListRepository.save(mailingList);
        return mailingListMapper.toDto(mailingList);
    }

    /**
     * Partially update a mailingList.
     *
     * @param mailingListDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MailingListDTO> partialUpdate(MailingListDTO mailingListDTO) {
        log.debug("Request to partially update MailingList : {}", mailingListDTO);

        return mailingListRepository
            .findById(mailingListDTO.getId())
            .map(
                existingMailingList -> {
                    mailingListMapper.partialUpdate(existingMailingList, mailingListDTO);
                    return existingMailingList;
                }
            )
            .map(mailingListRepository::save)
            .map(mailingListMapper::toDto);
    }

    /**
     * Get all the mailingLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MailingListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MailingLists");
        return mailingListRepository.findAll(pageable).map(mailingListMapper::toDto);
    }

    /**
     * Get one mailingList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MailingListDTO> findOne(Long id) {
        log.debug("Request to get MailingList : {}", id);
        return mailingListRepository.findById(id).map(mailingListMapper::toDto);
    }

    /**
     * Delete the mailingList by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MailingList : {}", id);
        mailingListRepository.deleteById(id);
    }
}
