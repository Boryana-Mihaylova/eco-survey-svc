package app.web;
import app.web.dto.SurveyRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import app.service.SurveyService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SurveyController.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenInvalidInput_thenReturnsValidationErrors() throws Exception {
        SurveyRequest invalidRequest = new SurveyRequest(); // всички полета null

        mockMvc.perform(post("/api/v1/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.subject").exists())
                .andExpect(jsonPath("$.support").exists());
    }
}
