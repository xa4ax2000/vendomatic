package com.codingsample.vendomatic.model.modelone;

import com.codingsample.vendomatic.model.AbstractFixedInventory;
import com.codingsample.vendomatic.model.Item;
import com.codingsample.vendomatic.model.exception.ItemOutOfStockException;
import com.codingsample.vendomatic.model.exception.MaximumItemsInStockException;
import com.codingsample.vendomatic.model.exception.SelectionUnknownException;

import java.util.Queue;
import java.util.stream.IntStream;

public class ModelOneFixedInventory extends AbstractFixedInventory {

    public ModelOneFixedInventory(int numSelections, int capacityPerSelection) {
        super(numSelections, capacityPerSelection);
    }

    @Override
    public void add(int index) throws SelectionUnknownException, MaximumItemsInStockException {
        if(index > 2)
            throw new SelectionUnknownException(); // There can only be 3 options
        // Should NEVER be null
        Queue<Item> q = getStorage().get(index);
        if(q.size()>=5)
            throw new MaximumItemsInStockException();
        q.offer(ModelOneItem.SOME_ITEM);
    }

    @Override
    public Item remove(int index) throws SelectionUnknownException, ItemOutOfStockException {
        if(index > 2)
            throw new SelectionUnknownException();
        // Should NEVER be null
        Queue<Item> q = getStorage().get(index);
        if(q.size()<=0)
            throw new ItemOutOfStockException();
        return q.poll();
    }

    @Override
    public Item peek(int index) throws SelectionUnknownException {
        if(index > 2)
            throw new SelectionUnknownException();
        // Should NEVER be null
        Queue<Item> q = getStorage().get(index);
        return q.peek();
    }

    @Override
    public Integer[] getInventoryQuantity() {
        Integer[] inv = new Integer[getStorage().size()];
        IntStream.range(0, getStorage().size())
                .forEach(index -> {
                    Queue<Item> q = getStorage().get(index);
                    inv[index] = q.size();
                });
        return inv;
    }

    @Override
    public int getInventoryStockQuantity(int index) throws SelectionUnknownException {
        if(index>2)
            throw new SelectionUnknownException();
        return getStorage().get(index)
                .size();
    }
}
