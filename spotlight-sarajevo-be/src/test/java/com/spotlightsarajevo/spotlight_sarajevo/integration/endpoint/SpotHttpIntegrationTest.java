package com.spotlightsarajevo.spotlight_sarajevo.integration.endpoint;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.exceptions.SpotExceptions;
import com.spotlightsarajevo.modules.spot.api.dto.SpotOverviewModel;
import com.spotlightsarajevo.modules.spot.domain.SpotDAO;
import com.spotlightsarajevo.modules.spot.domain.SpotTagsDAO;
import com.spotlightsarajevo.modules.spot.domain.SpotWorkHoursDAO;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotEntity;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotReviewStatsEntity;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotTagsEntity;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotWorkHoursEntity;
import com.spotlightsarajevo.modules.spot.service.SpotService;
import com.spotlightsarajevo.modules.spot.service.SpotServiceImpl;
import com.spotlightsarajevo.spotlight_sarajevo.integration.BaseIntegrationClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SpotHttpIntegrationTest extends BaseIntegrationClass {

    @MockitoBean
    private SpotService spotService;
    @Mock
    private SpotDAO spotDAO;

    // ===================== PUBLIC ENDPOINTS =====================

    @Test
    void findSpotOverview_validSlug_returns200() throws Exception {
        when(spotService.findSpotOverview("test-spot"))
                .thenReturn(ResponseEntity.ok(new SpotOverviewModel()));

        mockMvc.perform(get("/spot/find-spot-overview")
                        .param("spotSlug", "test-spot"))
                .andExpect(status().isOk());
    }

    @Test
    void findSpotOverview_missingSlug_returns400() throws Exception {
        mockMvc.perform(get("/spot/find-spot-overview"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findSpotOverview_notFound_returns404() throws Exception {
        when(spotService.findSpotOverview("nonexistent"))
                .thenThrow(new SpotExceptions.SpotNotFoundException(
                        ExceptionCodes.SpotExceptionCodes.SPOT_NOT_FOUND));

        mockMvc.perform(get("/spot/find-spot-overview")
                        .param("spotSlug", "nonexistent"))
                .andExpect(status().isNotFound());
    }

    // ===================== ADMIN ENDPOINTS =====================

    @Test
    void getSpotsTotalCount_asAdmin_returns200() throws Exception {
        when(spotService.getSpotsTotalCount(any()))
                .thenReturn(ResponseEntity.ok(42L));

        mockMvc.perform(get("/spot/admin/count")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(42));
    }

    @Test
    void getSpotsTotalCount_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/spot/admin/count"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getSpotsTotalCount_asRegularUser_returns403() throws Exception {
        mockMvc.perform(get("/spot/admin/count")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }
}
