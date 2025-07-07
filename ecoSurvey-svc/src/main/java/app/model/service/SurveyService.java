package app.model.service;

import app.model.model.Subject;
import app.model.model.Support;
import app.model.model.Survey;
import app.model.repository.SurveyRepository;
import app.web.dto.SurveyRequest;
import app.web.dto.SurveyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }


    public SurveyResponse submitSurvey(SurveyRequest surveyRequest) {

        log.info("Submitting survey for user: {}", surveyRequest.getUserId());

        Subject subjectEnum = Subject.valueOf(surveyRequest.getSubject().toUpperCase());
        Support supportEnum = Support.valueOf(surveyRequest.getSupport().toUpperCase());

        Survey survey = surveyRepository.findByUserId(surveyRequest.getUserId())
                .map(existing -> {
                    existing.setSubject(subjectEnum);
                    existing.setSupport(supportEnum);
                    return existing;
                })
                .orElseGet(() -> Survey.builder()
                        .userId(surveyRequest.getUserId())
                        .subject(subjectEnum)
                        .support(supportEnum)
                        .build()
                );

        Survey saved = surveyRepository.save(survey);

        return new SurveyResponse(
                saved.getUserId(),
                saved.getSubject().toString(),
                saved.getSupport().toString()
        );
    }


    public SurveyResponse getSurvey(UUID userId) {

        Survey survey = surveyRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Survey not found for user: {}", userId);
                    return new RuntimeException("Survey not found for user: " + userId);
                });

        return new SurveyResponse(
                survey.getUserId(),
                survey.getSubject().toString(),
                survey.getSupport().toString());
    }

    public Map<String, Long> getVoteStats() {
        List<Object[]> results = surveyRepository.countVotesBySubject();

        return results.stream()
                .collect(Collectors.toMap(
                        r -> ((Subject) r[0]).name(),
                        r -> (Long) r[1]
                ));
    }
}
