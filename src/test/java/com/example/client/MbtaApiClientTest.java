package com.example.client;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MbtaApiClientTest {

    private static MbtaApiClient mbtaApiClient;

    @BeforeAll
    public static void setUp() {
        mbtaApiClient = new MbtaApiClient();
    }

    @Test
    public void testFetchPredictions() {
        try {
            JsonObject predictions = mbtaApiClient.fetchPredictions();
            assertNotNull(predictions, "Predictions should not be null");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
