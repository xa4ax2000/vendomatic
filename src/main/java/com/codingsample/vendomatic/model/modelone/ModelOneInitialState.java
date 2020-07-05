package com.codingsample.vendomatic.model.modelone;

import com.codingsample.vendomatic.model.Item;
import com.codingsample.vendomatic.model.State;
import com.codingsample.vendomatic.model.currency.Coin;
import com.codingsample.vendomatic.model.currency.CurrencyCode;
import com.codingsample.vendomatic.model.exception.*;

public class ModelOneInitialState implements State {

    private final ModelOneVendingMachine machine;

    public ModelOneInitialState(ModelOneVendingMachine machine){
        this.machine = machine;
    }

    @Override
    public void insertCoin(int quantity, Coin coin) throws MachineDoesNotTakeMultipleOrNonExistentCoinsException, InvalidCurrencyException{
        if(quantity != 1)
            throw new MachineDoesNotTakeMultipleOrNonExistentCoinsException();
        if(coin.getCurrencyCode() != CurrencyCode.USD)
            throw new InvalidCurrencyException();
        machine.setCurrentBalance(
                machine.getCurrentBalance().add(coin.getValue())
        );
        machine.setCurrentState(machine.getHasMoneyState());
    }

    @Override
    public Item selectItem(int index) throws SelectionUnknownException, InsufficientChangeException{
        // For this model we just say they don't have enough money
        Item item = machine.getInventory().peek(index);
        throw new InsufficientChangeException(item!=null ? item.getPrice() : null);
    }


}
