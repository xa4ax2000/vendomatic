package com.codingsample.vendomatic.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class AbstractFixedInventory implements Inventory{

    private List<Queue<Item>> storage;

    public AbstractFixedInventory(int numSelections, int capacityPerSelection){
        this.storage = new ArrayList<>(numSelections);
        for(int i = 0; i < numSelections; i++){
            Queue<Item> q = new LinkedList<>();
            storage.add(q);
        }
    }

    public List<Queue<Item>> getStorage() {
        return storage;
    }
}
