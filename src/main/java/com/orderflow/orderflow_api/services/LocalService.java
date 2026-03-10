package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.payload.LocalDTO;
import com.orderflow.orderflow_api.payload.LocalResponse;

public interface LocalService{

    LocalResponse findAllUserLocals(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    LocalResponse findAllLocals(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    LocalDTO registerLocal(LocalDTO localDTO);

    LocalDTO findLocalById(Long localId);

    LocalResponse findLocalsByCity(String city, Integer pageNumber, Integer pageSize,  String sortBy, String sortOrder);

    LocalResponse findLocalsByState(String state, Integer pageNumber, Integer pageSize,  String sortBy, String sortOrder);

    LocalResponse findLocalsByPostalCode(String postalCode, Integer pageNumber, Integer pageSize,  String sortBy, String sortOrder);

    LocalDTO updateLocal(Long localId, LocalDTO localDTO);
}
