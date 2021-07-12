package com.looseboxes.mailinglist.service.cucumber;

import com.looseboxes.mailinglist.service.MailinglistserviceApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = MailinglistserviceApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
