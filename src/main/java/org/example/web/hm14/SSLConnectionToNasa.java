package org.example.web.hm14;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SSLConnectionToNasa {
    public static final String URL = "/mars-photos/api/v1/rovers/curiosity/photos?sol=16&api_key=DEMO_KEY";
    public static final String EMPTY = "";

    public static void main(String[] args) {
        var response = sendRequest();
        var imgs = parseResponseBody(response);

        var imgWithMaxSize = findImgWithMaxSize(imgs);

        System.out.println("img: " + imgWithMaxSize.getKey());
        System.out.println("size: " + imgWithMaxSize.getValue());
    }


    @SneakyThrows
    private static Map.Entry<URI, Long> findImgWithMaxSize(List<URI> imgs) {
        var httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        var maxOptional = imgs.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(img -> img, img -> getContentLength(httpClient, img)),
                        map -> map.entrySet().stream().max(Map.Entry.comparingByValue()))
                );

        return maxOptional.orElseThrow();
    }

    @SneakyThrows
    private static Long getContentLength(HttpClient client, URI uri) {
        var httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .build();
        var imgResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return Long.valueOf(imgResponse.headers().firstValue("Content-length").get());
    }

    @SneakyThrows
    private static List<URI> parseResponseBody(String body) {
        var mapper = new ObjectMapper();
        var urls = mapper.readTree(body).get("photos").findValuesAsText("img_src");
        return urls.stream()
                .map(URI::create)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private static String sendRequest() {
        var sslSocketFactory = SSLSocketFactory.getDefault();
        try (var sslSocket = (SSLSocket) sslSocketFactory.createSocket("api.nasa.gov", 443)) {
            var writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
            writer.write("GET " + URL + " HTTP/1.1\n");
            writer.write("Host: api.nasa.gov\n");
            writer.write("\n");
            writer.flush();

            var response = new StringBuilder();
            var startBody = false;
            try (var reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()))) {
                while (true) {
                    var line = reader.readLine();
                    if (line.equals(EMPTY)) { // end of headers
                        startBody = true;
                        continue;
                    }
                    if (line.equals("0")) { // end of response
                        break;
                    }
                    if (!startBody || isHexadecimal(line)) {
                        continue;
                    }
                    response.append(line);
                }
            }

            return response.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static boolean isHexadecimal(String line) {
        return line.matches("^[0-9a-fA-F]+$");
    }
}
