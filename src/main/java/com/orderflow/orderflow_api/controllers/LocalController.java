package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.config.AppConsts;
import com.orderflow.orderflow_api.payload.LocalDTO;
import com.orderflow.orderflow_api.payload.LocalResponse;
import com.orderflow.orderflow_api.services.LocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class LocalController {

    @Autowired
    private LocalService localService;

    @GetMapping("/public/user/locals")
    public ResponseEntity<LocalResponse> findAllUserLocals(
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false)  Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConsts.SORT_LOCALS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConsts.SORT_DIRECTION, required = false) String sortOrder
    ) {
        LocalResponse localResponse = localService.findAllUserLocals( pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(localResponse, HttpStatus.OK);
    }

    @GetMapping("/admin/locals")
    public ResponseEntity<LocalResponse> findAllLocals(
            @RequestParam(name = "pageNumber", defaultValue = AppConsts.PAGE_NUM, required = false)  Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConsts.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConsts.SORT_LOCALS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConsts.SORT_DIRECTION, required = false) String sortOrder
    ){
        LocalResponse localResponse = localService.findAllLocals(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(localResponse, HttpStatus.OK);
    }

    @PostMapping("/public/locals")
    public ResponseEntity<LocalDTO> createLocal(@RequestBody LocalDTO localDTO) {
        LocalDTO savedLocalDTO = localService.registerLocal(localDTO);
        return new ResponseEntity<>(savedLocalDTO, HttpStatus.CREATED);
    }

}
