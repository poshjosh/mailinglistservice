package com.looseboxes.mailinglist.service.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MailingListUserMapperTest {

    private MailingListUserMapper mailingListUserMapper;

    @BeforeEach
    public void setUp() {
        mailingListUserMapper = new MailingListUserMapperImpl();
    }
}
