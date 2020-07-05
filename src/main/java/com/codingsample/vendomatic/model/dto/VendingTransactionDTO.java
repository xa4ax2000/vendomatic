package com.codingsample.vendomatic.model.dto;

import com.codingsample.vendomatic.model.currency.Coin;

import java.util.Map;

/**
 * DTO object containing the details of a given selection after a completed transaction (purchase event)
 */
public class VendingTransactionDTO {
    private int indexToPurchase;

    public VendingTransactionDTO(int indexToPurchase){
        this.indexToPurchase = indexToPurchase;
    }

    private int numItemsVended;

    /**
     * In the event item is out of stock, we should return all of the coins that are currently in the machine (and zero out)
     * in the event coins are insufficient, we should return the coins (0|1) and zero it out
     */
    private Map<Coin, Integer> coinsToBeReturned;
    private int remainingItemsInSelection;
    // eventually may want to add items that were vended in a list to display back to user?


    public int getNumItemsVended() {
        return numItemsVended;
    }

    public void setNumItemsVended(int numItemsVended) {
        this.numItemsVended = numItemsVended;
    }

    public Map<Coin, Integer> getCoinsToBeReturned() {
        return coinsToBeReturned;
    }

    public void setCoinsToBeReturned(Map<Coin, Integer> coinsToBeReturned) {
        this.coinsToBeReturned = coinsToBeReturned;
    }

    public int getRemainingItemsInSelection() {
        return remainingItemsInSelection;
    }

    public void setRemainingItemsInSelection(int remainingItemsInSelection) {
        this.remainingItemsInSelection = remainingItemsInSelection;
    }

    public int getIndexToPurchase() {
        return indexToPurchase;
    }
}
