package com.spotlightsarajevo.spotlight_sarajevo.unit;

import com.spotlightsarajevo.common.exceptions.TransportExceptions;
import com.spotlightsarajevo.common.utils.CommonFunctions;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
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
import com.spotlightsarajevo.modules.transport.service.TransportServiceImpl;
import com.spotlightsarajevo.modules.transport.utils.TransportUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Geometry;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransportServiceUnitTests {
    @Mock
    TransportMethodDAO transportMethodDAO;
    @Mock
    TransportMethodLineDAO transportMethodLineDAO;
    @Mock
    TransportTaxiOperatorDAO taxiOperatorDAO;
    @Mock
    TransportMapper transportMapper;
    @Mock
    TransportUtilities transportUtilities;
    @Mock
    Principal principal;
    @Mock
    CommonFunctions commonFunctions;
    @InjectMocks
    TransportServiceImpl transportService;

    UserEntity userEntity;

    @BeforeEach
    void init() {
        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setIsAdmin(true);
        when(commonFunctions.getUserFromPrincipal(principal)).thenReturn(userEntity);
    }

    @Test
    void findAllTransportMethods_ShouldReturnAllTransportMethods_WhenNoError() {
        // Arrange
        TransportMethodEntity entity = new TransportMethodEntity();
        entity.setId(1);
        entity.setMethodNameEn("Bus");
        List<TransportMethodEntity> entities = new ArrayList<>();
        entities.add(entity);

        TransportMethodShorthandModel model = new TransportMethodShorthandModel();
        model.setId(1);
        model.setMethodNameEn("Bus");
        List<TransportMethodShorthandModel> models = new ArrayList<>();
        models.add(model);

        when(transportMethodDAO.findAll()).thenReturn(entities);
        when(transportMapper.entitiesToShorthandDtos(entities)).thenReturn(models);

        // Act
        ResponseEntity<List<TransportMethodShorthandModel>> result = transportService.findAllTransportMethods();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(model, result.getBody().get(0));

        verify(transportMethodDAO).findAll();
        verify(transportMapper).entitiesToShorthandDtos(entities);
    }

    @Test
    void findAllTransportMethods_ShouldReturnEmptyList_WhenNoMethodsExist() {
        // Arrange
        List<TransportMethodEntity> entities = new ArrayList<>();
        List<TransportMethodShorthandModel> models = new ArrayList<>();

        when(transportMethodDAO.findAll()).thenReturn(entities);
        when(transportMapper.entitiesToShorthandDtos(entities)).thenReturn(models);

        // Act
        ResponseEntity<List<TransportMethodShorthandModel>> result = transportService.findAllTransportMethods();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().size());

        verify(transportMethodDAO).findAll();
        verify(transportMapper).entitiesToShorthandDtos(entities);
    }

    @Test
    void findTransportMethodById_ShouldReturnTransportMethod_WhenIdNotNull() {
        // Arrange
        Integer methodId = 1;
        TransportMethodEntity entity = new TransportMethodEntity();
        entity.setId(methodId);
        entity.setMethodNameEn("Bus");
        Geometry geometry = null;
        entity.setGeometry(geometry);
        Optional<TransportMethodEntity> optionalEntity = Optional.of(entity);

        TransportMethodModel model = new TransportMethodModel();
        model.setId(methodId);
        model.setMethodNameEn("Bus");

        when(transportMethodDAO.findById(methodId)).thenReturn(optionalEntity);
        when(transportMapper.entityToDto(entity)).thenReturn(model);
        when(transportUtilities.convertGeometryToGeoJson(geometry)).thenReturn("{\"type\":\"Point\"}");

        // Act
        ResponseEntity<TransportMethodModel> result = transportService.findTransportMethodById(methodId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(model, result.getBody());

        verify(transportMethodDAO).findById(methodId);
        verify(transportMapper).entityToDto(entity);
        verify(transportUtilities).convertGeometryToGeoJson(geometry);
    }

    @Test
    void findTransportMethodById_ShouldThrowException_WhenIdNull() {
        // Arrange
        Integer methodId = null;

        // Act & Assert
        assertThrows(TransportExceptions.TransportBadRequestException.class, () -> {
            transportService.findTransportMethodById(methodId);
        });
    }

    @Test
    void findTransportMethodById_ShouldThrowException_WhenMethodNotFound() {
        // Arrange
        Integer methodId = 999;

        when(transportMethodDAO.findById(methodId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TransportExceptions.TransportNotFoundException.class, () -> {
            transportService.findTransportMethodById(methodId);
        });

        verify(transportMethodDAO).findById(methodId);
    }

    @Test
    void findLinesByOperatorAndTransportType_ShouldReturnLines_WhenIdsNotNull() {
        // Arrange
        Integer operatorId = 1;
        Integer transportTypeId = 1;
        TransportMethodLineEntity entity = new TransportMethodLineEntity();
        entity.setId(1);
        entity.setGeometry(null);
        List<TransportMethodLineEntity> entities = new ArrayList<>();
        entities.add(entity);

        TransportMethodLineModel model = new TransportMethodLineModel();
        model.setId(1);

        when(transportMethodLineDAO.findByOperatorIdAndTransportTypeId(operatorId, transportTypeId)).thenReturn(entities);
        when(transportMapper.lineEntityToDto(entity)).thenReturn(model);
        when(transportUtilities.convertGeometryToGeoJson(null)).thenReturn("{\"type\":\"Point\"}");

        // Act
        ResponseEntity<List<TransportMethodLineModel>> result = transportService.findLinesByOperatorAndTransportType(operatorId, transportTypeId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(model, result.getBody().get(0));

        verify(transportMethodLineDAO).findByOperatorIdAndTransportTypeId(operatorId, transportTypeId);
        verify(transportMapper).lineEntityToDto(entity);
        verify(transportUtilities).convertGeometryToGeoJson(null);
    }

    @Test
    void findLinesByOperatorAndTransportType_ShouldReturnEmptyList_WhenNoLinesExist() {
        // Arrange
        Integer operatorId = 1;
        Integer transportTypeId = 1;
        List<TransportMethodLineEntity> entities = new ArrayList<>();

        when(transportMethodLineDAO.findByOperatorIdAndTransportTypeId(operatorId, transportTypeId)).thenReturn(entities);

        // Act
        ResponseEntity<List<TransportMethodLineModel>> result = transportService.findLinesByOperatorAndTransportType(operatorId, transportTypeId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().size());

        verify(transportMethodLineDAO).findByOperatorIdAndTransportTypeId(operatorId, transportTypeId);
    }

    @Test
    void findLinesByOperatorAndTransportType_ShouldThrowException_WhenOperatorIdNull() {
        // Arrange
        Integer operatorId = null;
        Integer transportTypeId = 1;

        // Act & Assert
        assertThrows(TransportExceptions.TransportBadRequestException.class, () -> {
            transportService.findLinesByOperatorAndTransportType(operatorId, transportTypeId);
        });
    }

    @Test
    void findLinesByOperatorAndTransportType_ShouldThrowException_WhenTransportTypeIdNull() {
        // Arrange
        Integer operatorId = 1;
        Integer transportTypeId = null;

        // Act & Assert
        assertThrows(TransportExceptions.TransportBadRequestException.class, () -> {
            transportService.findLinesByOperatorAndTransportType(operatorId, transportTypeId);
        });
    }

    @Test
    void findTaxiOperators_ShouldReturnAllTaxiOperators_WhenNoError() {
        // Arrange
        TransportTaxiOperatorEntity entity = new TransportTaxiOperatorEntity();
        entity.setId(1);
        entity.setCompany("Taxi Company");
        List<TransportTaxiOperatorEntity> entities = new ArrayList<>();
        entities.add(entity);

        TransportTaxiOperatorModel model = new TransportTaxiOperatorModel();
        model.setId(1);
        model.setCompany("Taxi Company");
        List<TransportTaxiOperatorModel> models = new ArrayList<>();
        models.add(model);

        when(taxiOperatorDAO.findAll()).thenReturn(entities);
        when(transportMapper.taxiEntitiesToDtos(entities)).thenReturn(models);

        // Act
        ResponseEntity<List<TransportTaxiOperatorModel>> result = transportService.findTaxiOperators();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(model, result.getBody().get(0));

        verify(taxiOperatorDAO).findAll();
        verify(transportMapper).taxiEntitiesToDtos(entities);
    }

    @Test
    void findTaxiOperators_ShouldReturnEmptyList_WhenNoOperatorsExist() {
        // Arrange
        List<TransportTaxiOperatorEntity> entities = new ArrayList<>();
        List<TransportTaxiOperatorModel> models = new ArrayList<>();

        when(taxiOperatorDAO.findAll()).thenReturn(entities);
        when(transportMapper.taxiEntitiesToDtos(entities)).thenReturn(models);

        // Act
        ResponseEntity<List<TransportTaxiOperatorModel>> result = transportService.findTaxiOperators();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().size());

        verify(taxiOperatorDAO).findAll();
        verify(transportMapper).taxiEntitiesToDtos(entities);
    }
}
