package com.example.pagepulse.controller;

import com.example.pagepulse.dto.AnalyzeRequest;
import com.example.pagepulse.dto.AnalyzeResponse;
import com.example.pagepulse.service.AnalyzeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AnalyzeController {

    private final AnalyzeService analyzeService;

    public AnalyzeController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeResponse> analyze(@RequestBody AnalyzeRequest request) {
        AnalyzeResponse response = analyzeService.analyze(request.url());
        return ResponseEntity.ok(response);
    }
}
