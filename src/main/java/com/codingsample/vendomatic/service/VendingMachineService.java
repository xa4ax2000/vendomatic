package com.codingsample.vendomatic.service;

import com.codingsample.vendomatic.model.currency.Coin;
import com.codingsample.vendomatic.model.dto.VendingTransactionDTO;
import com.codingsample.vendomatic.model.exception.InvalidCurrencyException;
import com.codingsample.vendomatic.model.exception.MachineDoesNotTakeMultipleCoinsException;
import com.codingsample.vendomatic.model.exception.SelectionUnknownException;

import java.util.Map;

public interface VendingMachineService {

    /**
     * Will insert {{quantity}} number of {{coin}} to the vending machine tied to the service
     * @param coinToQuantityMap the coins inserted into the machine
     * @return the current number of coins in the machine (?)
     */
    int insertCoin(Map<Coin, Integer> coinToQuantityMap) throws MachineDoesNotTakeMultipleCoinsException, InvalidCurrencyException;

    /**
     * Will reset the coins in the machine
     * @return the number of coins to return (?)
     */
    int reset();

    /**
     * Will return the inventory in each selection index
     * @return Array of amount of items in each selection index
     */
    Integer[] getTotalInventory();

    /**
     * Will return the inventory in a given selection index
     * @param index the selection index
     * @return the inventory in the given selection index
     * @throws SelectionUnknownException when the index selection is unknown
     */
    int getInventoryInGivenSelection(int index) throws SelectionUnknownException;

    /**
     * Will attempt to purchase selection
     * @param index the selection index
     * @return a transaction dto that contains information regarding the transaction
     * @throws SelectionUnknownException when the index selection is unknown
     */
    VendingTransactionDTO purchase(int index) throws SelectionUnknownException;
}
