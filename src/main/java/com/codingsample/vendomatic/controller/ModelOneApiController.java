package com.codingsample.vendomatic.controller;

import com.codingsample.vendomatic.model.VendingMachine;
import com.codingsample.vendomatic.model.currency.UnitedStatesCoin;
import com.codingsample.vendomatic.model.request.InsertCoinRequest;
import com.codingsample.vendomatic.service.VendingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ModelOneApiController {

    public static final String MAIN = "/";
    public static final String INVENTORY = "/inventory";

    @Autowired
    // Since only one instance of this type exists we don't need qualifier!
    private VendingMachineService service;

    @RequestMapping(value = MAIN, consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<?> insertCoin(@RequestBody InsertCoinRequest insertCoinRequest){
        return ResponseEntity.ok().build(); // todo
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
