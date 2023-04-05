package com.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

/**
 * Class responsible for processing the JSON response from the MBTA API and extracting relevant information such as train schedules.
 */
public class ApiDataProcessor {

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final String DATA = "data";
    private static final String INCLUDED = "included";
    private static final String ID = "id";
    public static final String AMERICA_NEW_YORK = "America/New_York";
    private static final String RELATIONSHIPS = "relationships";
    private static final String ROUTE = "route";
    private static final String ATTRIBUTES = "attributes";
    private static final String LONG_NAME = "long_name";
    private static final String DIRECTION_ID = "direction_id";
    private static final String DIRECTION_DESTINATIONS = "direction_destinations";
    private static final String DEPARTURE_TIME = "departure_time";

    public TreeMap<String, StringBuilder> extractTrainSchedules(JsonObject jsonResponse) {
        // Extract relevant information from the API response
        JsonArray predictions = jsonResponse.getAsJsonArray(DATA);
        JsonObject included = new JsonObject();
        for (JsonElement element : jsonResponse.getAsJsonArray(INCLUDED)) {
            JsonObject routeObject = element.getAsJsonObject();
            String routeId = routeObject.get(ID).getAsString();
            included.add(routeId, routeObject);
        }

        // Get the current time and print it
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of(AMERICA_NEW_YORK));
        System.out.println("Current Time: " + currentTime.format(TIME_FORMATTER));

        // Extract train schedules from the JSON response
        TreeMap<String, StringBuilder> trainsByLine = new TreeMap<>();
        int trainCount = 0;

        for (JsonElement element : predictions) {
            if (trainCount >= 10) {
                break;
            }
            JsonObject predictionObject = element.getAsJsonObject();
            String routeId = predictionObject.getAsJsonObject(RELATIONSHIPS).getAsJsonObject(ROUTE).getAsJsonObject(DATA).get(ID).getAsString();
            JsonObject routeObject = included.getAsJsonObject(routeId);

            String routeName = routeObject.get(ATTRIBUTES).getAsJsonObject().get(LONG_NAME).getAsString();
            String destination = predictionObject.get(ATTRIBUTES).getAsJsonObject().get(DIRECTION_ID).getAsInt() == 0
                    ? routeObject.get(ATTRIBUTES).getAsJsonObject().get(DIRECTION_DESTINATIONS).getAsJsonArray().get(0).getAsString()
                    : routeObject.get(ATTRIBUTES).getAsJsonObject().get(DIRECTION_DESTINATIONS).getAsJsonArray().get(1).getAsString();

            LocalDateTime departureTime = LocalDateTime.parse(predictionObject.get(ATTRIBUTES)
                    .getAsJsonObject().get(DEPARTURE_TIME).getAsString(), DATE_TIME_FORMATTER);
            long minutesUntilDeparture = java.time.Duration.between(currentTime, departureTime).toMinutes();

            if (minutesUntilDeparture < 0) {
                continue;
            }

            StringBuilder trainInfo = new StringBuilder();
            trainInfo.append(destination).append(": Departing in ").append(minutesUntilDeparture).append(" minutes\n");

            trainsByLine.putIfAbsent(routeName, new StringBuilder());
            trainsByLine.get(routeName).append(trainInfo);

            trainCount++;
        }

        return trainsByLine;
    }
}
