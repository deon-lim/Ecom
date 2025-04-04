package com.mycompany.ecommerceorder.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrderAllPropertiesEquals(Order expected, Order actual) {
        assertOrderAutoGeneratedPropertiesEquals(expected, actual);
        assertOrderAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrderAllUpdatablePropertiesEquals(Order expected, Order actual) {
        assertOrderUpdatableFieldsEquals(expected, actual);
        assertOrderUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrderAutoGeneratedPropertiesEquals(Order expected, Order actual) {
        assertThat(actual)
            .as("Verify Order auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrderUpdatableFieldsEquals(Order expected, Order actual) {
        assertThat(actual)
            .as("Verify Order relevant properties")
            .satisfies(a -> assertThat(a.getCustomerId()).as("check customerId").isEqualTo(expected.getCustomerId()))
            .satisfies(a -> assertThat(a.getOrderStatus()).as("check orderStatus").isEqualTo(expected.getOrderStatus()))
            .satisfies(a -> assertThat(a.getOrderDate()).as("check orderDate").isEqualTo(expected.getOrderDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrderUpdatableRelationshipsEquals(Order expected, Order actual) {
        // empty method
    }
}
