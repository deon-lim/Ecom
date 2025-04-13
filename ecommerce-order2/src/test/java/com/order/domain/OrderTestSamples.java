package com.order.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Order getOrderSample1() {
        return new Order().id("id1").product_name("product_name1").product_quantity(1);
    }

    public static Order getOrderSample2() {
        return new Order().id("id2").product_name("product_name2").product_quantity(2);
    }

    public static Order getOrderRandomSampleGenerator() {
        return new Order()
            .id(UUID.randomUUID().toString())
            .product_name(UUID.randomUUID().toString())
            .product_quantity(intCount.incrementAndGet());
    }
}
