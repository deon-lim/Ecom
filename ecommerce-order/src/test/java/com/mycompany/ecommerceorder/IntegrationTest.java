package com.mycompany.ecommerceorder;

import com.mycompany.ecommerceorder.config.AsyncSyncConfiguration;
import com.mycompany.ecommerceorder.config.EmbeddedRedis;
import com.mycompany.ecommerceorder.config.EmbeddedSQL;
import com.mycompany.ecommerceorder.config.JacksonConfiguration;
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
@SpringBootTest(classes = { EcommerceOrderApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
public @interface IntegrationTest {
}
