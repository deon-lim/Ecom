package com.mycompany.order2.domain;

import java.math.BigDecimal;

/**
 * A Product.
 */
public class Order2Product {

    private String productId;
    private BigDecimal productPrice;
    private int quantity;

    public Order2Product(String productId, BigDecimal productPrice, int quantity) {
        this.productId = productId;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
            "productId='" + productId + '\'' +
            ", productPrice=" + productPrice +
            ", quantity=" + quantity +
            '}';
    }
}
