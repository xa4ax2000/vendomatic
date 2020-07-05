package com.codingsample.vendomatic.controller;

import com.codingsample.vendomatic.model.currency.Coin;
import com.codingsample.vendomatic.model.currency.UnitedStatesCoin;
import com.codingsample.vendomatic.model.exception.InvalidCurrencyException;
import com.codingsample.vendomatic.model.exception.MachineDoesNotTakeMultipleCoinsException;
import com.codingsample.vendomatic.model.request.InsertCoinRequest;
import com.codingsample.vendomatic.service.VendingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public static final String MAIN = "/";
    public static final String INVENTORY = "/inventory";

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
            service.insertCoin(coinToQuantityMap);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build(); // todo
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
        return ResponseEntity.ok().build(); // todo
    }

    @RequestMapping(value = INVENTORY, consumes = "application/json", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<?> getAllInventory(){
        return ResponseEntity.ok().build(); // todo
    }

    @RequestMapping(value = INVENTORY + "/{selectionIndex}", consumes = "application/json", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<?> getInventoryOfSelection(){
        return ResponseEntity.ok().build(); // todo
    }

    @RequestMapping(value = INVENTORY + "/{selectionIndex}", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<?> chooseSelection(){
        return ResponseEntity.ok().build(); // todo
    }

    // for DI
    public void setService(VendingMachineService service) {
        this.service = service;
    }
}
