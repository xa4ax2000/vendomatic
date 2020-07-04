package com.codingsample.vendomatic.model;

import com.codingsample.vendomatic.model.exception.SelectionUnknownException;

import java.math.BigDecimal;

public abstract class AbstractVendingMachine implements VendingMachine{

    // States
    private State initialState;
    private State hasMoneyState;

    private State currentState;

    private BigDecimal currentBalance;
    private Inventory inventory;

    // Constructor
    protected AbstractVendingMachine(Inventory inventory){
        this.currentBalance = new BigDecimal("0.00");
        this.inventory = inventory;
    }

    @Override
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    @Override
    public void reset() {
        setCurrentBalance(new BigDecimal("0.00"));
        setCurrentState(initialState);
    }

    @Override
    public Integer getInventoryForSelection(int index) throws SelectionUnknownException {
        return getInventory().getInventoryStockQuantity(index);
    }

    @Override
    public Integer[] getEntireInventory() {
        return getInventory().getInventoryQuantity();
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getInitialState() {
        return initialState;
    }

    public State getHasMoneyState() {
        return hasMoneyState;
    }

    public void setInitialState(State initialState) {
        this.initialState = initialState;
    }

    public void setHasMoneyState(State hasMoneyState) {
        this.hasMoneyState = hasMoneyState;
    }

}
