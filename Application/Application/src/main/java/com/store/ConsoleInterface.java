package com.store;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Scanner;

public class ConsoleInterface {
    private static final String BASE_URL = "http://localhost:8081/api";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Global Dorm Console Interface ---");
            System.out.println("1. Search for Available Rooms");
            System.out.println("2. Apply for a Room");
            System.out.println("3. Cancel a Room Application");
            System.out.println("4. View History of Room Applications");
            System.out.println("5. Approve/Reject an Application");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> searchAvailableRooms();
                case 2 -> applyForRoom();
                case 3 -> cancelRoomApplication();
                case 4 -> viewApplicationHistory();
                case 5 -> changeApplicationStatus();
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void searchAvailableRooms() {
        System.out.println("Search by:");
        System.out.println("1. Price");
        System.out.println("2. Location");
        System.out.println("3. Language");
        System.out.print("Select an option: ");
        int searchChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String url = BASE_URL + "/rooms?availableOnly=true";

        switch (searchChoice) {
            case 1 -> {
                System.out.print("Enter maximum price: ");
                double maxPrice = scanner.nextDouble();
                url = BASE_URL + "/rooms/search/price?maxPrice=" + maxPrice + "&availableOnly=true";
            }
            case 2 -> {
                System.out.print("Enter location: ");
                String location = scanner.nextLine();
                url = BASE_URL + "/rooms/search/location?location=" + location + "&availableOnly=true";
            }
            case 3 -> {
                System.out.print("Enter language: ");
                String language = scanner.nextLine();
                url = BASE_URL + "/rooms/search/language?language=" + language + "&availableOnly=true";
            }
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        String response = restTemplate.getForObject(url, String.class);
        System.out.println("Search Results: " + response);
    }

    private static void applyForRoom() {
        System.out.print("Enter your User ID: ");
        int userId = scanner.nextInt();
        System.out.print("Enter Room ID: ");
        int roomId = scanner.nextInt();

        String url = BASE_URL + "/applications/apply?userId=" + userId + "&roomId=" + roomId;
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);

        System.out.println("Application Response: " + response.getBody());
    }

    private static void cancelRoomApplication() {
        System.out.print("Enter Application ID: ");
        int applicationId = scanner.nextInt();
        System.out.print("Enter your User ID: ");
        int userId = scanner.nextInt();

        String url = BASE_URL + "/applications/" + applicationId + "/cancel?userId=" + userId;
        restTemplate.put(url, null);
        System.out.println("Application cancelled successfully.");
    }

    private static void viewApplicationHistory() {
        System.out.print("Enter your User ID: ");
        int userId = scanner.nextInt();

        String url = BASE_URL + "/applications/user/" + userId;
        String response = restTemplate.getForObject(url, String.class);

        System.out.println("Application History: " + response);
    }

    private static void changeApplicationStatus() {
        System.out.print("Enter Application ID: ");
        int applicationId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter status (Approved/Rejected): ");
        String status = scanner.nextLine();

        String url = BASE_URL + "/applications/" + applicationId + "/status";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"status\": \"" + status + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        restTemplate.put(url, entity);
        System.out.println("Application status updated to " + status + ".");
    }
}
