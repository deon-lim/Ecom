package com.mycompany.ecommercecustomer;

import com.mycompany.ecommercecustomer.config.AsyncSyncConfiguration;
import com.mycompany.ecommercecustomer.config.EmbeddedRedis;
import com.mycompany.ecommercecustomer.config.EmbeddedSQL;
import com.mycompany.ecommercecustomer.config.JacksonConfiguration;
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
@SpringBootTest(classes = { EcommerceCustomerApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
public @interface IntegrationTest {
}
