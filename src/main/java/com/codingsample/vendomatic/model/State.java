package com.codingsample.vendomatic.model;

import com.codingsample.vendomatic.model.currency.Coin;
import com.codingsample.vendomatic.model.exception.*;

/**
 * Keeps the vending machine's current state
 *
 * There are several states a vending machine will be in throughout the vending process:
 * 1. Initial State -- Vending machine has no money (depending on operation it could trigger "more info")
 * 2. Has Money State -- Vending Machine will have money (depending on the operation it could trigger
 *          an "insufficient funds" or move to dispensing)
 * 3. Dispensing State -- Vending machine will dispense the item or trigger "item out of stock"
 */
public interface State {
    /**
     * Actions:
     */
    void insertCoin(int quantity, Coin coin) throws MachineDoesNotTakeMultipleOrNonExistentCoinsException, InvalidCurrencyException;

    Item selectItem(int index) throws SelectionUnknownException, ItemOutOfStockException, InsufficientChangeException;


}
