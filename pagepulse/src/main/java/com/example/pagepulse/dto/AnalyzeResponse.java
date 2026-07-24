package com.example.pagepulse.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AnalyzeResponse {

    String analyzedUrl;
    int httpStatus;
    long responseTimeMs;
    String pageTitle;
    String metaDescription;
    int h1Count;
    int imagesMissingAltCount;
    int wordCount;
}