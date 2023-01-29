package org.example;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Service {
    private String name;
    private String healthURL;

    public Service(String name, String healthURL) {
        this.name = name;
        this.healthURL = healthURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHealthURL() {
        return healthURL;
    }

    public void setHealthURL(String healthURL) {
        this.healthURL = healthURL;
    }

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
