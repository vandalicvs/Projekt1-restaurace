package com.pavel.restaurace;
import java.util.ArrayList;
import java.util.Scanner;

public class Dish {
    private String title;
    private double price;
    private int prepTime;
    private ArrayList<String> photoURLs;
    private String category;

    // Constructor
    public Dish(String title, double price, int prepTime, ArrayList<String> photoURLs, String category) {
        this.title = title;
        this.price = price;
        this.prepTime = prepTime;
        this.photoURLs = photoURLs;
        this.category = category;
    }

    // Default constructor
    public Dish() {
        this.title = "";
        this.price = 0.0;
        this.prepTime = 0;
        this.photoURLs = new ArrayList<>();
        this.category = "";
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public ArrayList<String> getPhotoURLs() {
        return photoURLs;
    }

    public String getCategory() {
        return category;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public void setPhotoURLs(ArrayList<String> photoURLs) {
        this.photoURLs = photoURLs;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Add and remove photos
    public void addPhotoURL(String photoURL) {
        this.photoURLs.add(photoURL);
    }

    public void removePhotoURL(String photoURL) {
        this.photoURLs.remove(photoURL);
    }

    // Check if dish has at least one photo
    public boolean hasPhoto() {
        return !photoURLs.isEmpty();
    }

    // Default photo if no photos are present
    public String getDefaultPhoto() {
        return "blank";
    }

    // Method for manual adding dishes
    public static Dish addDish() {
        Scanner scanner = new Scanner(System.in);

        // Ask for dish title
        System.out.print("Enter dish title: ");
        String title = scanner.nextLine();

        // Ask for dish price
        System.out.print("Enter dish price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        // Ask for preparation time
        System.out.print("Enter preparation time (in minutes): ");
        int prepTime = scanner.nextInt();
        scanner.nextLine();

        // Ask for photo URLs
        ArrayList<String> photoURLs = new ArrayList<String>();
        while (true) {
            System.out.print("Enter photo URL (or type 'skip' to skip): ");
            String photoURL = scanner.nextLine();
            if (photoURL.equals("skip")) {
                break;
            }
            photoURLs.add(photoURL);
        }

        // Ask for dish category
        String category = "";
        while (true) {
            System.out.print("Enter dish category (Předkrm/Polévka/Hlavní chod/Dezert): ");
            category = scanner.nextLine();
            if (category.equals("Předkrm") || category.equals("Polévka") || category.equals("Hlavní chod") || category.equals("Dezert")) {
                break;
            }
            System.out.println("Invalid category, please try again.");
        }

        // Create new dish object
        Dish newDish = new Dish(title, price, prepTime, photoURLs, category);

        return newDish;
    }
    public static void listAllDishes(ArrayList<Dish> dishes) {
        if (dishes.isEmpty()) {
            System.out.println("There are no dishes to display.");
        } else {
            System.out.println("List of dishes:");
            for (Dish dish : dishes) {
                System.out.println("- " + dish.getTitle());
            }
        }
    }
}
