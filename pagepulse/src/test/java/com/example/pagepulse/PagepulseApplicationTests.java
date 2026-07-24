package com.example.pagepulse;

import com.example.pagepulse.dto.AnalyzeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PagepulseApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void dtoSanityCheck() {
        AnalyzeResponse response = AnalyzeResponse.builder()
                .analyzedUrl("https://example.com")
                .httpStatus(200)
                .responseTimeMs(150L)
                .pageTitle("Example")
                .metaDescription(null)
                .h1Count(1)
                .imagesMissingAltCount(2)
                .wordCount(300)
                .build();

        assertThat(response.getHttpStatus()).isEqualTo(200);
    }
}