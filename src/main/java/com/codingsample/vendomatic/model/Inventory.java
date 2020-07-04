package com.codingsample.vendomatic.model;

import com.codingsample.vendomatic.model.exception.ItemOutOfStockException;
import com.codingsample.vendomatic.model.exception.MaximumItemsInStockException;
import com.codingsample.vendomatic.model.exception.SelectionUnknownException;

public interface Inventory {
    /**
     *
     * @param index the selection index
     */
    void add(int index) throws SelectionUnknownException, MaximumItemsInStockException;

    /**
     * Try to remove an item from the selection index. If none exists, throw an error
     * @param index the selection index
     * @return the item to be removed
     */
    Item remove(int index) throws SelectionUnknownException, ItemOutOfStockException;

    /**
     * Peek at the item from the selection index.
     * @param index the selection index
     * @return the item that would be removed -- Note: will return null if the selection is empty!
     */
    Item peek(int index) throws SelectionUnknownException;

    /**
     * Get count of all items in each selection
     * @return array of inventory for each selection in inventory
     */
    Integer[] getInventoryQuantity();

    /**
     * Get count of items in given selection
     * @param index the selection index
     * @return inventory for given selection in inventory
     */
    int getInventoryStockQuantity(int index) throws SelectionUnknownException;
}
