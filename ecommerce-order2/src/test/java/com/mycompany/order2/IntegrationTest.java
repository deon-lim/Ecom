package com.mycompany.order2;

import com.mycompany.order2.config.AsyncSyncConfiguration;
import com.mycompany.order2.config.EmbeddedMongo;
import com.mycompany.order2.config.EmbeddedRedis;
import com.mycompany.order2.config.JacksonConfiguration;
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
@SpringBootTest(classes = { EcommerceOrder2App.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedMongo
public @interface IntegrationTest {
}
