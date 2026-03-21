package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
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

import java.time.Instant;
import java.util.List;

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

    public SupplyEventResponseDTO increaseQuantityMovedEvent(Long supplyId, Integer quantityMoved) {
        Supply supplyFromDb = supplyRepository.findById(supplyId)
                .orElseThrow(()-> new ResourceNotFoundException("Supply", "supplyId", supplyId));

        SupplyEventRequestDTO supplyEventRequestDTO = new SupplyEventRequestDTO();
        supplyEventRequestDTO.setSupplyId(supplyFromDb.getSupplyId());
        supplyEventRequestDTO.setEventType("IN");
        supplyEventRequestDTO.setQuantityMoved(quantityMoved);

        SupplyEvent supplyEvent = modelMapper.map(supplyEventRequestDTO, SupplyEvent.class);
        supplyEventRepository.save(supplyEvent);

        SupplyEventResponseDTO supplyEventResponseDTO = modelMapper.map(supplyEvent, SupplyEventResponseDTO.class);
        return supplyEventResponseDTO;
    }

    public SupplyEventResponseDTO decreaseQuantityMovedEvent(Long supplyId, Integer quantityMoved) {
        Supply supplyFromDb = supplyRepository.findById(supplyId)
                .orElseThrow(()-> new ResourceNotFoundException("Supply", "supplyId", supplyId));

        SupplyEventRequestDTO supplyEventRequestDTO = new SupplyEventRequestDTO();
        supplyEventRequestDTO.setSupplyId(supplyFromDb.getSupplyId());
        supplyEventRequestDTO.setEventType("OUT");
        supplyEventRequestDTO.setQuantityMoved(quantityMoved);

        SupplyEvent supplyEvent = modelMapper.map(supplyEventRequestDTO, SupplyEvent.class);
        supplyEventRepository.save(supplyEvent);

        SupplyEventResponseDTO supplyEventResponseDTO = modelMapper.map(supplyEvent, SupplyEventResponseDTO.class);
        return supplyEventResponseDTO;
    }

    @Override
    public List<SupplyEventResponseDTO> getSupplyEventList(Long supplyId) {
        List<SupplyEvent> supplyEventsList = supplyEventRepository.findAllBySupplyId(supplyId);

        List<SupplyEventResponseDTO> response = supplyEventsList.stream()
                .map( supply -> {
                    return modelMapper.map(supply, SupplyEventResponseDTO.class);
                }).toList();

        return response;
    }

    public Integer getActualQuantityMovedEvent(Long supplyId) {
        List<SupplyEvent> supplyEventsList = supplyEventRepository.findAllBySupplyId(supplyId);

        Integer quantityAtDate = 0;

        for (SupplyEvent supplyEvents : supplyEventsList){
            if(supplyEvents.getEventType().equals("IN"))
                quantityAtDate += supplyEvents.getQuantityMoved();
            else if (supplyEvents.getEventType().equals("OUT"))
                quantityAtDate -= supplyEvents.getQuantityMoved();
            else
                throw new APIException("Error: Invalid event type, contact the administrator");
        }

        return quantityAtDate;
    }

    @Override
    public String createDashboardTimeSeriesMonthlyItem(Instant firstDate, Instant lastDate, String chartTitleName, String axisLabelName, String valuesLabelName) {
        return "";
    }

}
