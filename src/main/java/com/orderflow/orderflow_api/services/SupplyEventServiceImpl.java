package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.Supply;
import com.orderflow.orderflow_api.models.SupplyEvent;
import com.orderflow.orderflow_api.payload.SupplyEventRequestDTO;
import com.orderflow.orderflow_api.payload.SupplyEventResponseDTO;
import com.orderflow.orderflow_api.repositories.SupplyEventRepository;
import com.orderflow.orderflow_api.repositories.SupplyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplyEventServiceImpl implements SupplyEventService {
    @Autowired
    private SupplyEventRepository supplyEventRepository;

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    ModelMapper modelMapper;

    public SupplyEventResponseDTO firstSupplyEvent(Long supplyId) {
        Supply supplyFromDb = supplyRepository.findById(supplyId)
                .orElseThrow(()-> new ResourceNotFoundException("Supply", "supplyId", supplyId));

        SupplyEventRequestDTO supplyEventRequestDTO = new SupplyEventRequestDTO();
        supplyEventRequestDTO.setSupplyId(supplyFromDb.getSupplyId());
        supplyEventRequestDTO.setEventType("IN");
        supplyEventRequestDTO.setQuantityMoved(0);

        SupplyEvent supplyEvent = modelMapper.map(supplyEventRequestDTO, SupplyEvent.class);
        supplyEventRepository.save(supplyEvent);

        SupplyEventResponseDTO supplyEventResponseDTO = modelMapper.map(supplyEvent, SupplyEventResponseDTO.class);
        return supplyEventResponseDTO;
    }

}
