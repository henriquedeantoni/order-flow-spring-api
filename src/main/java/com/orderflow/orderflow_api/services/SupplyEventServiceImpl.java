package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.graphicEngine.charts.ChartEngine;
import com.orderflow.orderflow_api.models.Supply;
import com.orderflow.orderflow_api.models.SupplyEvent;
import com.orderflow.orderflow_api.payload.ItemTimeDTO;
import com.orderflow.orderflow_api.payload.SupplyEventRequestDTO;
import com.orderflow.orderflow_api.payload.SupplyEventResponseDTO;
import com.orderflow.orderflow_api.repositories.SupplyEventRepository;
import com.orderflow.orderflow_api.repositories.SupplyRepository;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.Rectangle2D;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
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
    public String createDashboardTimeSeriesMonthlySupply(
            OffsetDateTime firstDate,
            OffsetDateTime lastDate,
            String chartTitleName,
            String axisLabelName,
            String valuesLabelName) {
        if(!firstDate.isBefore(lastDate))
        {
            throw new APIException("Error: First Date must starts before Last Date");
        }

        long months = ChronoUnit.MONTHS.between(firstDate, lastDate);
        if(months > 1) {
            throw  new APIException("The time duration must be less or equal 1 month");
        }

        List<SupplyEvent> supplyEventsList = supplyEventRepository.findByEventDateGreaterThanEqualAndEventDateLessThanEqual(firstDate, lastDate);

        List<SupplyEventResponseDTO> supplyEventResponseDTOS = supplyEventsList
                .stream().map(supply ->{
                    return modelMapper.map(supply, SupplyEventResponseDTO.class);
                }).toList();

        JFreeChart chart = ChartEngine.createTimeSeriesChartSvg(supplyEventResponseDTOS, SupplyEventResponseDTO::getEventDate, chartTitleName, axisLabelName, valuesLabelName, "month" );

        ChartPanel chartPanel = new ChartPanel(chart);

        SVGGraphics2D svg = new SVGGraphics2D(800, 600);

        Rectangle2D area = new Rectangle2D.Double(0, 0, 800, 600);

        chart.draw(svg, area);

        return svg.getSVGElement();
    }

    @Override
    public String createDashboardTimeSeriesYearlySupply(
            OffsetDateTime firstDate,
            OffsetDateTime lastDate,
            String chartTitleName,
            String axisLabelName,
            String valuesLabelName) {
        if(firstDate.isBefore(lastDate))
        {
            throw new APIException("Error: First Date must starts before Last Date");
        }

        Duration duration = Duration.between(firstDate, lastDate);
        if(duration.toDays() > 365) {
            throw  new APIException("The time duration must be less or equal 1 year");
        }

        List<SupplyEvent> supplyEventsList = supplyEventRepository.findByEventDateGreaterThanEqualAndEventDateLessThanEqual(firstDate, lastDate);

        List<SupplyEventResponseDTO> supplyEventResponseDTOS = supplyEventsList
                .stream().map(supply ->{
                    return modelMapper.map(supply, SupplyEventResponseDTO.class);
                }).toList();

        JFreeChart chart = ChartEngine.createTimeSeriesChartSvg(supplyEventResponseDTOS, SupplyEventResponseDTO::getEventDate, chartTitleName, axisLabelName, valuesLabelName, "year" );

        ChartPanel chartPanel = new ChartPanel(chart);

        SVGGraphics2D svg = new SVGGraphics2D(800, 600);
        chart.draw(svg, svg.getClipBounds());

        return svg.getSVGElement();
    }

}
