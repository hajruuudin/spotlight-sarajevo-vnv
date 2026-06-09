package com.spotlightsarajevo.modules.transport.api;

import com.spotlightsarajevo.modules.transport.api.dto.TransportMethodLineModel;
import com.spotlightsarajevo.modules.transport.api.dto.TransportMethodModel;
import com.spotlightsarajevo.modules.transport.api.dto.TransportMethodShorthandModel;
import com.spotlightsarajevo.modules.transport.api.dto.TransportTaxiOperatorModel;
import com.spotlightsarajevo.modules.transport.service.TransportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "transport", description = "Transport API")
@AllArgsConstructor
@RequestMapping(value = "/transport")
public class TransportRESTController {

    TransportService transportService;

    @GetMapping(value = "/all")
    @Operation(description = "Get all transport methods with only their ID and names")
    public ResponseEntity<List<TransportMethodShorthandModel>> findAllTransportMethods() {
        return transportService.findAllTransportMethods();
    }

    @GetMapping(value = "/{id}")
    @Operation(description = "Get a specific transport method by ID with all its data including geometry")
    public ResponseEntity<TransportMethodModel> findTransportMethodById(
            @PathVariable Integer id
    ) {
        return transportService.findTransportMethodById(id);
    }

    @GetMapping(value = "/lines")
    @Operation(description = "Get all lines for a specific operator and transport type")
    public ResponseEntity<List<TransportMethodLineModel>> findLinesByOperatorAndTransportType(
            @RequestParam Integer operatorId,
            @RequestParam Integer transportTypeId
    ) {
        return transportService.findLinesByOperatorAndTransportType(operatorId, transportTypeId);
    }

    @GetMapping(value = "/taxis")
    @Operation(description = "Get all the current Taxi operators inside the database")
    public ResponseEntity<List<TransportTaxiOperatorModel>> findTaxiOperators(){
        return transportService.findTaxiOperators();
    }
}
