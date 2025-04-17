package com.mycompany.order2.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Order2.
 */
@Document(collection = "order2")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order2 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("customer_id")
    private String customerId;

    @Field("order_status")
    private String orderStatus;

    @Field("total_amount")
    private BigDecimal totalAmount;

    @Field("created_on")
    private Instant createdOn;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Order2 id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public Order2 customerId(String customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public Order2 orderStatus(String orderStatus) {
        this.setOrderStatus(orderStatus);
        return this;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Order2 totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public Order2 createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order2)) {
            return false;
        }
        return getId() != null && getId().equals(((Order2) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order2{" +
            "id=" + getId() +
            ", customerId='" + getCustomerId() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", createdOn='" + getCreatedOn() + "'" +
            "}";
    }
}
