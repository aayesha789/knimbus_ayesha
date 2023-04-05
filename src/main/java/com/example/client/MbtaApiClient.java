package com.example.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * MbtaApiClient is a class responsible for fetching data from the MBTA API.
 */
public class MbtaApiClient {

    private static final String MBTA_API_URL =
            "https://api-v3.mbta.com/predictions/?filter[stop]=place-pktrm&sort=departure_time&include=route";

    /**
     * Fetches predictions data from the MBTA API.
     *
     * @return JsonObject containing the API response.
     * @throws IOException if an error occurs while fetching data from the API.
     */
    public JsonObject fetchPredictions() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(MBTA_API_URL).build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            return new Gson().fromJson(responseBody, JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
