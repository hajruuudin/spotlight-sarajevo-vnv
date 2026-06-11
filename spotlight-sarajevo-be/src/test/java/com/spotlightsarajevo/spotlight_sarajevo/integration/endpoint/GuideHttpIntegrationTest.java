package com.spotlightsarajevo.spotlight_sarajevo.integration.endpoint;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.exceptions.TouristGuideExceptions;
import com.spotlightsarajevo.modules.guide.api.dto.TouristGuideCreateModel;
import com.spotlightsarajevo.modules.guide.api.dto.TouristGuideModel;
import com.spotlightsarajevo.modules.guide.api.dto.TouristGuideOverviewModel;
import com.spotlightsarajevo.modules.guide.service.TouristGuideService;
import com.spotlightsarajevo.spotlight_sarajevo.integration.BaseIntegrationClass;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GuideHttpIntegrationTest extends BaseIntegrationClass {
    @MockitoBean
    private TouristGuideService guideService;

    @Test
    void findAllGuides_returns200() throws Exception {
        when(guideService.findAllGuides())
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/guide/all"))
                .andExpect(status().isOk());
    }

    @Test
    void findGuideOverview_validSlug_returns200() throws Exception {
        when(guideService.findGuideOverview("some-slug"))
                .thenReturn(ResponseEntity.ok(new TouristGuideOverviewModel()));

        mockMvc.perform(get("/guide/some-slug"))
                .andExpect(status().isOk());
    }

    @Test
    void findGuideOverview_invalidSlug_returns404() throws Exception {
        when(guideService.findGuideOverview("nonexistent"))
                .thenThrow(new TouristGuideExceptions.TouristGuideNotFound(
                        ExceptionCodes.TouristGuideExceptionCodes.TOURIST_GUIDE_NOT_FOUND));

        mockMvc.perform(get("/guide/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_validRequest_returns200() throws Exception {
        when(guideService.create(any(TouristGuideCreateModel.class), any()))
                .thenReturn(ResponseEntity.ok(new TouristGuideOverviewModel()));

        mockMvc.perform(post("/guide/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TouristGuideCreateModel())))
                .andExpect(status().isOk());
    }

    @Test
    void delete_validId_returns200() throws Exception {
        when(guideService.delete(1))
                .thenReturn(ResponseEntity.ok(new TouristGuideModel()));

        mockMvc.perform(delete("/guide/1"))
                .andExpect(status().isOk());
    }

    @Test
    void findByCategory_validCategoryId_returns200() throws Exception {
        when(guideService.findByCategory(1))
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/guide/category/1"))
                .andExpect(status().isOk());
    }
}
