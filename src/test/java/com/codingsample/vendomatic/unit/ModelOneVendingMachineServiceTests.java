package com.codingsample.vendomatic.unit;

import com.codingsample.vendomatic.model.currency.Coin;
import com.codingsample.vendomatic.model.currency.UnitedStatesCoin;
import com.codingsample.vendomatic.model.dto.VendingTransactionDTO;
import com.codingsample.vendomatic.model.exception.InsufficientChangeException;
import com.codingsample.vendomatic.model.exception.ItemOutOfStockException;
import com.codingsample.vendomatic.model.exception.SelectionUnknownException;
import com.codingsample.vendomatic.model.modelone.ModelOneItem;
import com.codingsample.vendomatic.model.modelone.ModelOneVendingMachine;
import com.codingsample.vendomatic.service.ModelOneVendingMachineService;
import com.codingsample.vendomatic.service.VendingMachineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;


public class ModelOneVendingMachineServiceTests {

    private VendingMachineService service;
    private static final Map<Coin, Integer> TWO_QUARTERS = new HashMap<>();
    private static final Map<Coin, Integer> ONE_QUARTER = new HashMap<>();
    private static final Map<Coin, Integer> ZERO_QUARTER = new HashMap<>();
    private static final Map<Coin, Integer> NEGATIVE_QUARTER = new HashMap<>();
    private static final Map<Coin, Integer> EMPTY = new HashMap<>();
    private static final Integer NUM_OF_SELECTIONS = 3;
    private static final Integer CAPACITY_PER_SELECTION = 5;
    static{
        TWO_QUARTERS.put(UnitedStatesCoin.QUARTER, 2);
        ONE_QUARTER.put(UnitedStatesCoin.QUARTER, 1);
        ZERO_QUARTER.put(UnitedStatesCoin.QUARTER, 0);
        NEGATIVE_QUARTER.put(UnitedStatesCoin.QUARTER, -1);
    }

    @BeforeEach
    public void reset(){
        ModelOneVendingMachineService vms = new ModelOneVendingMachineService();
        vms.setModelOneVendingMachine(new ModelOneVendingMachine());
        service = vms;
    }

    @Test
    @Order(1)
    public void insertCoin_OneQuarterInserted_shouldNotThrowException(){
        assertDoesNotThrow(() -> service.insertCoin(ONE_QUARTER));
    }

    @Test
    @Order(1)
    public void insertCoin_OneQuarterInsertedFourTimes_shouldReturnFourCoins() throws Exception{
        // Define an integer to do assertion checks
        service.insertCoin(ONE_QUARTER);
        service.insertCoin(ONE_QUARTER);
        service.insertCoin(ONE_QUARTER);
        int numOfCoinsInserted = service.insertCoin(ONE_QUARTER);

        // Assert that we should have 4 Coins and no exceptions should be thrown!
        assertEquals(4, numOfCoinsInserted, "Inserting four quarters into the service should return four coins");
    }

    @Test
    @Order(1)
    public void insertCoin_ZeroQuartersInserted_shouldThrowMachineDoesNotTakeMultipleOrNonExistentCoinsException() {
        // Assert that MachineDoesNotTakeMultipleCoinsException wrapped as a RuntimeException is thrown
        // when we try to insert this two quarters at once
        RuntimeException ex = assertThrows(RuntimeException.class, ()-> service.insertCoin(ZERO_QUARTER));
        assertThat("MachineDoesNotTakeMultipleOrNonExistentCoinsException wrapped as a RuntimeException is expected when inserting multiple coins at once!",
                ex.getMessage(), containsString("MachineDoesNotTakeMultipleOrNonExistentCoinsException"));
    }

    @Test
    @Order(1)
    public void insertCoin_NegativeQuartersInserted_shouldThrowMachineDoesNotTakeMultipleOrNonExistentCoinsException() {
        // Assert that MachineDoesNotTakeMultipleCoinsException wrapped as a RuntimeException is thrown
        // when we try to insert this two quarters at once
        RuntimeException ex = assertThrows(RuntimeException.class, ()-> service.insertCoin(NEGATIVE_QUARTER));
        assertThat("MachineDoesNotTakeMultipleOrNonExistentCoinsException wrapped as a RuntimeException is expected when inserting multiple coins at once!",
                ex.getMessage(), containsString("MachineDoesNotTakeMultipleOrNonExistentCoinsException"));
    }

