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
@RequestMapping("/V1")
public class SupplyController {
    @Autowired
    private SupplyService supplyService;

    @PostMapping("/admin/supplies/register")
    public ResponseEntity<SupplyDTO> registerSupply(
            @RequestBody SupplyDTO supplyDTO
    ){
        SupplyDTO savedSupply = supplyService.registerSupply(supplyDTO);
        return new ResponseEntity<>(savedSupply, HttpStatus.OK);
    }
}
