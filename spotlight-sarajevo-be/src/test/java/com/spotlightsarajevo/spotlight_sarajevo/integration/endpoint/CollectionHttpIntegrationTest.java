package com.spotlightsarajevo.spotlight_sarajevo.integration.endpoint;

import com.spotlightsarajevo.modules.collection.api.dto.CollectionCreateModel;
import com.spotlightsarajevo.modules.collection.api.dto.CollectionModel;
import com.spotlightsarajevo.modules.collection.domain.CollectionDAO;
import com.spotlightsarajevo.modules.collection.service.CollectionService;
import com.spotlightsarajevo.spotlight_sarajevo.integration.BaseIntegrationClass;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CollectionHttpIntegrationTest extends BaseIntegrationClass {
    @MockitoBean
    private CollectionService collectionService;
    @Mock
    private CollectionDAO collectionDAO;

    @Test
    void findUserCollections_returns200() throws Exception {
        when(collectionService.findUserCollections(any()))
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/collection/all"))
                .andExpect(status().isOk());
    }

    @Test
    void addCollection_validRequest_returns200() throws Exception {
        when(collectionService.addCollection(any(CollectionCreateModel.class), any()))
                .thenReturn(ResponseEntity.ok(new CollectionModel()));

        mockMvc.perform(post("/collection")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CollectionCreateModel())))
                .andExpect(status().isOk());
    }

    @Test
    void addCollection_missingContentType_returns415() throws Exception {
        mockMvc.perform(post("/collection")
                        .content(objectMapper.writeValueAsString(new CollectionCreateModel())))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void deleteCollection_validId_returns200() throws Exception {
        when(collectionService.deleteCollection(eq(1), any()))
                .thenReturn(ResponseEntity.ok(new CollectionModel()));

        mockMvc.perform(delete("/collection/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void removeItemFromCollection_missingParams_returns400() throws Exception {
        mockMvc.perform(delete("/collection/remove-item"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void existsInSystemCollections_missingParams_returns400() throws Exception {
        mockMvc.perform(get("/collection/exists"))
                .andExpect(status().isBadRequest());
    }
}
