package com.looseboxes.mailinglist.service.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.looseboxes.mailinglist.service.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserMailingListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMailingList.class);
        UserMailingList userMailingList1 = new UserMailingList();
        userMailingList1.setId(1L);
        UserMailingList userMailingList2 = new UserMailingList();
        userMailingList2.setId(userMailingList1.getId());
        assertThat(userMailingList1).isEqualTo(userMailingList2);
        userMailingList2.setId(2L);
        assertThat(userMailingList1).isNotEqualTo(userMailingList2);
        userMailingList1.setId(null);
        assertThat(userMailingList1).isNotEqualTo(userMailingList2);
    }
}
