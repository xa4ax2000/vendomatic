package com.codingsample.vendomatic.model.exception;

import java.math.BigDecimal;

/**
 * Exception thrown when client tries to purchase an item in the vending machine, but
 * the current balance is less than the value of the item.
 */
public class InsufficientChangeException extends Exception{
    private final String costOfItem;

    public InsufficientChangeException(BigDecimal costOfItem){
        super();
        this.costOfItem = costOfItem != null ? costOfItem.toPlainString() : "selection is empty";
    }

    public String getCostOfItem() {
        return costOfItem;
    }
}
