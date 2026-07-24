package com.example.pagepulse.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UrlValidatorTest {

    @Test
    void parse_returnsUri_forValidHttpsUrl() {
        Optional<URI> result = UrlValidator.parse("https://example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getHost()).isEqualTo("example.com");
    }

    @Test
    void parse_returnsUri_forValidHttpUrl() {
        Optional<URI> result = UrlValidator.parse("http://example.com/page");

        assertThat(result).isPresent();
    }

    @Test
    void parse_trimsWhitespace_beforeParsing() {
        Optional<URI> result = UrlValidator.parse("  https://example.com  ");

        assertThat(result).isPresent();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "not a url", "ftp://example.com",
            "https://", "https:///path", "example.com"})
    void parse_returnsEmpty_forInvalidInput(String invalidUrl) {
        Optional<URI> result = UrlValidator.parse(invalidUrl);

        assertThat(result).isEmpty();
    }

    @Test
    void isValid_returnsTrue_forWellFormedUrl() {
        assertThat(UrlValidator.isValid("https://example.com")).isTrue();
    }

    @Test
    void isValid_returnsFalse_forMalformedUrl() {
        assertThat(UrlValidator.isValid("htp:/bad-url")).isFalse();
    }
}