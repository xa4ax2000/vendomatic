package com.codingsample.vendomatic.model.modelone;

import com.codingsample.vendomatic.model.Item;
import com.codingsample.vendomatic.model.State;
import com.codingsample.vendomatic.model.currency.Coin;
import com.codingsample.vendomatic.model.currency.CurrencyCode;
import com.codingsample.vendomatic.model.exception.*;

public class ModelOneHasMoneyState implements State {

    private ModelOneVendingMachine machine;

    public ModelOneHasMoneyState(ModelOneVendingMachine machine){
        this.machine = machine;
    }

    @Override
    public void insertCoin(int quantity, Coin coin) throws MachineDoesNotTakeMultipleCoinsException, InvalidCurrencyException {
        if(quantity != 1)
            throw new MachineDoesNotTakeMultipleCoinsException();
        if(coin.getCurrencyCode() != CurrencyCode.USD)
            throw new InvalidCurrencyException();
        machine.setCurrentBalance(
                machine.getCurrentBalance().add(coin.getValue())
        );
    }

    @Override
    public Item selectItem(int index) throws SelectionUnknownException, ItemOutOfStockException, InsufficientChangeException {
        Item item = machine.getInventory().peek(index);
        if(item == null)
            throw new ItemOutOfStockException();
        if(machine.getCurrentBalance().compareTo(item.getPrice()) < 0)
            throw new InsufficientChangeException(item.getPrice());
        // For this model we will return all coins instead of holding onto it so change state back to initial state
        machine.setCurrentState(machine.getInitialState());
        // pop and return
        return machine.getInventory().remove(index);
    }
}
