package com.looseboxes.mailinglist.service.service;

import com.looseboxes.mailinglist.service.domain.MailingListUser;
import com.looseboxes.mailinglist.service.repository.MailingListUserRepository;
import com.looseboxes.mailinglist.service.service.dto.MailingListUserDTO;
import com.looseboxes.mailinglist.service.service.mapper.MailingListUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MailingListUser}.
 */
@Service
@Transactional
public class MailingListUserService {

    private final Logger log = LoggerFactory.getLogger(MailingListUserService.class);

    private final MailingListUserRepository mailingListUserRepository;

    private final MailingListUserMapper mailingListUserMapper;

    public MailingListUserService(MailingListUserRepository mailingListUserRepository, MailingListUserMapper mailingListUserMapper) {
        this.mailingListUserRepository = mailingListUserRepository;
        this.mailingListUserMapper = mailingListUserMapper;
    }

    /**
     * Save a mailingListUser.
     *
     * @param mailingListUserDTO the entity to save.
     * @return the persisted entity.
     */
    public MailingListUserDTO save(MailingListUserDTO mailingListUserDTO) {
        log.debug("Request to save MailingListUser : {}", mailingListUserDTO);
        MailingListUser mailingListUser = mailingListUserMapper.toEntity(mailingListUserDTO);
        mailingListUser = mailingListUserRepository.save(mailingListUser);
        return mailingListUserMapper.toDto(mailingListUser);
    }

    /**
     * Partially update a mailingListUser.
     *
     * @param mailingListUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MailingListUserDTO> partialUpdate(MailingListUserDTO mailingListUserDTO) {
        log.debug("Request to partially update MailingListUser : {}", mailingListUserDTO);

        return mailingListUserRepository
            .findById(mailingListUserDTO.getId())
            .map(
                existingMailingListUser -> {
                    mailingListUserMapper.partialUpdate(existingMailingListUser, mailingListUserDTO);
                    return existingMailingListUser;
                }
            )
            .map(mailingListUserRepository::save)
            .map(mailingListUserMapper::toDto);
    }

    /**
     * Get all the mailingListUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MailingListUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MailingListUsers");
        return mailingListUserRepository.findAll(pageable).map(mailingListUserMapper::toDto);
    }

    /**
     * Get one mailingListUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MailingListUserDTO> findOne(Long id) {
        log.debug("Request to get MailingListUser : {}", id);
        return mailingListUserRepository.findById(id).map(mailingListUserMapper::toDto);
    }

    /**
     * Delete the mailingListUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MailingListUser : {}", id);
        mailingListUserRepository.deleteById(id);
    }
}
