package com.codingsample.vendomatic.model.modelone;

import com.codingsample.vendomatic.model.AbstractVendingMachine;
import com.codingsample.vendomatic.model.Item;
import com.codingsample.vendomatic.model.currency.Coin;
import com.codingsample.vendomatic.model.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelOneVendingMachine extends AbstractVendingMachine {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelOneVendingMachine.class);

    private final Object VM_LOCK = new Object();

    @Override
    public Object getLock() {
        return VM_LOCK;
    }

    /*
     * Constraint #3: Machine only holds five of each of the three beverages available to purchase in its
     * inventory.
     */
    public ModelOneVendingMachine(){
        super(new ModelOneFixedInventory(3, 5));
        setInitialState(new ModelOneInitialState(this));
        setHasMoneyState(new ModelOneHasMoneyState(this));

        // Current State
        setCurrentState(getInitialState());

        initStock();
    }

    /**
     * HARD CODED
     */
    private void initStock(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++){
                try {
                    getInventory().add(i);
                }catch (MaximumItemsInStockException | SelectionUnknownException e){
                    LOGGER.error("There was an issue with your hard coded portion of the code during initialization!!");
                    // Eat up -- Bad practice!
                    // We are doing this to just fully stock up the inventory in the vending machine on initialization
                }
            }
        }
    }

    @Override
    public void insertCoin(int quantity, Coin coin) throws MachineDoesNotTakeMultipleOrNonExistentCoinsException, InvalidCurrencyException{
        try {
            getCurrentState().insertCoin(quantity, coin);
            setCurrentState(getHasMoneyState());
        }catch (MachineDoesNotTakeMultipleOrNonExistentCoinsException e){
            LOGGER.info("This machine should not be able to accept multiple coins at once. Client passed: " + quantity + " coins.");
            throw e;
        }catch (InvalidCurrencyException e){
            LOGGER.info("This machine does not accept currency of: " + coin.getCurrencyCode().name());
            throw e;
        }
    }

    @Override
    public Item selectItem(int index) throws ItemOutOfStockException, InsufficientChangeException, SelectionUnknownException {
        try{
            return getCurrentState().selectItem(index);
        }catch (ItemOutOfStockException e){
            LOGGER.info("Item in selection index: " + index + " is out of stock.");
            throw e;
        }catch (InsufficientChangeException e){
            LOGGER.info("Not enough money to buy the item in selection index: " + index + ". Current balance: " +
                    getCurrentBalance().toPlainString() + ", item cost: " + e.getCostOfItem());
            throw e;
        }catch (SelectionUnknownException e){
            LOGGER.info("Selection is not available for this machine! Index: " + index);
            throw e;
        }
    }
}
