package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.models.InventorySupply;
import com.orderflow.orderflow_api.models.Supply;
import com.orderflow.orderflow_api.payload.InventoryResponse;
import com.orderflow.orderflow_api.payload.InventorySupplyDTO;
import com.orderflow.orderflow_api.repositories.InventorySupplyRepository;
import com.orderflow.orderflow_api.repositories.SupplyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventorySupplyServiceImpl implements InventorySupplyService {

    @Autowired
    private InventorySupplyRepository inventorySupplyRepository;

    @Autowired
    private SupplyRepository supplyRepository;

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
        inventorySupply.setQuantity(0);

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


}
