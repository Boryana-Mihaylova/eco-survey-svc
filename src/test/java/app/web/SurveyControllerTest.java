package app.web;
import app.model.Support;
import app.model.Subject;
import app.service.SurveyService;
import app.web.dto.SurveyRequest;
import app.web.dto.SurveyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SurveyController.class)
public class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID userId = UUID.randomUUID();

    @Test
    void testSubmitSurvey_ShouldReturnCreatedResponse() throws Exception {
        SurveyRequest request = new SurveyRequest(userId, "RECYCLING", "SUPPORT");
        SurveyResponse response = new SurveyResponse(userId, "RECYCLING", "SUPPORT");

        Mockito.when(surveyService.submitSurvey(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.subject").value("RECYCLING"))
                .andExpect(jsonPath("$.support").value("SUPPORT"));
    }

    @Test
    void testGetSurvey_ShouldReturnSurveyResponse() throws Exception {
        SurveyResponse response = new SurveyResponse(userId, "ENVIRONMENT", "SUPPORT");

        Mockito.when(surveyService.getSurvey(userId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/surveys/user-survey")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.subject").value("ENVIRONMENT"))
                .andExpect(jsonPath("$.support").value("SUPPORT"));
    }

    @Test
    void testGetStats_ShouldReturnVoteStats() throws Exception {
        Map<String, Long> stats = Map.of(
                "RECYCLING", 5L,
                "SUSTAINABILITY", 3L
        );

        Mockito.when(surveyService.getVoteStats()).thenReturn(stats);

        mockMvc.perform(get("/api/v1/surveys/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.RECYCLING").value(5))
                .andExpect(jsonPath("$.SUSTAINABILITY").value(3));
    }
}
