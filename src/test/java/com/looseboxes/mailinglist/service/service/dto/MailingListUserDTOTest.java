package com.looseboxes.mailinglist.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.looseboxes.mailinglist.service.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MailingListUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MailingListUserDTO.class);
        MailingListUserDTO mailingListUserDTO1 = new MailingListUserDTO();
        mailingListUserDTO1.setId(1L);
        MailingListUserDTO mailingListUserDTO2 = new MailingListUserDTO();
        assertThat(mailingListUserDTO1).isNotEqualTo(mailingListUserDTO2);
        mailingListUserDTO2.setId(mailingListUserDTO1.getId());
        assertThat(mailingListUserDTO1).isEqualTo(mailingListUserDTO2);
        mailingListUserDTO2.setId(2L);
        assertThat(mailingListUserDTO1).isNotEqualTo(mailingListUserDTO2);
        mailingListUserDTO1.setId(null);
        assertThat(mailingListUserDTO1).isNotEqualTo(mailingListUserDTO2);
    }
}
