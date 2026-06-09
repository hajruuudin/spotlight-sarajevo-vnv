package com.spotlightsarajevo.modules.transport.service;

import com.spotlightsarajevo.modules.transport.api.dto.TransportMethodLineModel;
import com.spotlightsarajevo.modules.transport.api.dto.TransportMethodModel;
import com.spotlightsarajevo.modules.transport.api.dto.TransportMethodShorthandModel;
import com.spotlightsarajevo.modules.transport.api.dto.TransportTaxiOperatorModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * TransportService interface defines methods for retrieving transport-related data.
 * It includes methods to find all transport methods, find a transport method by its ID,
 * find transport lines by operator and transport type, and find taxi operators.
 */
public interface TransportService {
    /**
     * Retrieves a list of all transport methods in shorthand format.
     *
     * @return ResponseEntity containing a list of TransportMethodShorthandModel
     */
    ResponseEntity<List<TransportMethodShorthandModel>> findAllTransportMethods();

    /**
     * Retrieves a transport method by its unique identifier.
     *
     * @param id the unique identifier of the transport method
     * @return ResponseEntity containing the TransportMethodModel
     */
    ResponseEntity<TransportMethodModel> findTransportMethodById(Integer id);

    /**
     * Retrieves transport lines based on the operator ID and transport type ID.
     *
     * @param operatorId the unique identifier of the transport operator
     * @param transportTypeId the unique identifier of the transport type
     * @return ResponseEntity containing a list of TransportMethodLineModel
     */
    ResponseEntity<List<TransportMethodLineModel>> findLinesByOperatorAndTransportType(Integer operatorId, Integer transportTypeId);

    /**
     * Retrieves a list of all taxi operators.
     *
     * @return ResponseEntity containing a list of TransportTaxiOperatorModel
     */
    ResponseEntity<List<TransportTaxiOperatorModel>> findTaxiOperators();
}

