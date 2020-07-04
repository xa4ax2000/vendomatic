package com.codingsample.vendomatic.model;

import java.math.BigDecimal;

public enum UnitedStatesCoin implements Coin {
    QUARTER("0.25"),
    DIME("0.10"),
    NICKEL("0.05"),
    PENNY("0.01");

    private final BigDecimal VALUE;

    UnitedStatesCoin(String value){
        this.VALUE = new BigDecimal(value);
    }

    @Override
    public CurrencyCode getCurrencyCode() {
        return CurrencyCode.USD;
    }

    @Override
    public BigDecimal getValue() {
        return VALUE;
    }
}
