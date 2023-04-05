package com.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ApiDataProcessorTest {

    private static final String AMERICA_NEW_YORK = "America/New_York";

    private static final String TEST_JSON_RESPONSE = "{\n" +
            "  \"data\": [\n" +
            "    {\n" +
            "      \"id\": \"111\",\n" +
            "      \"type\": \"predictions\",\n" +
            "      \"attributes\": {\n" +
            "        \"departure_time\": \"2023-04-05T12:00:00-04:00\",\n" +
            "        \"direction_id\": 0\n" +
            "      },\n" +
            "      \"relationships\": {\n" +
            "        \"route\": {\n" +
            "          \"data\": {\n" +
            "            \"id\": \"222\",\n" +
            "            \"type\": \"routes\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"included\": [\n" +
            "    {\n" +
            "      \"id\": \"222\",\n" +
            "      \"type\": \"routes\",\n" +
            "      \"attributes\": {\n" +
            "        \"long_name\": \"Test Route\",\n" +
            "        \"direction_destinations\": [\"Destination 1\", \"Destination 2\"]\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Mock
    private JsonObject mockJsonResponse;

    @InjectMocks
    private ApiDataProcessor apiDataProcessor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExtractTrainSchedules() {
        // Set up mock JSON response
        Gson gson = new Gson();
        JsonObject predictionObject = gson.fromJson(TEST_JSON_RESPONSE, JsonObject.class);
        when(mockJsonResponse.getAsJsonArray("data")).thenReturn(predictionObject.getAsJsonArray("data"));
        when(mockJsonResponse.getAsJsonArray("included")).thenReturn(predictionObject.getAsJsonArray("included"));

        // Set up mock current time
        LocalDateTime mockCurrentTime = LocalDateTime.parse("2023-04-05T11:55:00-04:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        when(LocalDateTime.now(ZoneId.of(AMERICA_NEW_YORK))).thenReturn(mockCurrentTime);

        // Call the method under test
        TreeMap<String, StringBuilder> result = apiDataProcessor.extractTrainSchedules(mockJsonResponse);

        // Verify the result
        assertEquals(1, result.size());
        assertEquals("Test Route", result.firstKey());
        assertEquals("Destination 1: Departing in 5 minutes\n", result.get("Test Route").toString());
    }
}
