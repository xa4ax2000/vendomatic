package com.codingsample.vendomatic.model;

import java.math.BigDecimal;

/**
 * The object being sold in the vending machine
 */
public interface Item {
    String getDisplayName();
    BigDecimal getPrice();
}
