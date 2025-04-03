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

import java.util.UUID;
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


        Survey surveyToSave = Survey.builder()
                .subject(subjectEnum)
                .support(supportEnum)
                .userId(surveyRequest.getUserId())
                .build();

        Survey survey = surveyRepository.save(surveyToSave);

        return new SurveyResponse(
                survey.getUserId(),
                survey.getSubject().toString(),
                survey.getSupport().toString());
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



//    public List<Survey> getAllSurveyRequests() {
//        return surveyRepository.findAll();
//    }
}
