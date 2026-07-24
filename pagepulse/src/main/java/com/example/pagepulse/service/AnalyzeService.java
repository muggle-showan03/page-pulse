package com.example.pagepulse.service;

import com.example.pagepulse.dto.AnalyzeResponse;
import com.example.pagepulse.exception.InvalidUrlException;
import com.example.pagepulse.exception.PageFetchException;
import com.example.pagepulse.util.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class AnalyzeService {

    private static final Duration CONNECTION_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    private final HttpClient httpClient;

    public AnalyzeService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(CONNECTION_TIMEOUT)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public AnalyzeResponse analyze(String url) {
        if (!UrlValidator.isValid(url)) {
            throw new InvalidUrlException(url);
        }

        long startTime = System.currentTimeMillis();
        HttpResponse<String> httpResponse = fetchPage(url);
        long responseTimeMs = System.currentTimeMillis() - startTime;

        int httpStatus = httpResponse.statusCode();
        String html = httpResponse.body();

        Document document = Jsoup.parse(html, url);

        return AnalyzeResponse.builder()
                .analyzedUrl(url)
                .httpStatus(httpStatus)
                .responseTimeMs(responseTimeMs)
                .pageTitle(extractTitle(document))
                .metaDescription(extractMetaDescription(document))
                .h1Count(countH1Tags(document))
                .imagesMissingAltCount(countImagesMissingAlt(document))
                .wordCount(countWords(document))
                .build();
    }

    private String extractTitle(Document document) {
        String title = document.title();
        return title != null ? title : "";
    }

    private String extractMetaDescription(Document document) {
        Element metaTag = document.selectFirst("meta[name=description]");
        if (metaTag == null) {
            return "";
        }
        String content = metaTag.attr("content");
        return content != null ? content : "";
    }

    private int countH1Tags(Document document) {
        return document.select("h1").size();
    }

    private int countImagesMissingAlt(Document document) {
        Elements images = document.select("img");
        int count = 0;
        for (Element img : images) {
            String alt = img.attr("alt");
            if (alt == null || alt.isBlank()) {
                count++;
            }
        }
        return count;
    }

    private int countWords(Document document) {
        Element body = document.body();
        if (body == null) {
            return 0;
        }
        String text = body.text();
        if (text == null || text.isBlank()) {
            return 0;
        }
        return text.split("\\s+").length;
    }

    private HttpResponse<String> fetchPage(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(REQUEST_TIMEOUT)
                    .GET()
                    .build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new PageFetchException(url, e);
        }
    }
}

