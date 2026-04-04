package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.config.AppConsts;
import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.models.InventorySupply;
import com.orderflow.orderflow_api.models.Supply;
import com.orderflow.orderflow_api.payload.InventoryResponse;
import com.orderflow.orderflow_api.payload.InventorySupplyDTO;
import com.orderflow.orderflow_api.repositories.InventorySupplyRepository;
import com.orderflow.orderflow_api.repositories.SupplyRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class InventorySupplyServiceImpl implements InventorySupplyService {

    @Autowired
    private InventorySupplyRepository inventorySupplyRepository;


    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private SupplyEventService supplyEventService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public InventorySupplyDTO registerSupplyOnInventory(InventorySupplyDTO inventorySupplyDTO) {
        Supply supplyFromDB = supplyRepository.findBySupplyReference(inventorySupplyDTO.getSupplyReference());
        if(supplyFromDB==null){
            throw new APIException("Error: Supply reference not found");
        }

        InventorySupply inventorySupply = new InventorySupply();
        inventorySupply.setSupply(supplyFromDB);
        inventorySupply.setSection(inventorySupplyDTO.getSection());
        inventorySupply.setCodeBar(inventorySupplyDTO.getCodeBar());

        inventorySupplyRepository.save(inventorySupply);

        return inventorySupplyDTO;
    }

    @Override
    public InventoryResponse getAllInventoryItems(Integer pageSize, Integer pageNumber, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Specification<InventorySupply> specification = Specification.allOf(List.of());

        Page<InventorySupply> inventorySupplyPage = inventorySupplyRepository.findAll(specification, pageable);

        List<InventorySupply> inventoryList = inventorySupplyPage.getContent();
        List<InventorySupplyDTO> inventorySupplyDTOList = inventoryList
                .stream().map(
                        item -> {
                            InventorySupplyDTO inventorySupplyDTO = modelMapper.map(item, InventorySupplyDTO.class);
                            return inventorySupplyDTO;
                        }
                ).toList();

        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setContent(inventorySupplyDTOList);
        inventoryResponse.setPageNumber(pageNumber);
        inventoryResponse.setPageSize(pageSize);
        inventoryResponse.setTotalPages(inventorySupplyPage.getTotalPages());
        inventoryResponse.setTotalElements(inventorySupplyPage.getTotalElements());

        return inventoryResponse;
    }

    @Override
    @Transactional
    public InventoryResponse moveSupplyOnInventory(int quantity, InventorySupplyDTO inventorySupplyDTO, int pageSize, int pageNumber) {
        Supply supplyFromDB = supplyRepository.findBySupplyReference(inventorySupplyDTO.getSupplyReference());

        if(supplyFromDB==null){
            throw new APIException("Error: Supply reference not found, supply not registered or refence number wrong");
        }

        if(quantity>1000)
            throw new APIException("Error: Quantity exceed 1000");
        else if(quantity<0)
            throw new APIException("Error: Quantity must be positive");

        List<InventorySupplyDTO> inventorySupplyList = new ArrayList();

        for(int i = 0 ; i<quantity ; i++ ){
            InventorySupply inventorySupply = new InventorySupply();
            inventorySupply.setSupply(supplyFromDB);
            inventorySupply.setSection(inventorySupplyDTO.getSection());
            inventorySupply.setCodeBar(inventorySupplyDTO.getCodeBar());
            inventorySupply.setSupplyReference(inventorySupplyDTO.getSupplyReference());
            inventorySupply.setValDate(inventorySupplyDTO.getValDate());
            inventorySupply.setStatus("STOCK_IN");
            inventorySupply.setMovementDate(OffsetDateTime.now(ZoneOffset.UTC));
            inventorySupplyRepository.save(inventorySupply);

            InventorySupplyDTO supplyDTO = modelMapper.map(inventorySupply, InventorySupplyDTO.class);
            inventorySupplyList.add(supplyDTO);
        }

        Page<InventorySupplyDTO> pages = pageCreation(inventorySupplyList, pageNumber, pageSize, AppConsts.SORT_INVENTORIES_BY, "asc");

        List<InventorySupplyDTO> itemsPage = pages.getContent();

        if(itemsPage.size()>0){
            supplyEventService.increaseQuantityMovedEvent(supplyFromDB.getSupplyId(), quantity);
        }

        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setContent(itemsPage);
        inventoryResponse.setPageNumber(pageNumber);
        inventoryResponse.setPageSize(pageSize);
        inventoryResponse.setTotalPages(pages.getTotalPages());
        inventoryResponse.setTotalElements(pages.getTotalElements());
        inventoryResponse.setLastPage(pages.isLast());
        return inventoryResponse;
    }

    @Override
    public InventoryResponse moveSupplyOutInventory(int quantity, String supplyReference, Integer pageSize, Integer pageNumber) {
        Supply supplyFromDB = supplyRepository.findBySupplyReference(supplyReference);

        if(supplyFromDB==null){
            throw new APIException("Error: Supply reference not found, supply not registered or refence number wrong");
        }

        List<InventorySupplyDTO> inventorySupplyDTOListFromDB = inventorySupplyRepository.findAllBySupplyReference(supplyReference)
                .stream().map(inventory -> {
                    return modelMapper.map(inventory, InventorySupplyDTO.class);
                }).toList();

        if(quantity>inventorySupplyDTOListFromDB.size())
            throw new APIException("Error: Quantity exceed stock amount for supply reference "
                    +  supplyFromDB.getSupplyReference() + " and name " + supplyFromDB.getSupplyName());
        else if(quantity<0)
            throw new APIException("Error: Quantity must be positive");

        Comparator<InventorySupplyDTO> comparator = Comparator.comparing(
                inventory -> (Comparable) getField((InventorySupplyDTO) inventory, "valDate")).reversed();

        List<InventorySupplyDTO> listOrdered = inventorySupplyDTOListFromDB.stream().sorted(comparator).toList();

        /*
        for(int j=0; j<quantity; j++){
            InventorySupplyDTO inventorySupplyDTOFromDB = listOrdered.get(j);
            InventorySupply inventorySupply = inventorySupplyRepository.findById(inventorySupplyDTOFromDB.getInventorySupplyId())
                    .orElseThrow(() -> new APIException("Error: Supply Inventory with id " + inventorySupplyDTOFromDB.getInventorySupplyId() + " not found"));
            if(inventorySupply.getStatus().equals("STOCK_IN")){
                inventorySupply.setStatus("STOCK_OUT");
                inventorySupply.setMovementDate(OffsetDateTime.now(ZoneOffset.UTC));
                inventorySupplyRepository.save(inventorySupply);
            }
        }*/

        List<InventorySupplyDTO> listOrderedFiltered = new ArrayList();

        int i=0;
        for(InventorySupplyDTO inventorySupplyDTOFromDB : listOrdered){

            if(i<quantity){
                InventorySupply inventorySupply = inventorySupplyRepository.findById(inventorySupplyDTOFromDB.getInventorySupplyId())
                        .orElseThrow(() -> new APIException("Error: Supply Inventory with id " + inventorySupplyDTOFromDB.getInventorySupplyId() + " not found"));

                System.out.println(inventorySupply);
                if(inventorySupply.getStatus().equals("STOCK_IN")){
                    inventorySupply.setStatus("STOCK_OUT");
                    inventorySupply.setMovementDate(OffsetDateTime.now(ZoneOffset.UTC));
                    inventorySupplyRepository.save(inventorySupply);
                    listOrderedFiltered.add(inventorySupplyDTOFromDB);
                    i+=1;
                }
            }
        }

        Page<InventorySupplyDTO> pages = pageCreation(listOrderedFiltered, pageNumber, pageSize, AppConsts.SORT_INVENTORIES_BY, "asc");

        List<InventorySupplyDTO> itemsPage = pages.getContent();

        if(itemsPage.size()>0){
            supplyEventService.decreaseQuantityMovedEvent(supplyFromDB.getSupplyId(), quantity);
        }

        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setContent(itemsPage);
        inventoryResponse.setPageNumber(pageNumber);
        inventoryResponse.setPageSize(pageSize);
        inventoryResponse.setTotalPages(pages.getTotalPages());
        inventoryResponse.setTotalElements(pages.getTotalElements());
        inventoryResponse.setLastPage(pages.isLast());

        return inventoryResponse;
    }

    @Override
    public InventoryResponse movementsSupplyOnPeriod(OffsetDateTime firstDate, OffsetDateTime lastDate, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder) {

        if(!firstDate.equals(lastDate) && firstDate.isAfter(lastDate)){
            throw new APIException("Error: Last date must be before first date");
        }

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<InventorySupply> pageItems = inventorySupplyRepository.findByMovementDateGreaterThanEqualAndMovementDateLessThanEqual(firstDate, lastDate, pageDetails);

        List<InventorySupply> inventoriesFromContent = pageItems.getContent();

        if(inventoriesFromContent.isEmpty()){
            throw new APIException("Error: Inventories are empty");
        }

        List<InventorySupplyDTO> inventorySupplyDTOS = inventoriesFromContent.stream()
                .map(inventorySupply ->  {
                    return modelMapper.map(inventorySupply, InventorySupplyDTO.class);
                }).toList();

        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setContent(inventorySupplyDTOS);
        inventoryResponse.setPageNumber(pageNumber);
        inventoryResponse.setPageSize(pageSize);
        inventoryResponse.setTotalPages(pageItems.getTotalPages());
        inventoryResponse.setTotalElements(pageItems.getTotalElements());
        inventoryResponse.setLastPage(pageItems.isLast());

        return  inventoryResponse;
    }

    @Override
    public Integer getTotalQuantityFromSupply(long supplyId) {
        Supply supply = supplyRepository.findById(supplyId)
                .orElseThrow(() -> new APIException("Error: Supply with id " + supplyId + " not found"));

        String supplyReference = supply.getSupplyReference();

        List<InventorySupply> inventorySupplyList = inventorySupplyRepository.findAllBySupplyReference(supplyReference);

        if(inventorySupplyList.isEmpty())
            return 0;
        else {
            return inventorySupplyList.stream()
                    .mapToInt(inventorySupply ->
                        inventorySupply.getStatus().equalsIgnoreCase("STOCK_IN") ? 1 : 0
                    ).sum();
        }
    }

    @Override
    public Integer getTotalQuantityFromSupplyByPeriod(long supplyId, OffsetDateTime firstDate, OffsetDateTime lastDate) {
        Supply supply = supplyRepository.findById(supplyId)
                .orElseThrow(() -> new APIException("Error: Supply with id " + supplyId + " not found"));

        if(supply == null){
            throw new APIException("Error: Supply with id " + supplyId + " not found");
        }

        if(firstDate.isAfter(lastDate)){
            throw new APIException("Error: First date must be before last date");
        }

        List<InventorySupply> inventorySupplies = inventorySupplyRepository.findByMovementDateGreaterThanEqualAndMovementDateLessThanEqual(firstDate, lastDate);

        if(inventorySupplies.isEmpty())
            return 0;
        else {
            return inventorySupplies.stream()
                    .mapToInt(inventorySupply ->
                            inventorySupply.getStatus().equalsIgnoreCase("STOCK_IN") ? 1 : 0
                    ).sum();
        }
    }

    @Override
    public InventoryResponse getSuppliesExpirationWeek(Integer pageSize, Integer pageNumber, String sortBy, String sortOrder) {

        OffsetDateTime lastDate = OffsetDateTime.from(LocalDate.now());
        OffsetDateTime firstDate = OffsetDateTime.from(LocalDate.now().plusWeeks(1));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<InventorySupply> pageItems = inventorySupplyRepository.findByMovementDateGreaterThanEqualAndMovementDateLessThanEqual(firstDate, lastDate, pageDetails);


        List<InventorySupply> inventoriesFromContent = pageItems.getContent();

        if(inventoriesFromContent.isEmpty()){
            throw new APIException("Error: Inventories are empty");
        }

        List<InventorySupplyDTO> inventorySupplyDTOS = inventoriesFromContent.stream()
                .map(inventorySupply ->  {
                    return modelMapper.map(inventorySupply, InventorySupplyDTO.class);
                }).toList();

        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setContent(inventorySupplyDTOS);
        inventoryResponse.setPageNumber(pageNumber);
        inventoryResponse.setPageSize(pageSize);
        inventoryResponse.setTotalPages(pageItems.getTotalPages());
        inventoryResponse.setTotalElements(pageItems.getTotalElements());
        inventoryResponse.setLastPage(pageItems.isLast());

        return  inventoryResponse;
    }

    @Override
    public InventoryResponse getSuppliesExpirationMonth(Integer pageSize, Integer pageNumber, String sortBy, String sortOrder) {

        OffsetDateTime lastDate = OffsetDateTime.from(LocalDate.now());
        OffsetDateTime firstDate = OffsetDateTime.from(LocalDate.now().plusMonths(1));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<InventorySupply> pageItems = inventorySupplyRepository.findByMovementDateGreaterThanEqualAndMovementDateLessThanEqual(firstDate, lastDate, pageDetails);

        List<InventorySupply> inventoriesFromContent = pageItems.getContent();

        if(inventoriesFromContent.isEmpty()){
            throw new APIException("Error: Inventories are empty");
        }

        List<InventorySupplyDTO> inventorySupplyDTOS = inventoriesFromContent.stream()
                .map(inventorySupply ->  {
                    return modelMapper.map(inventorySupply, InventorySupplyDTO.class);
                }).toList();

        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setContent(inventorySupplyDTOS);
        inventoryResponse.setPageNumber(pageNumber);
        inventoryResponse.setPageSize(pageSize);
        inventoryResponse.setTotalPages(pageItems.getTotalPages());
        inventoryResponse.setTotalElements(pageItems.getTotalElements());
        inventoryResponse.setLastPage(pageItems.isLast());

        return  inventoryResponse;
    }

    private Page<InventorySupplyDTO> pageCreation(
            List<InventorySupplyDTO> inventorySupplyList,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));

        Comparator<InventorySupplyDTO> comparator = Comparator.comparing(
                inventory -> (Comparable) getField(inventory, sortBy));

        if (sortDirection.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }

        List<InventorySupplyDTO> listOrdered = inventorySupplyList.stream().sorted(comparator).toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), listOrdered.size());
        List<InventorySupplyDTO> subList = new ArrayList<>();

        if(start<end)
            subList = listOrdered.subList(start, end);
        else
            subList = Collections.emptyList();

        return new PageImpl<>(subList, pageable, listOrdered.size());
    }

    private Comparable<?> getField(InventorySupplyDTO inventorySupplyDTO, String sortBy) {
        switch (sortBy) {
            case "inventorySupplyId": return inventorySupplyDTO.getInventorySupplyId();
            case "supplyReference": return inventorySupplyDTO.getSupplyReference();
            case "codeBar": return inventorySupplyDTO.getCodeBar();
            case "section": return inventorySupplyDTO.getSection();
            case "valDate": return inventorySupplyDTO.getValDate();

            default: return inventorySupplyDTO.getSupplyReference();
        }
    }
}
