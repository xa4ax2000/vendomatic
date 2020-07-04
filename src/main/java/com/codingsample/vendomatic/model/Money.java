package com.codingsample.vendomatic.model;

import java.math.BigDecimal;

/**
 * Interface dealing with currency denominations in the form of coins, bills (not implemented), credit (not implemented), etc.
 */
public interface Money {
    CurrencyCode getCurrencyCode();
    BigDecimal getValue();
}