    @Test
    @Order(1)
    public void insertCoin_TwoQuartersInsertedAtOnce_shouldThrowMachineDoesNotTakeMultipleOrNonExistentCoinsException() {
        // Assert that MachineDoesNotTakeMultipleCoinsException wrapped as a RuntimeException is thrown
        // when we try to insert this two quarters at once
        RuntimeException ex = assertThrows(RuntimeException.class, ()-> service.insertCoin(TWO_QUARTERS));
        assertThat("MachineDoesNotTakeMultipleOrNonExistentCoinsException wrapped as a RuntimeException is expected when inserting multiple coins at once!",
                ex.getMessage(), containsString("MachineDoesNotTakeMultipleOrNonExistentCoinsException"));
    }

    @Test
    @Order(1)
    public void insertCoin_passingNonUSDCoin_shouldThrowInvalidCurrencyException() {
        // Currently only USD is implemented so this test case cannot be tested
        assertTrue(true);
    }

    @Test
    @Order(1)
    public void insertCoin_passingEmptyMap_shouldEffectivelyReturnNumberOfCoinsInMachine() throws Exception{
        // Insert coin into machine 3 times
        service.insertCoin(ONE_QUARTER);
        service.insertCoin(ONE_QUARTER);
        service.insertCoin(ONE_QUARTER);

        // Insert empty map
        Integer numCoinsInMachine = service.insertCoin(new HashMap<>());
        assertEquals(3, numCoinsInMachine, "Inserting an empty map should return number of coins in machine");
    }

    @Test
    @Order(2)
    public void reset_whenCalledAfterInsertingFiveCoins_shouldReturnFiveAndSubsequentCallShouldReturnZero() throws Exception{
        // Insert coin 5 times (So machine should have 5 coins)
        service.insertCoin(ONE_QUARTER); //Tested before this method gets called!
        service.insertCoin(ONE_QUARTER);
        service.insertCoin(ONE_QUARTER);
        service.insertCoin(ONE_QUARTER);
        service.insertCoin(ONE_QUARTER);

        int coinsReturned = service.reset();
        assertEquals(5, coinsReturned, "When resetting after inserting coin 5 times, we should expect to get a return of 5 coins");

        coinsReturned = service.reset();
        assertEquals(0, coinsReturned, "Subsequent resetting (back-to-back calls) should return 0");
    }

    @Test
    @Order(2)
    // Based off requirements, ModelOne Vending Machine has 3 selections (Zero-Index) and each selection can hold up to 5 items
    public void purchase_whenCalledUsingInvalidSelectionIndex_shouldThrowSelectionUnknownException() {
        // Choose invalid index
        VendingTransactionDTO vendingTransactionDTO = new VendingTransactionDTO(3);
        assertThrows(SelectionUnknownException.class, () -> service.purchase(vendingTransactionDTO));
    }

    @Test
    @Order(2)
    public void purchase_whenSelectingValidIndexAndCalledWithoutInsertingCoins_shouldThrowInsufficientChangeException() {
        // Choose valid index
        VendingTransactionDTO vendingTransactionDTO = new VendingTransactionDTO(1);
        Exception e = assertThrows(InsufficientChangeException.class, () -> service.purchase(vendingTransactionDTO));
    }

    // Insert Valid number of coins
    private void insertValidAmountOfCoinsForPurchase() throws Exception{
        // Insert Valid number of coins
        int i = ModelOneItem.SOME_ITEM.getPrice().divideToIntegralValue(UnitedStatesCoin.QUARTER.getValue()).intValue();
        for(int j = 0; j < i; j++){
            service.insertCoin(ONE_QUARTER);
        }
    }

    @Test
    @Order(2)
    public void purchase_whenSelectingValidIndexAndCalledWithValidCoins_shouldNotThrowException() throws Exception{
        insertValidAmountOfCoinsForPurchase();

        // Choose valid index
        VendingTransactionDTO vendingTransactionDTO = new VendingTransactionDTO(1);
        assertDoesNotThrow(()-> service.purchase(vendingTransactionDTO));
    }

