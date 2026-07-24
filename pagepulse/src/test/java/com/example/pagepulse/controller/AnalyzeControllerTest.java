package com.example.pagepulse.controller;

import com.example.pagepulse.dto.AnalyzeResponse;
import com.example.pagepulse.exception.InvalidUrlException;
import com.example.pagepulse.exception.PageFetchException;
import com.example.pagepulse.service.AnalyzeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.http.HttpTimeoutException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyzeController.class)
class AnalyzeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnalyzeService analyzeService;

    private static final String ANALYZE_URL = "/api/analyze";
    private static final String VALID_URL = "https://example.com";

    @Nested
    @DisplayName("POST /api/analyze — Success")
    class SuccessScenarios {

        @Test
        @DisplayName("Should return 200 with correct response body")
        void shouldReturnSuccessfulAnalysis() throws Exception {
            // Arrange
            AnalyzeResponse mockResponse = AnalyzeResponse.builder()
                    .analyzedUrl(VALID_URL)
                    .httpStatus(200)
                    .responseTimeMs(150L)
                    .pageTitle("Example Domain")
                    .metaDescription("An example page")
                    .h1Count(1)
                    .imagesMissingAltCount(2)
                    .wordCount(50)
                    .build();

            when(analyzeService.analyze(VALID_URL)).thenReturn(mockResponse);

            // Act & Assert
            mockMvc.perform(post(ANALYZE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"url\": \"" + VALID_URL + "\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.analyzedUrl").value(VALID_URL))
                    .andExpect(jsonPath("$.httpStatus").value(200))
                    .andExpect(jsonPath("$.responseTimeMs").value(150))
                    .andExpect(jsonPath("$.pageTitle").value("Example Domain"))
                    .andExpect(jsonPath("$.metaDescription").value("An example page"))
                    .andExpect(jsonPath("$.h1Count").value(1))
                    .andExpect(jsonPath("$.imagesMissingAltCount").value(2))
                    .andExpect(jsonPath("$.wordCount").value(50));
        }
    }

    @Nested
    @DisplayName("POST /api/analyze — Validation Errors")
    class ValidationErrors {

        @Test
        @DisplayName("Should return 400 for invalid URL")
        void shouldReturn400ForInvalidUrl() throws Exception {
            // Arrange
            when(analyzeService.analyze(anyString()))
                    .thenThrow(new InvalidUrlException("not-a-url"));

            // Act & Assert
            mockMvc.perform(post(ANALYZE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"url\": \"not-a-url\"}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.path").value(ANALYZE_URL));
        }

        @Test
        @DisplayName("Should return 400 for missing request body")
        void shouldReturn400ForMissingBody() throws Exception {
            // Act & Assert
            mockMvc.perform(post(ANALYZE_URL)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("Malformed or missing request body"));
        }

        @Test
        @DisplayName("Should return 400 for malformed JSON")
        void shouldReturn400ForMalformedJson() throws Exception {
            // Act & Assert
            mockMvc.perform(post(ANALYZE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{invalid json}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"));
        }
    }

    @Nested
    @DisplayName("POST /api/analyze — Network Errors")
    class NetworkErrors {

        @Test
        @DisplayName("Should return 502 when website is unreachable")
        void shouldReturn502ForUnreachableSite() throws Exception {
            // Arrange
            when(analyzeService.analyze(VALID_URL))
                    .thenThrow(new PageFetchException(VALID_URL, new IOException("Connection refused")));

            // Act & Assert
            mockMvc.perform(post(ANALYZE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"url\": \"" + VALID_URL + "\"}"))
                    .andExpect(status().isBadGateway())
                    .andExpect(jsonPath("$.status").value(502))
                    .andExpect(jsonPath("$.error").value("Bad Gateway"))
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.path").value(ANALYZE_URL));
        }

        @Test
        @DisplayName("Should return 408 on request timeout")
        void shouldReturn408OnTimeout() throws Exception {
            // Arrange
            when(analyzeService.analyze(VALID_URL))
                    .thenThrow(new PageFetchException(VALID_URL, new HttpTimeoutException("Timed out")));

            // Act & Assert
            mockMvc.perform(post(ANALYZE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"url\": \"" + VALID_URL + "\"}"))
                    .andExpect(status().isRequestTimeout())
                    .andExpect(jsonPath("$.status").value(408))
                    .andExpect(jsonPath("$.error").value("Request Timeout"))
                    .andExpect(jsonPath("$.path").value(ANALYZE_URL));
        }
    }

    @Nested
    @DisplayName("POST /api/analyze — Error Response Structure")
    class ErrorResponseStructure {

        @Test
        @DisplayName("Error response should contain all required fields")
        void errorResponseShouldHaveAllFields() throws Exception {
            // Arrange
            when(analyzeService.analyze(anyString()))
                    .thenThrow(new InvalidUrlException("bad-url"));

            // Act & Assert
            mockMvc.perform(post(ANALYZE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"url\": \"bad-url\"}"))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.status").isNumber())
                    .andExpect(jsonPath("$.error").isString())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.path").isString());
        }
    }
}
