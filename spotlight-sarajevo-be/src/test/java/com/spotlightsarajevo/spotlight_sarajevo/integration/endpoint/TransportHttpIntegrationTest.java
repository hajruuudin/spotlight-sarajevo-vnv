package com.spotlightsarajevo.spotlight_sarajevo.integration.endpoint;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.exceptions.TransportExceptions;
import com.spotlightsarajevo.modules.transport.api.dto.TransportMethodModel;
import com.spotlightsarajevo.modules.transport.service.TransportService;
import com.spotlightsarajevo.spotlight_sarajevo.integration.BaseIntegrationClass;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransportHttpIntegrationTest extends BaseIntegrationClass {
    @MockitoBean
    private TransportService transportService;

    @Test
    void findAllTransportMethods_returns200() throws Exception {
        when(transportService.findAllTransportMethods())
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/transport/all"))
                .andExpect(status().isOk());
    }

    @Test
    void findTransportMethodById_validId_returns200() throws Exception {
        when(transportService.findTransportMethodById(1))
                .thenReturn(ResponseEntity.ok(new TransportMethodModel()));

        mockMvc.perform(get("/transport/1"))
                .andExpect(status().isOk());
    }

    @Test
    void findTransportMethodById_invalidId_returns404() throws Exception {
        when(transportService.findTransportMethodById(999))
                .thenThrow(new TransportExceptions.TransportNotFoundException(
                        ExceptionCodes.TransportExceptionCodes.TRANSPORT_METHOD_NOT_FOUND));

        mockMvc.perform(get("/transport/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findLinesByOperatorAndTransportType_validParams_returns200() throws Exception {
        when(transportService.findLinesByOperatorAndTransportType(1, 1))
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/transport/lines")
                        .param("operatorId", "1")
                        .param("transportTypeId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void findLinesByOperatorAndTransportType_missingParams_returns400() throws Exception {
        mockMvc.perform(get("/transport/lines"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findTaxiOperators_returns200() throws Exception {
        when(transportService.findTaxiOperators())
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/transport/taxis"))
                .andExpect(status().isOk());
    }
}
