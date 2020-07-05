package com.codingsample.vendomatic.service;

import com.codingsample.vendomatic.model.Item;
import com.codingsample.vendomatic.model.VendingMachine;
import com.codingsample.vendomatic.model.currency.Coin;
import com.codingsample.vendomatic.model.currency.UnitedStatesCoin;
import com.codingsample.vendomatic.model.dto.VendingTransactionDTO;
import com.codingsample.vendomatic.model.exception.*;
import com.codingsample.vendomatic.model.modelone.ModelOneVendingMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ModelOneVendingMachineService implements VendingMachineService{

    @Autowired
    // In hte future when this service manages multiple vending machines we can make it generic
    private VendingMachine modelOneVendingMachine;

    @Override
    public int insertCoin(Map<Coin, Integer> coinToQuantityMap){
        synchronized (modelOneVendingMachine.getLock()){
            coinToQuantityMap.forEach( (coin, quantity) -> {
                try {
                    modelOneVendingMachine.insertCoin(quantity, coin);
                }catch (MachineDoesNotTakeMultipleCoinsException | InvalidCurrencyException e){
                    throw new RuntimeException(e);
                }
            });
            return modelOneVendingMachine.getCurrentBalance()
                    .divideToIntegralValue(UnitedStatesCoin.QUARTER.getValue())
                    .intValue();
        }
    }

    @Override
    public int reset() {
        synchronized (modelOneVendingMachine.getLock()){
            int coinsToReturn = modelOneVendingMachine.getCurrentBalance()
                    .divideToIntegralValue(UnitedStatesCoin.QUARTER.getValue())
                    .intValue();
            modelOneVendingMachine.reset();
            return coinsToReturn;
        }
    }

    @Override
    public Integer[] getTotalInventory() {
        synchronized (modelOneVendingMachine.getLock()){
            return modelOneVendingMachine.getEntireInventory();
        }
    }

    @Override
    public int getInventoryInGivenSelection(int index){
        synchronized (modelOneVendingMachine.getLock()){
            try {
                return modelOneVendingMachine.getInventoryForSelection(index);
            }catch (SelectionUnknownException e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    /*
     * Constraint #4: (...) but will only dispense a single
     * beverage per transaction.
     */
    public void purchase(VendingTransactionDTO vendingTransactionDTO) throws SelectionUnknownException, InsufficientChangeException, ItemOutOfStockException {
        synchronized (modelOneVendingMachine.getLock()){
            Map<Coin, Integer> coinsToQuantityMap = new HashMap<>();
            try {
                int indexToPurchase = vendingTransactionDTO.getIndexToPurchase();
                Item item = modelOneVendingMachine.selectItem(indexToPurchase);
                // is this always 1?
                vendingTransactionDTO.setNumItemsVended(1);
                vendingTransactionDTO.setRemainingItemsInSelection(modelOneVendingMachine.getInventoryForSelection(indexToPurchase));
                /*
                Upon transaction completion, any unused quarters must be dispensed back to the
                customer.
                 */
                int coinsToBeReturned = modelOneVendingMachine.getCurrentBalance()
                        .subtract(item.getPrice())
                        .divideToIntegralValue(UnitedStatesCoin.QUARTER.getValue())
                        .intValue();
                modelOneVendingMachine.reset();
                coinsToQuantityMap.put(UnitedStatesCoin.QUARTER, coinsToBeReturned);
                vendingTransactionDTO.setCoinsToBeReturned(coinsToQuantityMap);
            }catch (InsufficientChangeException | ItemOutOfStockException e) {
                int coinsToBeReturned = modelOneVendingMachine.getCurrentBalance()
                        .divideToIntegralValue(UnitedStatesCoin.QUARTER.getValue())
                        .intValue();
                modelOneVendingMachine.reset();
                coinsToQuantityMap.put(UnitedStatesCoin.QUARTER, coinsToBeReturned);
                vendingTransactionDTO.setCoinsToBeReturned(coinsToQuantityMap);
                throw e;
            }
        }
    }

    public void setModelOneVendingMachine(ModelOneVendingMachine modelOneVendingMachine) {
        this.modelOneVendingMachine = modelOneVendingMachine;
    }
}
