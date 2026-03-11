package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.exceptions.APIException;
import com.orderflow.orderflow_api.exceptions.ResourceNotFoundException;
import com.orderflow.orderflow_api.models.Local;
import com.orderflow.orderflow_api.models.User;
import com.orderflow.orderflow_api.payload.LocalDTO;
import com.orderflow.orderflow_api.payload.LocalResponse;
import com.orderflow.orderflow_api.repositories.LocalRepository;
import com.orderflow.orderflow_api.security.util.AuthUtil;
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
public class LocalServiceImpl implements LocalService{

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public LocalResponse findAllUserLocals(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        User user = authUtil.userOnLoggedSession();

        Page<Local> pageLocals = localRepository.findByUser(user, pageDetails);

        List<Local> locals = pageLocals.getContent();

        List<LocalDTO> localDTOS = locals
                .stream()
                .map(local -> {
                    LocalDTO localDTO = modelMapper.map(local, LocalDTO.class);
                    return localDTO;
                }).toList();

        LocalResponse localResponse = new LocalResponse();
        localResponse.setContent(localDTOS);
        localResponse.setTotalElements(pageLocals.getTotalElements());
        localResponse.setTotalPages(pageLocals.getTotalPages());
        localResponse.setPageNumber(pageLocals.getNumber());
        localResponse.setPageSize(pageLocals.getSize());
        localResponse.setLastPage(pageLocals.isLast());
        return localResponse;
    }

    @Override
    public LocalResponse findAllLocals(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Local> pageLocals = localRepository.findAll(pageDetails);

        List<Local> locals = pageLocals.getContent();

        List<LocalDTO> localDTOS = locals
                .stream()
                .map(local -> {
                    LocalDTO localDTO = modelMapper.map(local, LocalDTO.class);
                    return localDTO;
                }).toList();

        LocalResponse localResponse = new LocalResponse();
        localResponse.setContent(localDTOS);
        localResponse.setTotalElements(pageLocals.getTotalElements());
        localResponse.setTotalPages(pageLocals.getTotalPages());
        localResponse.setPageNumber(pageLocals.getNumber());
        localResponse.setPageSize(pageLocals.getSize());
        localResponse.setLastPage(pageLocals.isLast());
        return localResponse;
    }

    @Override
    public LocalDTO registerLocal(LocalDTO localDTO) {
        Local local = modelMapper.map(localDTO, Local.class);

        User user = authUtil.userOnLoggedSession();

        local.setUser(user);

        Local savedLocal = localRepository.save(local);

        return modelMapper.map(savedLocal, LocalDTO.class);
    }

    @Override
    public LocalDTO findLocalById(Long localId) {
        Local localFromDB = localRepository.findById(localId)
                .orElseThrow(() -> new ResourceNotFoundException("Local", "localId", localId));
        return modelMapper.map(localFromDB, LocalDTO.class);
    }

    @Override
    public LocalResponse findLocalsByCity(String city, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Local> pageLocals = localRepository.findAllByCity(city, pageDetails);

        List<Local> locals = pageLocals.getContent();

        List<LocalDTO> localDTOs = locals.stream()
                .map(local -> {
                    LocalDTO localDTO = modelMapper.map(local, LocalDTO.class);
                    return localDTO;
                }).toList();

        if(localDTOs.isEmpty()){
            throw new APIException("Any local with this city was not found");
        }

        LocalResponse localResponse = new LocalResponse();
        localResponse.setContent(localDTOs);
        localResponse.setTotalElements(pageLocals.getTotalElements());
        localResponse.setTotalPages(pageLocals.getTotalPages());
        localResponse.setPageNumber(pageLocals.getNumber());
        localResponse.setPageSize(pageLocals.getSize());
        localResponse.setLastPage(pageLocals.isLast());
        return localResponse;
    }

    @Override
    public LocalResponse findLocalsByState(String state, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Local> pageLocals = localRepository.findAllByState(state, pageDetails);

        List<Local> locals = pageLocals.getContent();

        List<LocalDTO> localDTOs = locals.stream()
                .map(local -> {
                    LocalDTO localDTO = modelMapper.map(local, LocalDTO.class);
                    return localDTO;
                }).toList();

        if(localDTOs.isEmpty()){
            throw new APIException("Any local with this state was not found");
        }

        LocalResponse localResponse = new LocalResponse();
        localResponse.setContent(localDTOs);
        localResponse.setTotalElements(pageLocals.getTotalElements());
        localResponse.setTotalPages(pageLocals.getTotalPages());
        localResponse.setPageNumber(pageLocals.getNumber());
        localResponse.setPageSize(pageLocals.getSize());
        localResponse.setLastPage(pageLocals.isLast());
        return localResponse;
    }

    @Override
    public LocalResponse findLocalsByPostalCode(String postalCode, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Specification<Local> specification = Specification.allOf(List.of());

        if(postalCode != null && !postalCode.isEmpty()){
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("postalCode")), "%" + postalCode.toLowerCase() + "%"));
        }

        Page<Local> pageLocals = localRepository.findAll(specification, pageDetails);

        List<Local> locals = pageLocals.getContent();

        List<LocalDTO> localDTOs = locals.stream()
                .map(local -> {
                    LocalDTO localDTO = modelMapper.map(local, LocalDTO.class);
                    return localDTO;
                }).toList();

        if(localDTOs.isEmpty()){
            throw new APIException("Any local with this postal code segment was not found");
        }

        LocalResponse localResponse = new LocalResponse();
        localResponse.setContent(localDTOs);
        localResponse.setTotalElements(pageLocals.getTotalElements());
        localResponse.setTotalPages(pageLocals.getTotalPages());
        localResponse.setPageNumber(pageLocals.getNumber());
        localResponse.setPageSize(pageLocals.getSize());
        localResponse.setLastPage(pageLocals.isLast());
        return localResponse;
    }

    @Override
    public LocalDTO updateLocal(Long localId, LocalDTO localDTO) {
        Local localFromDB = localRepository.findById(localId)
                .orElseThrow(() -> new ResourceNotFoundException("Local", "localId", localId));

        localFromDB.setCity(localDTO.getCity());
        localFromDB.setState(localDTO.getState());
        localFromDB.setCountry(localDTO.getCountry());
        localFromDB.setBuildingName(localDTO.getBuildingName());
        localFromDB.setPostalCode(localDTO.getPostalCode());
        localFromDB.setStreetName(localDTO.getStreetName());
        Local savedLocal = localRepository.save(localFromDB);

        return modelMapper.map(savedLocal, LocalDTO.class);
    }

    @Override
    public LocalDTO deleteLocal(Long localId) {
        Local localFromDB = localRepository.findById(localId)
                .orElseThrow(() -> new ResourceNotFoundException("Local", "localId", localId));

        LocalDTO localDTO = modelMapper.map(localFromDB, LocalDTO.class);
        localRepository.delete(localFromDB);
        return localDTO;
    }

    @Override
    public String createDashboardLocalByState(String state, String country, Integer qtyLayers) {
        List<Local> localsFromDB = localRepository.findAllByStateAndByCountry(state, country);



        return "";
    }
}
