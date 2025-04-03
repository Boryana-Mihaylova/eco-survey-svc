package app.web;

import app.model.service.SurveyService;
import app.web.dto.SurveyRequest;
import app.web.dto.SurveyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }



    @PostMapping
    public ResponseEntity<SurveyResponse> submitSurvey(@RequestBody SurveyRequest surveyRequest) {

        SurveyResponse response = surveyService.submitSurvey(surveyRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/user-survey")
    public ResponseEntity<SurveyResponse> getSurvey(@RequestParam(name = "userId") UUID userId) {

        SurveyResponse surveyResponse = surveyService.getSurvey(userId);
        return ResponseEntity.ok(surveyResponse);
    }
}
