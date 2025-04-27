package com.mycompany.ecommercecustomer.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Customer getCustomerSample1() {
        return new Customer()
            .id(1L)
            .firstName("firstName1")
            .lastName("lastName1")
            .phoneNumber("phoneNumber1")
            .addressLine1("addressLine11")
            .addressLine2("addressLine21")
            .postalCode("postalCode1")
            .city("city1")
            .state("state1")
            .country("country1")
            .preferences("preferences1")
            .loyaltyPoints(1)
            .userId("userId1");
    }

    public static Customer getCustomerSample2() {
        return new Customer()
            .id(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .phoneNumber("phoneNumber2")
            .addressLine1("addressLine12")
            .addressLine2("addressLine22")
            .postalCode("postalCode2")
            .city("city2")
            .state("state2")
            .country("country2")
            .preferences("preferences2")
            .loyaltyPoints(2)
            .userId("userId2");
    }

    public static Customer getCustomerRandomSampleGenerator() {
        return new Customer()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .addressLine1(UUID.randomUUID().toString())
            .addressLine2(UUID.randomUUID().toString())
            .postalCode(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .preferences(UUID.randomUUID().toString())
            .loyaltyPoints(intCount.incrementAndGet())
            .userId(UUID.randomUUID().toString());
    }
}
