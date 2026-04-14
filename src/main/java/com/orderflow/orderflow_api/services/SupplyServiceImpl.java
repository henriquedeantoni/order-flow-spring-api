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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Specification<Supply> specification = Specification.allOf(List.of());
        if(keyword != null && !keyword.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("supplyName")),
                            "%" + keyword.toLowerCase() + "%"));
        }

        Page<Supply> pageSupplies = supplyRepository.findAll(specification, pageDetails);

        List<Supply> supplies = pageSupplies.getContent();
        List<SupplyDTO> suppliesDTOS = supplies
                .stream()
                .map(supply -> {
                    return modelMapper.map(supply, SupplyDTO.class);
                }).toList();

        SupplyResponse supplyResponse = new SupplyResponse();
        supplyResponse.setContent(suppliesDTOS);
        supplyResponse.setPageNumber(pageSupplies.getNumber());
        supplyResponse.setPageSize(pageSupplies.getSize());
        supplyResponse.setTotalElements(pageSupplies.getTotalElements());
        supplyResponse.setTotalPages(pageSupplies.getTotalPages());
        return supplyResponse;
    }
}