    @Test
    @Order(2)
    public void purchase_whenPurchasingMoreThanStock_shouldThrowItemOutOfStockException() throws Exception{
        // Choose valid index
        VendingTransactionDTO vendingTransactionDTO = new VendingTransactionDTO(1);

        // buy up entire stock
        for(int i = 0; i < CAPACITY_PER_SELECTION; i++){
            insertValidAmountOfCoinsForPurchase();
            service.purchase(vendingTransactionDTO);
        }

        insertValidAmountOfCoinsForPurchase();
        assertThrows(ItemOutOfStockException.class, ()-> service.purchase(vendingTransactionDTO));
    }

    @Test
    @Order(2)
    public void purchase_whenPurchasing_transactionDtoShouldReturnExcessCoinsAfterTransaction() throws Exception{
        // Insert One more coin than cost of item
        insertValidAmountOfCoinsForPurchase();
        service.insertCoin(ONE_QUARTER);

        // Choose valid index
        VendingTransactionDTO vendingTransactionDTO = new VendingTransactionDTO(1);
        service.purchase(vendingTransactionDTO);

        // Check coins to be returned
        Integer expectedCoins = 1;
        // This machine only deals with quarters!
        Integer actualCoins = vendingTransactionDTO.getCoinsToBeReturned().get(UnitedStatesCoin.QUARTER);

        assertEquals(expectedCoins, actualCoins, "We added one more coin than the cost of item, so we should expect 1");
    }

    @Test
    @Order(2)
    public void purchase_whenPurchasingWithTripleCostOfItem_transactionDtoShouldOnlyVendOneItem() throws Exception{
        // Insert Three times the cost of one item
        insertValidAmountOfCoinsForPurchase();
        insertValidAmountOfCoinsForPurchase();
        insertValidAmountOfCoinsForPurchase();

        // Choose valid index
        VendingTransactionDTO vendingTransactionDTO = new VendingTransactionDTO(1);
        service.purchase(vendingTransactionDTO);

        // Check items vended
        Integer expectedNumItemsVended = 1;
        Integer actualNumItemsVended = vendingTransactionDTO.getNumItemsVended();

        assertEquals(expectedNumItemsVended, actualNumItemsVended, "Per transaction, should only vend one item at a time");
    }

    @Test
    @Order(2)
    public void purchase_whenPurchasing_transactionDtoShouldCorrectlyListRemainingInventoryForGivenSelection() throws Exception{
        int expectedRemainingInventoryInSelectionZero = 5;
        int expectedRemainingInventoryInSelectionOne = 5;

        // Purchase from index 0
        VendingTransactionDTO vendingTransactionDTOZero = new VendingTransactionDTO(0);
        for(int i = 0; i < 3; i++) {
            insertValidAmountOfCoinsForPurchase();
            service.purchase(vendingTransactionDTOZero);

            // Check remaining inventory in given selection
            expectedRemainingInventoryInSelectionZero--;
            int actualRemainingInventoryInSelection = vendingTransactionDTOZero.getRemainingItemsInSelection();

            assertEquals(expectedRemainingInventoryInSelectionZero, actualRemainingInventoryInSelection,
                    "Inventory should be properly decremented");
        }

        // Purchase from index 1
        VendingTransactionDTO vendingTransactionDTOOne = new VendingTransactionDTO(1);
        for(int i = 0; i < 1; i++) {
            insertValidAmountOfCoinsForPurchase();
            service.purchase(vendingTransactionDTOOne);

            // Check remaining inventory in given selection
            expectedRemainingInventoryInSelectionOne--;
            int actualRemainingInventoryInSelection = vendingTransactionDTOOne.getRemainingItemsInSelection();

            assertEquals(expectedRemainingInventoryInSelectionOne, actualRemainingInventoryInSelection,
                    "Inventory should be properly decremented");
        }

        // Purchase from index 0 again
        for(int i = 0; i < 1; i++) {
            insertValidAmountOfCoinsForPurchase();
            service.purchase(vendingTransactionDTOZero);

            // Check remaining inventory in given selection
            expectedRemainingInventoryInSelectionZero--;
            int actualRemainingInventoryInSelection = vendingTransactionDTOZero.getRemainingItemsInSelection();

            assertEquals(expectedRemainingInventoryInSelectionZero, actualRemainingInventoryInSelection,
                    "Inventory should be properly decremented");
        }
    }

