package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Getter
@Setter
@AllArgsConstructor
public class Service {
    private String name;
    private String healthURL;

    public String handleRequest(String request) throws Exception {
        // TODO: send request to specific service endpoint and return its response instead of returning String
        return "response for request: " + request;
    }

    public boolean healthCheck() {
        try {
            URL serviceUrl = new URL(healthURL);
            HttpURLConnection conn = (HttpURLConnection) serviceUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);

            int statusCode = conn.getResponseCode();
            if(statusCode == 200) {
                return true;
            } else {
                return false;
            }
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
