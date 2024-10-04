package ca.bc.gov.old.router.testing.web.utilities;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RouterApiClient {

    private final HttpClient httpClient;

    // Constructor to initialize the HttpClient
    public RouterApiClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    // Method to perform a GET request
    public String get(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(url))
            .header("Accept", "application/json")
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed: HTTP error code: " + response.statusCode());
        }

        return response.body();
    }

}
