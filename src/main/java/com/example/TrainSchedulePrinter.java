package com.example;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class responsible for printing the train schedules in a formatted way.
 */
class TrainSchedulePrinter {

    public void printTrainSchedules(TreeMap<String, StringBuilder> trainsByLine) {
        // Print the train schedules
        for (Map.Entry<String, StringBuilder> entry : trainsByLine.entrySet()) {
            System.out.println("\n----" + entry.getKey() + "----");
            System.out.print(entry.getValue());
        }
    }
}