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

import java.util.ArrayList;
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
    public InventoryResponse moveSupplyOutInventory(int quantity, InventorySupplyDTO inventorySupplyDTO, Integer pageSize, Integer pageNumber) {
        Supply supplyFromDB = supplyRepository.findBySupplyReference(inventorySupplyDTO.getSupplyReference());

        if(supplyFromDB==null){
            throw new APIException("Error: Supply reference not found, supply not registered or refence number wrong");
        }

        List<InventorySupplyDTO> inventorySupplyDTOListFromDB = inventorySupplyRepository.findAllBySupplyReference(inventorySupplyDTO.getSupplyReference())
                .stream().map(inventory -> {
                    return modelMapper.map(inventory, InventorySupplyDTO.class);
                }).toList();

        if(quantity>inventorySupplyDTOListFromDB.size())
            throw new APIException("Error: Quantity exceed stock amount for supply reference "
                    +  supplyFromDB.getSupplyReference() + " and name " + supplyFromDB.getSupplyName());
        else if(quantity<0)
            throw new APIException("Error: Quantity must be positive");

        Comparator<InventorySupplyDTO> comparator = Comparator.comparing(
                inventory -> (Comparable) getField(inventory, "valDate"));

        List<InventorySupplyDTO> listOrdered = inventorySupplyDTOListFromDB.stream().sorted(comparator).toList();

        for(InventorySupplyDTO inventorySupplyDTOFromDB : inventorySupplyDTOListFromDB){
            inventorySupplyDTOFromDB.setStatus("STOCK_OUT");
            inventorySupplyRepository.save(modelMapper.map(inventorySupplyDTOFromDB, InventorySupply.class));
        }

        Page<InventorySupplyDTO> pages = pageCreation(listOrdered, pageNumber, pageSize, AppConsts.SORT_INVENTORIES_BY, "asc");

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
        List<InventorySupplyDTO> subList = listOrdered.subList(start, end);

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
