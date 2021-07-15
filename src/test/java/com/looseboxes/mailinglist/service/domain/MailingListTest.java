package com.looseboxes.mailinglist.service.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.looseboxes.mailinglist.service.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MailingListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MailingList.class);
        MailingList mailingList1 = new MailingList();
        mailingList1.setId(1L);
        MailingList mailingList2 = new MailingList();
        mailingList2.setId(mailingList1.getId());
        assertThat(mailingList1).isEqualTo(mailingList2);
        mailingList2.setId(2L);
        assertThat(mailingList1).isNotEqualTo(mailingList2);
        mailingList1.setId(null);
        assertThat(mailingList1).isNotEqualTo(mailingList2);
    }
}
