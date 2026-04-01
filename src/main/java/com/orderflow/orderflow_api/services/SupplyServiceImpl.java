package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.InventorySupply;
import com.orderflow.orderflow_api.models.Supply;
import com.orderflow.orderflow_api.payload.SupplyDTO;
import com.orderflow.orderflow_api.payload.SupplyResponse;
import com.orderflow.orderflow_api.repositories.InventorySupplyRepository;
import com.orderflow.orderflow_api.repositories.SupplyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplyServiceImpl implements SupplyService {

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private InventorySupplyRepository inventorySupplyRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SupplyDTO registerSupply(SupplyDTO supplyDTO) {
        Supply supplyFromDb = supplyRepository.findBySupplyReference(supplyDTO.getSupplyReference());
        if (supplyFromDb != null) {
            throw new RuntimeException("Supply already exists, with reference specified " + supplyDTO.getSupplyReference());
        }

        Supply supply = modelMapper.map(supplyDTO, Supply.class);
        //System.out.println("supply : " + supply);

        supplyRepository.save(supply);

        return modelMapper.map(supply, SupplyDTO.class);
    }

    @Override
    public SupplyDTO updateSupply(Long supplyId, SupplyDTO supplyDTO) {
        Supply supplyFromDb = supplyRepository.findById(supplyId)
                .orElseThrow(() -> new ResourceNotFoundException("Supply", "supplyId", supplyId));

        Supply supplyByReference = supplyRepository.findBySupplyReference(supplyDTO.getSupplyReference());
        if (supplyByReference != null) {
            throw new RuntimeException("Supply already exists, with reference specified " + supplyDTO.getSupplyReference());
        }

        supplyFromDb.setSupplyReference(supplyDTO.getSupplyReference());
        supplyFromDb.setSupplyCode(supplyDTO.getSupplyCode());
        supplyFromDb.setBrandName(supplyDTO.getBrandName());
        supplyFromDb.setSupplyName(supplyDTO.getSupplyName());
        supplyFromDb.setSupplyDescription(supplyDTO.getSupplyDescription());
        supplyFromDb.setSupplyUnit(supplyDTO.getSupplyUnit());

        supplyRepository.save(supplyFromDb);
        return modelMapper.map(supplyFromDb, SupplyDTO.class);
    }

    @Override
    public SupplyResponse getAllSupplyRegistered(String keyword, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder) {
        return null;
    }
}
