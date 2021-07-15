package com.looseboxes.mailinglist.service.repository;

import com.looseboxes.mailinglist.service.domain.MailingListUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MailingListUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MailingListUserRepository extends JpaRepository<MailingListUser, Long>, JpaSpecificationExecutor<MailingListUser> {}
