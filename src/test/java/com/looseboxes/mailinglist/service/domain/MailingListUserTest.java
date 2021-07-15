package com.looseboxes.mailinglist.service.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.looseboxes.mailinglist.service.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MailingListUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MailingListUser.class);
        MailingListUser mailingListUser1 = new MailingListUser();
        mailingListUser1.setId(1L);
        MailingListUser mailingListUser2 = new MailingListUser();
        mailingListUser2.setId(mailingListUser1.getId());
        assertThat(mailingListUser1).isEqualTo(mailingListUser2);
        mailingListUser2.setId(2L);
        assertThat(mailingListUser1).isNotEqualTo(mailingListUser2);
        mailingListUser1.setId(null);
        assertThat(mailingListUser1).isNotEqualTo(mailingListUser2);
    }
}
