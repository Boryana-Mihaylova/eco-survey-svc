package app.web;


import app.service.SurveyService;
import app.web.dto.SurveyRequest;
import app.web.dto.SurveyResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
@Slf4j
@RestController
@RequestMapping("/api/v1/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }



    @PostMapping
    public ResponseEntity<SurveyResponse> submitSurvey(@Valid @RequestBody SurveyRequest surveyRequest) {
        log.info("Received survey request: {}", surveyRequest);
        SurveyResponse response = surveyService.submitSurvey(surveyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user-survey")
    public ResponseEntity<SurveyResponse> getSurvey(@RequestParam(name = "userId") UUID userId) {

        SurveyResponse surveyResponse = surveyService.getSurvey(userId);
        return ResponseEntity.ok(surveyResponse);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(surveyService.getVoteStats());
    }
}
