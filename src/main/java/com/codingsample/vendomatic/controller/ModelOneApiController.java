package com.codingsample.vendomatic.controller;

import com.codingsample.vendomatic.model.VendingMachine;
import com.codingsample.vendomatic.model.currency.UnitedStatesCoin;
import com.codingsample.vendomatic.model.request.InsertCoinRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ModelOneApiController {

    public static final String MAIN = "/";
    public static final String INVENTORY = "/inventory";

    @Autowired
    // Since only one instance of this type exists we don't need qualifier!
    private VendingMachine machine;

    @RequestMapping(value = MAIN, consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<?> insertCoin(@RequestBody InsertCoinRequest insertCoinRequest){
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = MAIN, consumes = "application/json", produces = "application/json", method = RequestMethod.DELETE)
    public ResponseEntity<?> reset(){
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = INVENTORY, consumes = "application/json", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<?> getAllInventory(){
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = INVENTORY + "/{selectionIndex}", consumes = "application/json", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<?> getInventoryOfSelection(){
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = INVENTORY + "/{selectionIndex}", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<?> chooseSelection(){
        return ResponseEntity.ok().build();
    }

    // for DI
    public void setMachine(VendingMachine machine) {
        this.machine = machine;
    }
}
