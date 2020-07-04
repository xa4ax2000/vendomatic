package com.codingsample.vendomatic.model.modelone;

import com.codingsample.vendomatic.model.Item;

import java.math.BigDecimal;

public enum  ModelOneItem implements Item {
    SOME_ITEM("SOME_ITEM", new BigDecimal("0.50"));

    private final String NAME;
    private final BigDecimal PRICE;

    ModelOneItem(String displayName, BigDecimal price){
        this.NAME = displayName;
        this.PRICE = price;
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }

    @Override
    public BigDecimal getPrice() {
        return PRICE;
    }
}
