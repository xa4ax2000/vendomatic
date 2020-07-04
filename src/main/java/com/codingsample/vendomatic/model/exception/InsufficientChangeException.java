package com.codingsample.vendomatic.model.exception;

/**
 * Exception thrown when client tries to purchase an item in the vending machine, but
 * the current balance is less than the value of the item.
 */
public class InsufficientChangeException extends Exception{
}
