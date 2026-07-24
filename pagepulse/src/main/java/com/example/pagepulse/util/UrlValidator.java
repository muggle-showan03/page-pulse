package com.example.pagepulse.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public final class UrlValidator {

    private UrlValidator() {
    }

    public static Optional<URI> parse(String rawUrl) {
        if (rawUrl == null || rawUrl.isBlank()) {
            return Optional.empty();
        }

        try {
            URI uri = new URI(rawUrl.trim());
            String scheme = uri.getScheme();
            String host = uri.getHost();

            boolean hasValidScheme = "http".equalsIgnoreCase(scheme)
                    || "https".equalsIgnoreCase(scheme);
            boolean hasHost = host != null && !host.isBlank();

            if (hasValidScheme && hasHost) {
                return Optional.of(uri);
            }
            return Optional.empty();

        } catch (URISyntaxException e) {
            return Optional.empty();
        }
    }

    public static boolean isValid(String rawUrl) {
        return parse(rawUrl).isPresent();
    }
}