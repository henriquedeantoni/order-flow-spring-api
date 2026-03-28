package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.models.Supply;
import com.orderflow.orderflow_api.payload.SupplyDTO;
import com.orderflow.orderflow_api.services.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;

@RestController
@RequestMapping("/V1/auth")
public class SupplyController {
    @Autowired
    private SupplyService supplyService;

    @PostMapping("/admin/supplies/register")
    public ResponseEntity<SupplyDTO> registerSupply(
            @RequestBody SupplyDTO supplyDTO
    ){
        SupplyDTO savedSupply = supplyService.registerSupply(supplyDTO);
        return new ResponseEntity<>(savedSupply, HttpStatus.CREATED);
    }

    @PutMapping("/admin/supplies/{supplyId}")
    public ResponseEntity<SupplyDTO> updateSupply(
            @PathVariable Long supplyId,
            @RequestBody SupplyDTO supplyDTO
    ){
        SupplyDTO supplyUpdateDTO = supplyService.updateSupply(supplyId, supplyDTO);
        return new ResponseEntity<>(supplyUpdateDTO, HttpStatus.OK);
    }
}
