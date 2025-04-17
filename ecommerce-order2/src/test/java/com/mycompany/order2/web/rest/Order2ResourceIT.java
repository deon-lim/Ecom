package com.mycompany.order2.web.rest;

import static com.mycompany.order2.domain.Order2Asserts.*;
import static com.mycompany.order2.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mycompany.order2.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.order2.IntegrationTest;
import com.mycompany.order2.domain.Order2;
import com.mycompany.order2.repository.Order2Repository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link Order2Resource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class Order2ResourceIT {

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_STATUS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/order-2-s";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private Order2Repository order2Repository;

    @Autowired
    private MockMvc restOrder2MockMvc;

    private Order2 order2;

    private Order2 insertedOrder2;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order2 createEntity() {
        return new Order2()
            .customerId(DEFAULT_CUSTOMER_ID)
            .orderStatus(DEFAULT_ORDER_STATUS)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .createdOn(DEFAULT_CREATED_ON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order2 createUpdatedEntity() {
        return new Order2()
            .customerId(UPDATED_CUSTOMER_ID)
            .orderStatus(UPDATED_ORDER_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .createdOn(UPDATED_CREATED_ON);
    }

    @BeforeEach
    public void initTest() {
        order2 = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOrder2 != null) {
            order2Repository.delete(insertedOrder2);
            insertedOrder2 = null;
        }
    }

    @Test
    void createOrder2() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Order2
        var returnedOrder2 = om.readValue(
            restOrder2MockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(order2)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Order2.class
        );

        // Validate the Order2 in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrder2UpdatableFieldsEquals(returnedOrder2, getPersistedOrder2(returnedOrder2));

        insertedOrder2 = returnedOrder2;
    }

    @Test
    void createOrder2WithExistingId() throws Exception {
        // Create the Order2 with an existing ID
        order2.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrder2MockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(order2)))
            .andExpect(status().isBadRequest());

        // Validate the Order2 in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllOrder2s() throws Exception {
        // Initialize the database
        insertedOrder2 = order2Repository.save(order2);

        // Get all the order2List
        restOrder2MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order2.getId())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS)))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())));
    }

    @Test
    void getOrder2() throws Exception {
        // Initialize the database
        insertedOrder2 = order2Repository.save(order2);

        // Get the order2
        restOrder2MockMvc
            .perform(get(ENTITY_API_URL_ID, order2.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(order2.getId()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()));
    }

    @Test
    void getNonExistingOrder2() throws Exception {
        // Get the order2
        restOrder2MockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingOrder2() throws Exception {
        // Initialize the database
        insertedOrder2 = order2Repository.save(order2);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the order2
        Order2 updatedOrder2 = order2Repository.findById(order2.getId()).orElseThrow();
        updatedOrder2
            .customerId(UPDATED_CUSTOMER_ID)
            .orderStatus(UPDATED_ORDER_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .createdOn(UPDATED_CREATED_ON);

        restOrder2MockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrder2.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrder2))
            )
            .andExpect(status().isOk());

        // Validate the Order2 in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrder2ToMatchAllProperties(updatedOrder2);
    }

    @Test
    void putNonExistingOrder2() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order2.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrder2MockMvc
            .perform(put(ENTITY_API_URL_ID, order2.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(order2)))
            .andExpect(status().isBadRequest());

        // Validate the Order2 in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOrder2() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order2.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrder2MockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(order2))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order2 in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOrder2() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order2.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrder2MockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(order2)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order2 in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOrder2WithPatch() throws Exception {
        // Initialize the database
        insertedOrder2 = order2Repository.save(order2);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the order2 using partial update
        Order2 partialUpdatedOrder2 = new Order2();
        partialUpdatedOrder2.setId(order2.getId());

        partialUpdatedOrder2.orderStatus(UPDATED_ORDER_STATUS).totalAmount(UPDATED_TOTAL_AMOUNT).createdOn(UPDATED_CREATED_ON);

        restOrder2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder2.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrder2))
            )
            .andExpect(status().isOk());

        // Validate the Order2 in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrder2UpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOrder2, order2), getPersistedOrder2(order2));
    }

    @Test
    void fullUpdateOrder2WithPatch() throws Exception {
        // Initialize the database
        insertedOrder2 = order2Repository.save(order2);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the order2 using partial update
        Order2 partialUpdatedOrder2 = new Order2();
        partialUpdatedOrder2.setId(order2.getId());

        partialUpdatedOrder2
            .customerId(UPDATED_CUSTOMER_ID)
            .orderStatus(UPDATED_ORDER_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .createdOn(UPDATED_CREATED_ON);

        restOrder2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder2.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrder2))
            )
            .andExpect(status().isOk());

        // Validate the Order2 in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrder2UpdatableFieldsEquals(partialUpdatedOrder2, getPersistedOrder2(partialUpdatedOrder2));
    }

    @Test
    void patchNonExistingOrder2() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order2.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrder2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, order2.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(order2))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order2 in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOrder2() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order2.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrder2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(order2))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order2 in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOrder2() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        order2.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrder2MockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(order2)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order2 in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOrder2() throws Exception {
        // Initialize the database
        insertedOrder2 = order2Repository.save(order2);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the order2
        restOrder2MockMvc
            .perform(delete(ENTITY_API_URL_ID, order2.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return order2Repository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Order2 getPersistedOrder2(Order2 order2) {
        return order2Repository.findById(order2.getId()).orElseThrow();
    }

    protected void assertPersistedOrder2ToMatchAllProperties(Order2 expectedOrder2) {
        assertOrder2AllPropertiesEquals(expectedOrder2, getPersistedOrder2(expectedOrder2));
    }

    protected void assertPersistedOrder2ToMatchUpdatableProperties(Order2 expectedOrder2) {
        assertOrder2AllUpdatablePropertiesEquals(expectedOrder2, getPersistedOrder2(expectedOrder2));
    }
}
