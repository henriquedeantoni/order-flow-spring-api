package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.Supply;
import com.orderflow.orderflow_api.payload.SupplyDTO;
import com.orderflow.orderflow_api.repositories.SupplyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplyServiceImpl implements SupplyService {

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SupplyDTO registerSupply(SupplyDTO supplyDTO) {
        Supply supplyFromDb = supplyRepository.findBySupplyReference(supplyDTO.getSupplyReference());
        if (supplyFromDb != null) {
            throw new RuntimeException("Supply already exists, with reference specified " + supplyDTO.getSupplyReference());
        }
        supplyRepository.save(supplyFromDb);

        return modelMapper.map(supplyFromDb, SupplyDTO.class);
    }
}
