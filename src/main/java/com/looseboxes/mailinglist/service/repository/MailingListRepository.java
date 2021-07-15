package com.looseboxes.mailinglist.service.repository;

import com.looseboxes.mailinglist.service.domain.MailingList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MailingList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MailingListRepository extends JpaRepository<MailingList, Long>, JpaSpecificationExecutor<MailingList> {}
