package com.looseboxes.mailinglist.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.looseboxes.mailinglist.service.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserMailingListDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMailingListDTO.class);
        UserMailingListDTO userMailingListDTO1 = new UserMailingListDTO();
        userMailingListDTO1.setId(1L);
        UserMailingListDTO userMailingListDTO2 = new UserMailingListDTO();
        assertThat(userMailingListDTO1).isNotEqualTo(userMailingListDTO2);
        userMailingListDTO2.setId(userMailingListDTO1.getId());
        assertThat(userMailingListDTO1).isEqualTo(userMailingListDTO2);
        userMailingListDTO2.setId(2L);
        assertThat(userMailingListDTO1).isNotEqualTo(userMailingListDTO2);
        userMailingListDTO1.setId(null);
        assertThat(userMailingListDTO1).isNotEqualTo(userMailingListDTO2);
    }
}
