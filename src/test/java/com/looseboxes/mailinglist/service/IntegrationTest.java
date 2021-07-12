package com.looseboxes.mailinglist.service;

import com.looseboxes.mailinglist.service.MailinglistserviceApp;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = MailinglistserviceApp.class)
public @interface IntegrationTest {
}
