package com.nusiss.order.domain;

import java.util.UUID;

public class OrderTestSamples {

    public static Order getOrderSample1() {
        return new Order().id("id1").customerId("customerId1");
    }

    public static Order getOrderSample2() {
        return new Order().id("id2").customerId("customerId2");
    }

    public static Order getOrderRandomSampleGenerator() {
        return new Order().id(UUID.randomUUID().toString()).customerId(UUID.randomUUID().toString());
    }
}
