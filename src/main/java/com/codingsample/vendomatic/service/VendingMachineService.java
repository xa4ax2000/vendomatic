package com.codingsample.vendomatic.service;

import com.codingsample.vendomatic.model.currency.Coin;
import com.codingsample.vendomatic.model.dto.VendingTransactionDTO;
import com.codingsample.vendomatic.model.exception.*;

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
     * @param vendingTransactionDTO the DTO that contains the selection index to attempt purchase on
     * @throws SelectionUnknownException when the index selection is unknown
     * @throws InsufficientChangeException when balance < cost of item
     * @throws ItemOutOfStockException when items in given selection index is out of stock
     */
    void purchase(VendingTransactionDTO vendingTransactionDTO) throws SelectionUnknownException, InsufficientChangeException, ItemOutOfStockException;
}
