package com.codingsample.vendomatic.integration;

import com.codingsample.vendomatic.controller.ModelOneApiController;
import com.codingsample.vendomatic.model.currency.UnitedStatesCoin;
import com.codingsample.vendomatic.model.modelone.ModelOneItem;
import com.codingsample.vendomatic.model.modelone.ModelOneVendingMachine;
import com.codingsample.vendomatic.service.ModelOneVendingMachineService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ModelOneApiControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private static final Integer NUM_OF_SELECTIONS = 3;
    private static final Integer CAPACITY_PER_SELECTION = 5;

    private ModelOneApiController modelOneApiController;

    @BeforeEach
    public void setup(){
        modelOneApiController = new ModelOneApiController();
        ModelOneVendingMachineService vms = new ModelOneVendingMachineService();
        vms.setModelOneVendingMachine(new ModelOneVendingMachine());
        modelOneApiController.setService(vms);
        mockMvc = MockMvcBuilders.standaloneSetup(modelOneApiController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void givenValidCoinQuantity_whenCoinInsertApiIsCalled_then204IsReceived() throws Exception {

        MvcResult result = mockMvc.perform(put("/")
                .content("{\"coin\":1}")
                .contentType("application/json"))
                .andExpect(status().is(204))
                .andReturn();
    }

    @Test
    public void givenValidCoinQuantity_whenCoinInsertApiIsCalledThreeTimes_thenResponseHeaderShouldReturnThreeTimes() throws Exception {

        mockMvc.perform(put("/")
                .content("{\"coin\":1}")
                .contentType("application/json"));
        mockMvc.perform(put("/")
                .content("{\"coin\":1}")
                .contentType("application/json"));

        // Third time
        MvcResult result = mockMvc.perform(put("/")
                .content("{\"coin\":1}")
                .contentType("application/json"))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        String coinsAccepted = response.getHeader("X-Coins");

        assertEquals("3", coinsAccepted, "After inserting coin three times, we should expect to see 3");

    }

    @Test
    public void givenInvalidCoinQuantity_whenCoinInsertApiIsCalled_then400IsReceived() throws Exception {

        mockMvc.perform(put("/")
                .content("{\"coin\":2}")
                .contentType("application/json"))
                .andExpect(status().is(400));

    }

    @Test
    public void whenResetApiIsCalled_then204IsReceived() throws Exception {
        mockMvc.perform(delete("/")
                .contentType("application/json"))
                .andExpect(status().is(204));

    }

    @Test
    public void givenThreeCoinsWereInserted_whenResetApiIsCalled_thenResponseHeaderShouldReturnThree() throws Exception {

        mockMvc.perform(put("/")
                .content("{\"coin\":1}")
                .contentType("application/json"));
        mockMvc.perform(put("/")
                .content("{\"coin\":1}")
                .contentType("application/json"));
        mockMvc.perform(put("/")
                .content("{\"coin\":1}")
                .contentType("application/json"));

        MvcResult result = mockMvc.perform(delete("/")
                .contentType("application/json"))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        String coinsReturned = response.getHeader("X-Coins");

        assertEquals("3", coinsReturned, "After inserting coin three times, we should expect to see 3");
    }

    @Test
    public void whenGetTotalInventoryIsCalled_then200IsReceived() throws Exception {

        mockMvc.perform(get("/inventory")
                .contentType("application/json"))
                .andExpect(status().is(200));
    }

    @Test
    public void givenInitialState_whenGetTotalInventoryIsCalled_thenResponseHeaderShouldReturnInventory() throws Exception {

        List<Integer> expected = Arrays.asList(CAPACITY_PER_SELECTION, CAPACITY_PER_SELECTION, CAPACITY_PER_SELECTION);
        MvcResult result = mockMvc.perform(get("/inventory")
                .contentType("application/json"))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        String totalInventory = response.getContentAsString();
        List<Integer> results = objectMapper.readValue(totalInventory, new TypeReference<List<Integer>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        });

        assertThat(results, IsIterableContainingInOrder.contains(expected.toArray()));
    }

    @Test
    public void givenValidSelectionId_whenGetInventoryForSelectionApiIsCalled_then200IsReceived() throws Exception {
        mockMvc.perform(get("/inventory/{id}", 1)
                .contentType("application/json"))
                .andExpect(status().is(200));
    }

    @Test
    public void givenInvalidSelectionId_whenGetInventoryForSelectionApiIsCalled_then400IsReceived() throws Exception {
        mockMvc.perform(get("/inventory/{id}", 5)
                .contentType("application/json"))
                .andExpect(status().is(400));
    }

    @Test
    public void givenValidSelectionId_whenGetInventoryForSelectionApiIsCalled_thenInventoryQuantityForSelectionIsReceived() throws Exception {
        MvcResult result = mockMvc.perform(get("/inventory/{id}", 1)
                .contentType("application/json"))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();
        String expected = ""+CAPACITY_PER_SELECTION;

        assertEquals(expected, content, "On initial load, inventory should be CAPACITY_PER_SELECTION");
    }

    @Test
    public void givenInvalidSelectionId_whenPurchaseApiIsCalled_then400IsReceived() throws Exception {
        mockMvc.perform(put("/inventory/{id}", 5)
                .contentType("application/json"))
                .andExpect(status().is(400));
    }

    @Test
    public void givenValidSelectionIdAndInsufficientCoins_whenPurchaseApiIsCalled_then403IsReceived() throws Exception {
        mockMvc.perform(put("/inventory/{id}", 1)
                .contentType("application/json"))
                .andExpect(status().is(403));
    }

    @Test
    public void givenValidSelectionIdAndInsufficientCoins_whenPurchaseApiIsCalled_thenResponseHeaderReturnsCoins() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(put("/inventory/{id}", 1)
                .contentType("application/json"))
                .andReturn()
                .getResponse();
        String expected = "0";
        String coinsReturned = response.getHeader("X-Coins");
        assertEquals(expected, coinsReturned, "On initial load when machine has no coins, we should expect it to return 0");

        // Insert one coin (not sufficient for a purchase)
        mockMvc.perform(put("/")
                .content("{\"coin\":1}")
                .contentType("application/json"));

        response = mockMvc.perform(put("/inventory/{id}", 1)
                .contentType("application/json"))
                .andReturn()
                .getResponse();
        expected = "1";
        coinsReturned = response.getHeader("X-Coins");
        assertEquals(expected, coinsReturned, "When inserting one coin and it's not sufficient, we should expect it to return 1");
    }

    private void insertCoinsForOnePurchase() throws Exception{
        int repititions = ModelOneItem.SOME_ITEM.getPrice().divideToIntegralValue(UnitedStatesCoin.QUARTER.getValue()).intValue();
        for(int i = 0; i < repititions; i++) {
            mockMvc.perform(put("/")
                    .content("{\"coin\":1}")
                    .contentType("application/json"));
        }
    }

    @Test
    public void givenValidSelectionIdAndInsufficientStock_whenPurchaseApiIsCalled_then404IsReceived() throws Exception {
        // Wipe out stock for index 1
        for(int i = 0; i < CAPACITY_PER_SELECTION; i++) {
            insertCoinsForOnePurchase();
            mockMvc.perform(put("/inventory/{id}", 1)
                    .contentType("application/json"));
        }

        // Expect 404
        insertCoinsForOnePurchase();
        mockMvc.perform(put("/inventory/{id}", 1)
                .contentType("application/json"))
                .andExpect(status().is(404));
    }

    @Test
    public void givenValidSelectionIdAndInsufficientStock_whenPurchaseApiIsCalled_thenResponseHeaderReturnsCoins() throws Exception {
        // Wipe out stock for index 1
        for(int i = 0; i < CAPACITY_PER_SELECTION; i++) {
            insertCoinsForOnePurchase();
            mockMvc.perform(put("/inventory/{id}", 1)
                    .contentType("application/json"));
        }

        // Now lets try purchasing one more item from out of stock selection
        insertCoinsForOnePurchase();

        MockHttpServletResponse response = mockMvc.perform(put("/inventory/{id}", 1)
                .contentType("application/json"))
                .andReturn()
                .getResponse();

        String expected = ModelOneItem.SOME_ITEM.getPrice().divideToIntegralValue(UnitedStatesCoin.QUARTER.getValue()).toPlainString();
        String coinsAccepted = response.getHeader("X-Coins");
        assertEquals(expected, coinsAccepted, "We should get back the coins when attempting to purchase from out of stock selection");
    }

    @Test
    public void givenValidSelectionIdAndSufficientStock_whenPurchaseApiIsCalled_then200IsReceived() throws Exception {
        insertCoinsForOnePurchase();
        mockMvc.perform(put("/inventory/{id}", 1)
                .contentType("application/json"))
                .andExpect(status().is(200));
    }

    @Test
    public void givenValidSelectionIdAndSufficientStock_whenPurchaseApiIsCalled_thenResponseHeaderReturnsCoins() throws Exception {
        insertCoinsForOnePurchase();

        // Let's insert one more coin
        mockMvc.perform(put("/")
                .content("{\"coin\":1}")
                .contentType("application/json"));

        MockHttpServletResponse response = mockMvc.perform(put("/inventory/{id}", 1)
                .contentType("application/json"))
                .andReturn()
                .getResponse();

        String expected = "1";
        String actual = response.getHeader("X-Coins");

        assertEquals(expected, actual, "We should get back the remainder of coins");
    }

    @Test
    public void givenValidSelectionIdAndSufficientStock_whenPurchaseApiIsCalled_thenResponseHeaderReturnsRemainingInventoryInSelection() throws Exception {
        insertCoinsForOnePurchase();

        MockHttpServletResponse response = mockMvc.perform(put("/inventory/{id}", 1)
                .contentType("application/json"))
                .andReturn()
                .getResponse();

        String expected = "4";
        String actual = response.getHeader("X-Inventory-Remaining");

        assertEquals(expected, actual, "We should get back the CAPACITY_PER_SELECTION - 1");
    }

    @Test
    public void givenValidSelectionIdAndSufficientStock_whenPurchaseApiIsCalled_thenContentReturnsNumItemVended() throws Exception {
        insertCoinsForOnePurchase();

        // Let's insert one more coin
        mockMvc.perform(put("/")
                .content("{\"coin\":1}")
                .contentType("application/json"));

        MockHttpServletResponse response = mockMvc.perform(put("/inventory/{id}", 1)
                .contentType("application/json"))
                .andReturn()
                .getResponse();

        String expected = "{\"quantity\":1}";
        String actual = response.getContentAsString();
        System.out.println(actual);
        assertEquals(expected, actual, "A transaction will only return one item, the rest will return coins");
    }
}
