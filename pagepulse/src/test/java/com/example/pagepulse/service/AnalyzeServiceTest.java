package com.example.pagepulse.service;

import com.example.pagepulse.dto.AnalyzeResponse;
import com.example.pagepulse.exception.InvalidUrlException;
import com.example.pagepulse.exception.PageFetchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnalyzeServiceTest {

    private AnalyzeService analyzeService;
    private HttpClient mockHttpClient;

    @SuppressWarnings("unchecked")
    private HttpResponse<String> mockResponse;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        analyzeService = new AnalyzeService();
        mockHttpClient = mock(HttpClient.class);
        mockResponse = mock(HttpResponse.class);

        // Replace the real HttpClient with our mock
        ReflectionTestUtils.setField(analyzeService, "httpClient", mockHttpClient);
    }

    @Nested
    @DisplayName("URL Validation")
    class UrlValidation {

        @Test
        @DisplayName("Should throw InvalidUrlException for null URL")
        void shouldRejectNullUrl() {
            assertThatThrownBy(() -> analyzeService.analyze(null))
                    .isInstanceOf(InvalidUrlException.class);
        }

        @Test
        @DisplayName("Should throw InvalidUrlException for blank URL")
        void shouldRejectBlankUrl() {
            assertThatThrownBy(() -> analyzeService.analyze(""))
                    .isInstanceOf(InvalidUrlException.class);
        }

        @Test
        @DisplayName("Should throw InvalidUrlException for URL without scheme")
        void shouldRejectUrlWithoutScheme() {
            assertThatThrownBy(() -> analyzeService.analyze("example.com"))
                    .isInstanceOf(InvalidUrlException.class);
        }

        @Test
        @DisplayName("Should throw InvalidUrlException for FTP URL")
        void shouldRejectFtpUrl() {
            assertThatThrownBy(() -> analyzeService.analyze("ftp://example.com"))
                    .isInstanceOf(InvalidUrlException.class);
        }
    }

    @Nested
    @DisplayName("Successful Analysis")
    class SuccessfulAnalysis {

        private static final String VALID_URL = "https://example.com";
        private static final String FULL_HTML = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Example Page</title>
                    <meta name="description" content="A sample page for testing">
                </head>
                <body>
                    <h1>Main Heading</h1>
                    <p>Hello world this is a test page with some content.</p>
                    <img src="logo.png" alt="Logo">
                    <img src="banner.png">
                    <img src="icon.png" alt="">
                </body>
                </html>
                """;

        @Test
        @DisplayName("Should return correct page title")
        void shouldExtractPageTitle() throws Exception {
            // Arrange
            stubHttpResponse(200, FULL_HTML);

            // Act
            AnalyzeResponse response = analyzeService.analyze(VALID_URL);

            // Assert
            assertThat(response.getPageTitle()).isEqualTo("Example Page");
        }

        @Test
        @DisplayName("Should return correct meta description")
        void shouldExtractMetaDescription() throws Exception {
            // Arrange
            stubHttpResponse(200, FULL_HTML);

            // Act
            AnalyzeResponse response = analyzeService.analyze(VALID_URL);

            // Assert
            assertThat(response.getMetaDescription()).isEqualTo("A sample page for testing");
        }

        @Test
        @DisplayName("Should count H1 tags")
        void shouldCountH1Tags() throws Exception {
            // Arrange
            stubHttpResponse(200, FULL_HTML);

            // Act
            AnalyzeResponse response = analyzeService.analyze(VALID_URL);

            // Assert
            assertThat(response.getH1Count()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should count images missing alt attribute")
        void shouldCountImagesMissingAlt() throws Exception {
            // Arrange
            stubHttpResponse(200, FULL_HTML);

            // Act
            AnalyzeResponse response = analyzeService.analyze(VALID_URL);

            // Assert — banner.png has no alt, icon.png has empty alt
            assertThat(response.getImagesMissingAltCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should count words in page body")
        void shouldCountWords() throws Exception {
            // Arrange
            stubHttpResponse(200, FULL_HTML);

            // Act
            AnalyzeResponse response = analyzeService.analyze(VALID_URL);

            // Assert
            assertThat(response.getWordCount()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should return correct HTTP status and URL")
        void shouldReturnHttpStatusAndUrl() throws Exception {
            // Arrange
            stubHttpResponse(200, FULL_HTML);

            // Act
            AnalyzeResponse response = analyzeService.analyze(VALID_URL);

            // Assert
            assertThat(response.getAnalyzedUrl()).isEqualTo(VALID_URL);
            assertThat(response.getHttpStatus()).isEqualTo(200);
            assertThat(response.getResponseTimeMs()).isGreaterThanOrEqualTo(0);
        }

        @Test
        @DisplayName("Should handle non-200 HTTP status")
        void shouldHandleNon200Status() throws Exception {
            // Arrange
            stubHttpResponse(404, "<html><head><title>Not Found</title></head><body></body></html>");

            // Act
            AnalyzeResponse response = analyzeService.analyze(VALID_URL);

            // Assert
            assertThat(response.getHttpStatus()).isEqualTo(404);
            assertThat(response.getPageTitle()).isEqualTo("Not Found");
        }

        private void stubHttpResponse(int statusCode, String body) throws Exception {
            when(mockResponse.statusCode()).thenReturn(statusCode);
            when(mockResponse.body()).thenReturn(body);
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);
        }
    }

    @Nested
    @DisplayName("Network Errors")
    class NetworkErrors {

        private static final String VALID_URL = "https://example.com";

        @Test
        @DisplayName("Should throw PageFetchException when site is unreachable")
        void shouldThrowOnUnreachableSite() throws Exception {
            // Arrange
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenThrow(new IOException("Connection refused"));

            // Act & Assert
            assertThatThrownBy(() -> analyzeService.analyze(VALID_URL))
                    .isInstanceOf(PageFetchException.class)
                    .hasMessageContaining(VALID_URL)
                    .hasCauseInstanceOf(IOException.class);
        }

        @Test
        @DisplayName("Should throw PageFetchException on request timeout")
        void shouldThrowOnTimeout() throws Exception {
            // Arrange
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenThrow(new HttpTimeoutException("Request timed out"));

            // Act & Assert
            assertThatThrownBy(() -> analyzeService.analyze(VALID_URL))
                    .isInstanceOf(PageFetchException.class)
                    .hasCauseInstanceOf(HttpTimeoutException.class);
        }

        @Test
        @DisplayName("Should throw PageFetchException on interrupted request")
        void shouldThrowOnInterruption() throws Exception {
            // Arrange
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenThrow(new InterruptedException("Thread interrupted"));

            // Act & Assert
            assertThatThrownBy(() -> analyzeService.analyze(VALID_URL))
                    .isInstanceOf(PageFetchException.class)
                    .hasCauseInstanceOf(InterruptedException.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    @SuppressWarnings("unchecked")
    class EdgeCases {

        private static final String VALID_URL = "https://example.com";

        @Test
        @DisplayName("Should handle non-HTML response gracefully")
        void shouldHandleNonHtmlResponse() throws Exception {
            // Arrange
            String jsonBody = "{\"key\": \"value\"}";
            when(mockResponse.statusCode()).thenReturn(200);
            when(mockResponse.body()).thenReturn(jsonBody);
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);

            // Act
            AnalyzeResponse response = analyzeService.analyze(VALID_URL);

            // Assert — Jsoup parses non-HTML gracefully, no exception thrown
            assertThat(response.getAnalyzedUrl()).isEqualTo(VALID_URL);
            assertThat(response.getHttpStatus()).isEqualTo(200);
            assertThat(response.getPageTitle()).isEmpty();
            assertThat(response.getMetaDescription()).isEmpty();
            assertThat(response.getH1Count()).isZero();
        }

        @Test
        @DisplayName("Should handle empty HTML document")
        void shouldHandleEmptyHtml() throws Exception {
            // Arrange
            when(mockResponse.statusCode()).thenReturn(200);
            when(mockResponse.body()).thenReturn("");
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);

            // Act
            AnalyzeResponse response = analyzeService.analyze(VALID_URL);

            // Assert
            assertThat(response.getPageTitle()).isEmpty();
            assertThat(response.getMetaDescription()).isEmpty();
            assertThat(response.getH1Count()).isZero();
            assertThat(response.getImagesMissingAltCount()).isZero();
            assertThat(response.getWordCount()).isZero();
        }

        @Test
        @DisplayName("Should handle HTML with no meta description")
        void shouldHandleMissingMetaDescription() throws Exception {
            // Arrange
            String html = "<html><head><title>No Meta</title></head><body><p>Content</p></body></html>";
            when(mockResponse.statusCode()).thenReturn(200);
            when(mockResponse.body()).thenReturn(html);
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);

            // Act
            AnalyzeResponse response = analyzeService.analyze(VALID_URL);

            // Assert
            assertThat(response.getPageTitle()).isEqualTo("No Meta");
            assertThat(response.getMetaDescription()).isEmpty();
        }

        @Test
        @DisplayName("Should handle HTML with multiple H1 tags")
        void shouldCountMultipleH1Tags() throws Exception {
            // Arrange
            String html = """
                    <html><body>
                        <h1>First</h1>
                        <h1>Second</h1>
                        <h1>Third</h1>
                    </body></html>
                    """;
            when(mockResponse.statusCode()).thenReturn(200);
            when(mockResponse.body()).thenReturn(html);
            when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);

            // Act
            AnalyzeResponse response = analyzeService.analyze(VALID_URL);

            // Assert
            assertThat(response.getH1Count()).isEqualTo(3);
        }
    }
}
