package com.example;

import com.example.client.MbtaApiClient;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {
        try {

            // Make API call and get the data
            MbtaApiClient mbtaApiClient = new MbtaApiClient();
            JsonObject apiResponse = mbtaApiClient.fetchPredictions();


            // Process the Data
            ApiDataProcessor apiDataProcessor = new ApiDataProcessor();
            TreeMap<String, StringBuilder> extractedTrainSchedules = apiDataProcessor.extractTrainSchedules(apiResponse);


            // Print the schedule
            TrainSchedulePrinter trainSchedulePrinter = new TrainSchedulePrinter();
            trainSchedulePrinter.printTrainSchedules(extractedTrainSchedules);


        } catch (IOException e) {
            System.out.println("Error fetching data from the MBTA API.");
            e.printStackTrace();
        }
    }
}
