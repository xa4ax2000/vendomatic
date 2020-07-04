package com.codingsample.vendomatic.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InsertCoinRequest {
    @JsonProperty("coin")
    private int coinQuantity;

    public int getCoinQuantity() {
        return coinQuantity;
    }

    public void setCoinQuantity(int coinQuantity) {
        this.coinQuantity = coinQuantity;
    }
}
