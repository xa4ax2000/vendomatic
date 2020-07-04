package com.codingsample.vendomatic.model;

import com.codingsample.vendomatic.model.currency.Coin;
import com.codingsample.vendomatic.model.exception.*;

import java.math.BigDecimal;

public interface VendingMachine {
    /**
     * Inserts a coin denomination value {{quantity}} number of times
     * @param quantity The number of {{coin}} inserted into the machine
     * @param coin The coin denomination
     * @throws MachineDoesNotTakeMultipleCoinsException when more than one coin is inserted at once
     * @throws InvalidCurrencyException when currency is not supported
     */
    void insertCoin(int quantity, Coin coin) throws MachineDoesNotTakeMultipleCoinsException, InvalidCurrencyException;

    /**
     * Checks the balance in the vending machine at the time of calling
     * @return current balance (in BigDecimal)
     */
    BigDecimal getCurrentBalance();

    /**
     * Will attempt to purchase the item selected
     * @param index the selection index
     * @return the item from the transaction
     * @throws InsufficientChangeException when current balance is less than price of selected item
     * @throws ItemOutOfStockException when selected item is unavailable
     * @throws SelectionUnknownException when selection index is not valid
     */
    Item selectItem(int index) throws ItemOutOfStockException, InsufficientChangeException, SelectionUnknownException;

    /**
     * Will essentially zero out the current balance
     */
    void reset();

    // Other implementations would include
    // selecting an item when no money is in the machine to get information on the item like its price
}
