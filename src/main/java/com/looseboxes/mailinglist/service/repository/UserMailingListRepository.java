package com.looseboxes.mailinglist.service.repository;

import com.looseboxes.mailinglist.service.domain.UserMailingList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserMailingList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserMailingListRepository extends JpaRepository<UserMailingList, Long>, JpaSpecificationExecutor<UserMailingList> {}
