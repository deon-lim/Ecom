package com.nusiss.order;

import com.nusiss.order.config.AsyncSyncConfiguration;
import com.nusiss.order.config.EmbeddedMongo;
import com.nusiss.order.config.EmbeddedRedis;
import com.nusiss.order.config.JacksonConfiguration;
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
