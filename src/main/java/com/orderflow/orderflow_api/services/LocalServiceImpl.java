package com.orderflow.orderflow_api.services;

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


}
