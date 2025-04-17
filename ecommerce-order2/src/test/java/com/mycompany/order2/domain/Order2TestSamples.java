package com.mycompany.order2.domain;

import java.util.UUID;

public class Order2TestSamples {

    public static Order2 getOrder2Sample1() {
        return new Order2().id("id1").customerId("customerId1").orderStatus("orderStatus1");
    }

    public static Order2 getOrder2Sample2() {
        return new Order2().id("id2").customerId("customerId2").orderStatus("orderStatus2");
    }

    public static Order2 getOrder2RandomSampleGenerator() {
        return new Order2()
            .id(UUID.randomUUID().toString())
            .customerId(UUID.randomUUID().toString())
            .orderStatus(UUID.randomUUID().toString());
    }
}
