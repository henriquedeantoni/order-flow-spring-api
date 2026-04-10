package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.graphicEngine.charts.ChartEngine;
import com.orderflow.orderflow_api.models.Supply;
import com.orderflow.orderflow_api.models.SupplyEvent;
import com.orderflow.orderflow_api.payload.SupplyEventRequestDTO;
import com.orderflow.orderflow_api.payload.SupplyEventResponseDTO;
import com.orderflow.orderflow_api.repositories.SupplyEventRepository;
import com.orderflow.orderflow_api.repositories.SupplyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        supplyEventRequestDTO.setEventType("STOCK_IN");
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
        supplyEventRequestDTO.setEventType("STOCK_IN");
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
        supplyEventRequestDTO.setEventType("STOCK_OUT");
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
            if(supplyEvents.getEventType().equals("STOCK_IN"))
                quantityAtDate += supplyEvents.getQuantityMoved();
            else if (supplyEvents.getEventType().equals("STOCK_OUT"))
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
            String valuesLabelName,
            Long supplyId) {
        if(!firstDate.isBefore(lastDate))
        {
            throw new APIException("Error: First Date must starts before Last Date");
        }

        long months = ChronoUnit.MONTHS.between(firstDate, lastDate);
        if(months > 1) {
            throw  new APIException("The time duration must be less or equal 1 month");
        }

        List<SupplyEvent> supplyEventsList = supplyEventRepository.findByEventDateGreaterThanEqualAndEventDateLessThanEqual(firstDate, lastDate);

        List<SupplyEventResponseDTO> supplyEventResponseDTOS = supplyEventsList.stream()
                .filter(supply -> supply.getSupplyId().equals(supplyId))
                .map(supply -> modelMapper.map(supply, SupplyEventResponseDTO.class))
                .toList();

        Map<OffsetDateTime, Integer> timeSeriesProgression = getTimeSeriesMap(supplyEventResponseDTOS);

        /*
        JFreeChart chart = ChartEngine.createTimeSeriesChartSvg(supplyEventResponseDTOS, SupplyEventResponseDTO::getEventDate, chartTitleName, axisLabelName, valuesLabelName, "month" );

        ChartPanel chartPanel = new ChartPanel(chart);

        SVGGraphics2D svg = new SVGGraphics2D(800, 600);

        Rectangle2D area = new Rectangle2D.Double(0, 0, 800, 600);*/

        String svgElement = ChartEngine.createTimeSeriesProgressionChartSvg(
                timeSeriesProgression,
                chartTitleName,
                axisLabelName,
                valuesLabelName,
                "month");

        return svgElement;
    }

    @Override
    public String createDashboardTimeSeriesYearlySupply(
            OffsetDateTime firstDate,
            OffsetDateTime lastDate,
            String chartTitleName,
            String axisLabelName,
            String valuesLabelName,
            Long supplyId) {
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

        Map<OffsetDateTime, Integer> timeSeriesProgression = getTimeSeriesMap(supplyEventResponseDTOS);

        /*
        JFreeChart chart = ChartEngine.createTimeSeriesChartSvg(supplyEventResponseDTOS, SupplyEventResponseDTO::getEventDate, chartTitleName, axisLabelName, valuesLabelName, "year" );

        ChartStyle.applyTimeSeriesStyleToChart(chart);

        ChartPanel chartPanel = new ChartPanel(chart);

        SVGGraphics2D svg = new SVGGraphics2D(800, 600);
        chart.draw(svg, svg.getClipBounds());
        */

        String svgElement = ChartEngine.createTimeSeriesChartSvgString(
                supplyEventResponseDTOS,
                SupplyEventResponseDTO::getEventDate,
                chartTitleName,
                axisLabelName,
                valuesLabelName,
                "year"
        );

        return svgElement;
    }

    private Map<OffsetDateTime, Integer> getTimeSeriesMap(List<SupplyEventResponseDTO> supplyEventResponseDTOS){

        Map<OffsetDateTime, Integer> finalMapProgressQuantity = new HashMap<>();
        Map<OffsetDateTime, SupplyEventResponseDTO> map = new HashMap<>();

        for(SupplyEventResponseDTO responseDTO : supplyEventResponseDTOS){
            map.put(responseDTO.getEventDate(), responseDTO);
        }

        LinkedHashMap<OffsetDateTime, SupplyEventResponseDTO> mapOrdered = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1, LinkedHashMap::new
                ));

        int quantityMoved = 0;
        for(Map.Entry<OffsetDateTime, SupplyEventResponseDTO> entry : mapOrdered.entrySet()){
            if(entry.getValue().getEventType().equalsIgnoreCase("STOCK_IN")){
                quantityMoved+=entry.getValue().getQuantityMoved();
            } else if(entry.getValue().getEventType().equalsIgnoreCase("STOCK_OUT")){
                quantityMoved-=entry.getValue().getQuantityMoved();
            }
            finalMapProgressQuantity.put(entry.getKey(), quantityMoved);
        }

        return finalMapProgressQuantity;
    }

}
