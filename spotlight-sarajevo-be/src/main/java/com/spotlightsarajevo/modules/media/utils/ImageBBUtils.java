package com.spotlightsarajevo.modules.media.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for ImageBB operations such as deleting images.
 */
@Slf4j
@Component
public class ImageBBUtils {

    @Value("${imagebb.api.key}")
    private String apiKey;

    private final HttpClient httpClient;

    public ImageBBUtils() {
        // Create an HTTP client that handles cookies (required for CSRF)
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .cookieHandler(cookieManager)
                .build();
    }

    /**
     * Deletes an image from ImageBB using the delete URL.
     * ImageBB delete URLs are in format: https://ibb.co/delete/IMAGE_ID/DELETE_HASH
     * This method performs a two-step process:
     * 1. GET the delete page to obtain CSRF token and establish session
     * 2. POST to confirm deletion
     *
     * @param deleteUrl The delete URL provided by ImageBB when the image was uploaded
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteImage(String deleteUrl) {
        if (deleteUrl == null || deleteUrl.isBlank()) {
            log.warn("Delete URL is null or empty, skipping ImageBB deletion");
            return false;
        }

        try {
            // Step 1: GET the delete confirmation page
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(URI.create(deleteUrl))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .GET()
                    .build();

            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            String pageContent = getResponse.body();

            log.debug("Delete page status: {}", getResponse.statusCode());

            // Check if the image was already deleted
            if (pageContent.contains("doesn't exist") || pageContent.contains("has been deleted") || pageContent.contains("not found")) {
                log.info("Image already deleted or doesn't exist on ImageBB: {}", deleteUrl);
                return true;
            }

            // Step 2: Extract auth_token from the page and POST to confirm deletion
            String authToken = extractAuthToken(pageContent);

            if (authToken != null) {
                // Build form data for POST request
                String formData = "auth_token=" + authToken + "&confirm-deletion=1";

                HttpRequest postRequest = HttpRequest.newBuilder()
                        .uri(URI.create(deleteUrl))
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .header("Referer", deleteUrl)
                        .POST(HttpRequest.BodyPublishers.ofString(formData))
                        .build();

                HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
                String postContent = postResponse.body();

                if (postResponse.statusCode() == 200 &&
                    (postContent.contains("deleted") || postContent.contains("success") || postContent.contains("has been deleted"))) {
                    log.info("Successfully deleted image from ImageBB: {}", deleteUrl);
                    return true;
                }
            }

            // Fallback: Try with simple confirm parameter
            String confirmUrl = deleteUrl + (deleteUrl.contains("?") ? "&" : "?") + "confirm=true";
            HttpRequest confirmRequest = HttpRequest.newBuilder()
                    .uri(URI.create(confirmUrl))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .GET()
                    .build();

            HttpResponse<String> confirmResponse = httpClient.send(confirmRequest, HttpResponse.BodyHandlers.ofString());

            if (confirmResponse.statusCode() == 200) {
                log.info("ImageBB deletion request sent (status 200): {}", deleteUrl);
                return true;
            }

            log.warn("Failed to delete image from ImageBB. Status: {}, URL: {}", confirmResponse.statusCode(), deleteUrl);
            return false;

        } catch (Exception e) {
            log.error("Error deleting image from ImageBB: {} - {}", deleteUrl, e.getMessage());
            return false;
        }
    }

    /**
     * Extracts the auth_token from the ImageBB delete confirmation page.
     */
    private String extractAuthToken(String pageContent) {
        // Pattern to find auth_token in hidden input field
        Pattern pattern = Pattern.compile("name=\"auth_token\"\\s+value=\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pageContent);

        if (matcher.find()) {
            return matcher.group(1);
        }

        // Alternative pattern
        Pattern altPattern = Pattern.compile("auth_token[\"']?\\s*[=:]\\s*[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
        Matcher altMatcher = altPattern.matcher(pageContent);

        if (altMatcher.find()) {
            return altMatcher.group(1);
        }

        log.debug("Could not extract auth_token from ImageBB delete page");
        return null;
    }
}

