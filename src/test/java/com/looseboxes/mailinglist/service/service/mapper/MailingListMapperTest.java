package com.looseboxes.mailinglist.service.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MailingListMapperTest {

    private MailingListMapper mailingListMapper;

    @BeforeEach
    public void setUp() {
        mailingListMapper = new MailingListMapperImpl();
    }
}
