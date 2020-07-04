package com.codingsample.vendomatic.model;

import com.codingsample.vendomatic.model.currency.Coin;
import com.codingsample.vendomatic.model.exception.InsufficientChangeException;
import com.codingsample.vendomatic.model.exception.ItemOutOfStockException;

import java.math.BigDecimal;

public interface VendingMachine {
    /**
     * Inserts a coin denomination value {{quantity}} number of times
     * @param quantity The number of {{coin}} inserted into the machine
     * @param coin The coin denomination
     */
    void insertCoin(int quantity, Coin coin);

    /**
     * Checks the balance in the vending machine at the time of calling
     * @return current balance (in BigDecimal)
     */
    BigDecimal getCurrentBalance();

    /**
     * Will attempt to purchase the item selected
     * @param index the selection index
     * @return the change from the transaction
     * @throws com.codingsample.vendomatic.model.exception.InsufficientChangeException when current balance is less than price of selected item
     * @throws com.codingsample.vendomatic.model.exception.ItemOutOfStockException when selected item is unavailable
     */
    BigDecimal selectItem(int index) throws ItemOutOfStockException, InsufficientChangeException;

    // Other implementations would include -- resetting the machine to get back your money,
    // selecting an item when no money is in the machine to get information on the item like its price
}