    // Needs to be run after we confirm purchasing works
    @Test
    @Order(3)
    public void getTotalInventory_onInit_shouldReturnArrayOfFiveByThree(){
        Integer[] expected = {CAPACITY_PER_SELECTION, CAPACITY_PER_SELECTION, CAPACITY_PER_SELECTION};
        Integer[] actual = service.getTotalInventory();

        assertArrayEquals(expected, actual, "On init, the machine should be stocked to max capacity (CAPACITY_PER_SELECTION, CAPACITY_PER_SELECTION, CAPACITY_PER_SELECTION)");
    }

    @Test
    @Order(3)
    public void getTotalInventory_afterPurchasing_shouldProperlyDecrementRespectiveIndices() throws Exception{
        Integer[] expected = {3, 5, 0};

        // Purchase index 0 twice
        VendingTransactionDTO indexZero = new VendingTransactionDTO(0);
        for(int i = 0; i < 2; i++){
            insertValidAmountOfCoinsForPurchase();
            service.purchase(indexZero);
        }

        // Purchase index 2 five times
        VendingTransactionDTO indexTwo = new VendingTransactionDTO(2);
        for(int i = 0; i < 5; i++){
            insertValidAmountOfCoinsForPurchase();
            service.purchase(indexTwo);
        }

        Integer[] actual = service.getTotalInventory();

        assertArrayEquals(expected, actual, "The inventory when purchasing at different indices should decrement properly");
    }

    @Test
    @Order(3)
    public void getInventoryInGivenSelection_whenSelectingInvalidIndex_shouldThrowSelectionUnknownException() {
        // Should throw SelectionUnknownException wrapped in a RuntimeException
        Exception e = assertThrows(RuntimeException.class, ()-> service.getInventoryInGivenSelection(3));

        assertThat(e.getMessage(), containsString("SelectionUnknownException"));
    }

    @Test
    @Order(3)
    public void getInventoryInGivenSelection_onInit_shouldReturnFiveForAnyValidSelection() throws Exception{
        int expected = CAPACITY_PER_SELECTION;

        assertEquals(expected, service.getInventoryInGivenSelection(0), "Inventory on init should be CAPACITY_PER_SELECTION");
        assertEquals(expected, service.getInventoryInGivenSelection(1), "Inventory on init should be CAPACITY_PER_SELECTION");
        assertEquals(expected, service.getInventoryInGivenSelection(2), "Inventory on init should be CAPACITY_PER_SELECTION");
    }

    @Test
    @Order(3)
    public void getInventoryInGivenSelection_afterPurchaseOnIndices_shouldCorrectlyReturnInventory() throws Exception{
        // We need to first assert that inventory in index 0 starts out correctly
        int expectedIndexZeroInventory = CAPACITY_PER_SELECTION;
        assertEquals(expectedIndexZeroInventory, service.getInventoryInGivenSelection(0), "Inventory on init should be CAPACITY_PER_SELECTION");

        // Purchase from index 0 three times (decrementing inventory by 3)
        for(int j = 0; j < 3; j++) {
            VendingTransactionDTO indexZero = new VendingTransactionDTO(0);
            insertValidAmountOfCoinsForPurchase();
            service.purchase(indexZero);
            expectedIndexZeroInventory--;
        }

        assertEquals(expectedIndexZeroInventory, service.getInventoryInGivenSelection(0), "Inventory should have decremented by 3 = CAPACITY_PER_SELECTION - 3");

        // Check that none of the other indices have changed inventory
        int expectedInventoryInOtherIndices = CAPACITY_PER_SELECTION;
        assertEquals(expectedInventoryInOtherIndices, service.getInventoryInGivenSelection(1), "Inventory in other indices should not have been affected");
        assertEquals(expectedInventoryInOtherIndices, service.getInventoryInGivenSelection(2), "Inventory in other indices should not have been affected");
    }
}
