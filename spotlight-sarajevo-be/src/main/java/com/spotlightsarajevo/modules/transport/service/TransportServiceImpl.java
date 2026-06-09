package com.spotlightsarajevo.modules.transport.service;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.exceptions.TransportExceptions;
import com.spotlightsarajevo.modules.transport.api.dto.TransportMethodLineModel;
import com.spotlightsarajevo.modules.transport.api.dto.TransportMethodModel;
import com.spotlightsarajevo.modules.transport.api.dto.TransportMethodShorthandModel;
import com.spotlightsarajevo.modules.transport.api.dto.TransportTaxiOperatorModel;
import com.spotlightsarajevo.modules.transport.domain.TransportMethodDAO;
import com.spotlightsarajevo.modules.transport.domain.TransportMethodLineDAO;
import com.spotlightsarajevo.modules.transport.domain.TransportTaxiOperatorDAO;
import com.spotlightsarajevo.modules.transport.domain.entity.TransportMethodEntity;
import com.spotlightsarajevo.modules.transport.domain.entity.TransportMethodLineEntity;
import com.spotlightsarajevo.modules.transport.domain.entity.TransportTaxiOperatorEntity;
import com.spotlightsarajevo.modules.transport.mapper.TransportMapper;
import com.spotlightsarajevo.modules.transport.utils.TransportUtilities;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransportServiceImpl implements TransportService {
    TransportMethodDAO transportMethodDAO;
    TransportMethodLineDAO transportMethodLineDAO;
    TransportTaxiOperatorDAO taxiOperatorDAO;
    TransportMapper transportMapper;
    TransportUtilities transportUtilities;

    @Override
    public ResponseEntity<List<TransportMethodShorthandModel>> findAllTransportMethods() {
        List<TransportMethodEntity> entities = transportMethodDAO.findAll();
        List<TransportMethodShorthandModel> models = transportMapper.entitiesToShorthandDtos(entities);

        return ResponseEntity.status(200).body(models);
    }

    @Override
    public ResponseEntity<TransportMethodModel> findTransportMethodById(Integer id) {
        if (id == null) {
            throw new TransportExceptions.TransportBadRequestException(
                    ExceptionCodes.TransportExceptionCodes.TRANSPORT_INVALID_REQUEST
            );
        }

        Optional<TransportMethodEntity> entityOptional = transportMethodDAO.findById(id);
        TransportMethodEntity entity = entityOptional.orElseThrow(
                () -> new TransportExceptions.TransportNotFoundException(
                        ExceptionCodes.TransportExceptionCodes.TRANSPORT_METHOD_NOT_FOUND
                )
        );

        TransportMethodModel model = transportMapper.entityToDto(entity);
        model.setGeometryGeoJson(transportUtilities.convertGeometryToGeoJson(entity.getGeometry()));

        return ResponseEntity.status(200).body(model);
    }

    @Override
    public ResponseEntity<List<TransportMethodLineModel>> findLinesByOperatorAndTransportType(Integer operatorId, Integer transportTypeId) {
        if (operatorId == null || transportTypeId == null) {
            throw new TransportExceptions.TransportBadRequestException(
                    ExceptionCodes.TransportExceptionCodes.TRANSPORT_INVALID_REQUEST
            );
        }

        List<TransportMethodLineEntity> entities = transportMethodLineDAO.findByOperatorIdAndTransportTypeId(operatorId, transportTypeId);

        List<TransportMethodLineModel> models = new ArrayList<>();
        for (TransportMethodLineEntity entity : entities) {
            TransportMethodLineModel model = transportMapper.lineEntityToDto(entity);
            model.setGeometryGeoJson(transportUtilities.convertGeometryToGeoJson(entity.getGeometry()));
            models.add(model);
        }

        return ResponseEntity.status(200).body(models);
    }

    @Override
    public ResponseEntity<List<TransportTaxiOperatorModel>> findTaxiOperators() {
        List<TransportTaxiOperatorEntity> entities = taxiOperatorDAO.findAll();
        List<TransportTaxiOperatorModel> models = transportMapper.taxiEntitiesToDtos(entities);

        return ResponseEntity.status(200).body(models);
    }
}
