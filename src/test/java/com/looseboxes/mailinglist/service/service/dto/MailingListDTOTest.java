package com.looseboxes.mailinglist.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.looseboxes.mailinglist.service.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MailingListDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MailingListDTO.class);
        MailingListDTO mailingListDTO1 = new MailingListDTO();
        mailingListDTO1.setId(1L);
        MailingListDTO mailingListDTO2 = new MailingListDTO();
        assertThat(mailingListDTO1).isNotEqualTo(mailingListDTO2);
        mailingListDTO2.setId(mailingListDTO1.getId());
        assertThat(mailingListDTO1).isEqualTo(mailingListDTO2);
        mailingListDTO2.setId(2L);
        assertThat(mailingListDTO1).isNotEqualTo(mailingListDTO2);
        mailingListDTO1.setId(null);
        assertThat(mailingListDTO1).isNotEqualTo(mailingListDTO2);
    }
}
