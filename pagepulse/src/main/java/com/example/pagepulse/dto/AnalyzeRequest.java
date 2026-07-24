package com.example.pagepulse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AnalyzeRequest(

        @NotBlank(message = "URL must not be blank")
        @Pattern(regexp = "^https?://.+$", message = "URL must start with http:// or https://")
        String url

) {}