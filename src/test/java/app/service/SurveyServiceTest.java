package app.service;
import app.model.Subject;
import app.model.Support;
import app.model.Survey;
import app.repository.SurveyRepository;
import app.web.dto.SurveyRequest;
import app.web.dto.SurveyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
public class SurveyServiceTest {

    private SurveyRepository surveyRepository;
    private SurveyService surveyService;

    private final UUID mockUserId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        surveyRepository = mock(SurveyRepository.class);
        surveyService = new SurveyService(surveyRepository);
    }

    @Test
    void testSubmitSurvey_ShouldCreateNewSurveyIfNoneExists() {
        SurveyRequest request = new SurveyRequest(mockUserId, "RECYCLING", "SUPPORT");

        when(surveyRepository.findByUserId(mockUserId)).thenReturn(Optional.empty());

        Survey savedSurvey = Survey.builder()
                .id(UUID.randomUUID())
                .userId(mockUserId)
                .subject(Subject.RECYCLING)
                .support(Support.SUPPORT)
                .build();

        when(surveyRepository.save(any())).thenReturn(savedSurvey);

        SurveyResponse response = surveyService.submitSurvey(request);

        assertThat(response.getUserId()).isEqualTo(mockUserId);
        assertThat(response.getSubject()).isEqualTo("RECYCLING");
        assertThat(response.getSupport()).isEqualTo("SUPPORT");
    }

    @Test
    void testSubmitSurvey_ShouldUpdateIfSurveyExists() {
        SurveyRequest request = new SurveyRequest(mockUserId, "SUSTAINABILITY", "SUPPORT");

        Survey existingSurvey = Survey.builder()
                .id(UUID.randomUUID())
                .userId(mockUserId)
                .subject(Subject.RECYCLING)
                .support(Support.SUPPORT)
                .build();

        when(surveyRepository.findByUserId(mockUserId)).thenReturn(Optional.of(existingSurvey));
        when(surveyRepository.save(any())).thenReturn(existingSurvey);

        SurveyResponse response = surveyService.submitSurvey(request);

        assertThat(response.getSubject()).isEqualTo("SUSTAINABILITY");
        assertThat(response.getSupport()).isEqualTo("SUPPORT");
    }

    @Test
    void testGetSurvey_ShouldReturnSurveyResponse_WhenExists() {
        Survey survey = Survey.builder()
                .id(UUID.randomUUID())
                .userId(mockUserId)
                .subject(Subject.ENVIRONMENT)
                .support(Support.SUPPORT)
                .build();

        when(surveyRepository.findByUserId(mockUserId)).thenReturn(Optional.of(survey));

        SurveyResponse response = surveyService.getSurvey(mockUserId);

        assertThat(response.getUserId()).isEqualTo(mockUserId);
        assertThat(response.getSubject()).isEqualTo("ENVIRONMENT");
    }

    @Test
    void testGetSurvey_ShouldThrow_WhenSurveyNotFound() {
        when(surveyRepository.findByUserId(mockUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> surveyService.getSurvey(mockUserId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Survey not found for user");
    }

    @Test
    void testGetVoteStats_ShouldGroupBySubjectCorrectly() {
        Survey s1 = Survey.builder().userId(UUID.randomUUID()).subject(Subject.RECYCLING).support(Support.SUPPORT).build();
        Survey s2 = Survey.builder().userId(UUID.randomUUID()).subject(Subject.RECYCLING).support(Support.SUPPORT).build();
        Survey s3 = Survey.builder().userId(UUID.randomUUID()).subject(Subject.ENVIRONMENT).support(Support.SUPPORT).build();

        when(surveyRepository.findAll()).thenReturn(List.of(s1, s2, s3));

        Map<String, Long> stats = surveyService.getVoteStats();

        assertThat(stats).containsEntry("RECYCLING", 2L);
        assertThat(stats).containsEntry("ENVIRONMENT", 1L);
    }
}
