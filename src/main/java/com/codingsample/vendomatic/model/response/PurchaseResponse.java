package com.codingsample.vendomatic.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PurchaseResponse {
    @JsonProperty
    private int quantity;

    public PurchaseResponse(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
