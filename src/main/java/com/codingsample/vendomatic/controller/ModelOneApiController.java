package com.codingsample.vendomatic.controller;

import com.codingsample.vendomatic.model.currency.Coin;
import com.codingsample.vendomatic.model.currency.UnitedStatesCoin;
import com.codingsample.vendomatic.model.dto.VendingTransactionDTO;
import com.codingsample.vendomatic.model.exception.*;
import com.codingsample.vendomatic.model.request.InsertCoinRequest;
import com.codingsample.vendomatic.model.response.PurchaseResponse;
import com.codingsample.vendomatic.service.VendingMachineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
/*
 * Constraint #5: All test interactions will be performed with a single content type of “application/json”.
 *
 * If you check all of the Request Mappings, it consumes application/json as content type!
 */
public class ModelOneApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelOneApiController.class);

    private final String MAIN = "/";
    private final String INVENTORY = "/inventory";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    // Since only one instance of this type exists we don't need qualifier!
    private VendingMachineService service;

    @RequestMapping(value = MAIN, consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<?> insertCoin(@RequestBody InsertCoinRequest insertCoinRequest){
        try {
            // Validate Request
            validateInsertCoinRequest(insertCoinRequest);
            Map<Coin, Integer> coinToQuantityMap = new HashMap<>();
            // Constraint #1: Machine only accepts US quarters
            coinToQuantityMap.put(UnitedStatesCoin.QUARTER, insertCoinRequest.getCoinQuantity());
            int numCoinsAccepted = service.insertCoin(coinToQuantityMap);

            return ResponseEntity
                    .status(204)
                    .header("X-Coins", String.valueOf(numCoinsAccepted))
                    .build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Will validate request
     * @param insertCoinRequest the request to validate
     * @throws Exception when coin quantity in request is a value other than 1
     */
    /*
     * Constraint #1: (...) you can only put one coin in at a time.
     */
    private void validateInsertCoinRequest(InsertCoinRequest insertCoinRequest) throws Exception{
        if(insertCoinRequest.getCoinQuantity()!=1){
            throw new Exception("This machine can physically only handle one coin at a time!");
        }
    }

    @RequestMapping(value = MAIN, consumes = "application/json", produces = "application/json", method = RequestMethod.DELETE)
    public ResponseEntity<?> reset(){
        int numCoinsToBeReturned = service.reset();
        return ResponseEntity
                .status(204)
                .header("X-Coins", String.valueOf(numCoinsToBeReturned))
                .build();
    }

    @RequestMapping(value = INVENTORY, consumes = "application/json", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<?> getAllInventory(){
        Integer[] inventory = service.getTotalInventory();
        return ResponseEntity
                .status(200)
                .body(inventory);

    }

    @RequestMapping(value = INVENTORY + "/{selectionIndex}", consumes = "application/json", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<?> getInventoryOfSelection(@PathVariable int selectionIndex){
        try {
            Integer inventoryAmt = service.getInventoryInGivenSelection(selectionIndex);
            return ResponseEntity.ok(inventoryAmt);
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .build();
        }
    }

    @RequestMapping(value = INVENTORY + "/{selectionIndex}", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<?> chooseSelection(@PathVariable int selectionIndex){
        VendingTransactionDTO vendingTransactionDTO = new VendingTransactionDTO(selectionIndex);
        try{
            service.purchase(vendingTransactionDTO);
            String jsonResponseBody = objectMapper.writeValueAsString(new PurchaseResponse(vendingTransactionDTO.getNumItemsVended()));
            return ResponseEntity
                    .status(200)
                    .header("X-Coins", String.valueOf(
                            vendingTransactionDTO.getCoinsToBeReturned()
                                    .values().stream()
                                    .mapToInt(Integer::intValue)
                                    .sum()
                    ))
                    .header("X-Inventory-Remaining", String.valueOf(vendingTransactionDTO.getRemainingItemsInSelection()))
                    .body(jsonResponseBody);
        } catch (SelectionUnknownException e) {
            return ResponseEntity
                    .badRequest()
                    .build();
        } catch (InsufficientChangeException e) {
            return ResponseEntity
                    .status(403)
                    .header("X-Coins", String.valueOf(
                            vendingTransactionDTO.getCoinsToBeReturned()
                                    .values().stream()
                                    .mapToInt(Integer::intValue)
                                    .sum()
                    ))
                    .build();
        } catch (ItemOutOfStockException e) {
            Integer coins = vendingTransactionDTO.getCoinsToBeReturned()
                    .values().stream()
                    .mapToInt(Integer::intValue)
                    .sum();
            return ResponseEntity
                    .status(404)
                    .header("X-Coins", String.valueOf(
                            vendingTransactionDTO.getCoinsToBeReturned()
                                    .values().stream()
                                    .mapToInt(Integer::intValue)
                                    .sum()
                    ))
                    .build();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    // for DI
    public void setService(VendingMachineService service) {
        this.service = service;
    }
}
