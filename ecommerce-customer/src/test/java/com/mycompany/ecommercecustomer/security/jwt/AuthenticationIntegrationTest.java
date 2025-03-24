package com.mycompany.ecommercecustomer.security.jwt;

import com.mycompany.ecommercecustomer.config.SecurityConfiguration;
import com.mycompany.ecommercecustomer.config.SecurityJwtConfiguration;
import com.mycompany.ecommercecustomer.config.WebConfigurer;
import com.mycompany.ecommercecustomer.management.SecurityMetersService;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import tech.jhipster.config.JHipsterProperties;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        JHipsterProperties.class,
        WebConfigurer.class,
        SecurityConfiguration.class,
        SecurityJwtConfiguration.class,
        SecurityMetersService.class,
        JwtAuthenticationTestUtils.class,
    }
)
public @interface AuthenticationIntegrationTest {
}
