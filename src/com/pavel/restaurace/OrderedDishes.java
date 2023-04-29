package com.pavel.restaurace;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class OrderedDishes {
    private String name;
    private int amount;
    private String waiter;
    private LocalTime orderedTime;
    private LocalTime fulfilledTime;
    private double price;
    private double totalPrice;

    public OrderedDishes(String name, int amount, double totalPrice, LocalTime orderedTime, LocalTime fulfilledTime) {
        this.name = name;
        this.amount = amount;
        this.totalPrice = totalPrice;
        this.orderedTime = orderedTime;
        this.fulfilledTime = fulfilledTime;
    }

    public OrderedDishes() {

    }
    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getWaiter() {
        return waiter;
    }

    public LocalTime getOrderedTime() {
        return orderedTime;
    }

    public LocalTime getFulfilledTime() {
        return fulfilledTime;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setFulfilledTime(LocalTime fulfilledTime) {
        this.fulfilledTime = fulfilledTime;
    }

    public void orderedDishes(Menu menu) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of the dish: ");
        String name = scanner.nextLine();

        Dish dish = menu.getDishByName(name);
        if (dish == null) {
            System.out.println("Error: the dish is not on the menu.");
            return;
        }

        this.name = dish.getTitle();


        System.out.print("Enter the amount: ");
        this.amount = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter the waiter: ");
        this.waiter = scanner.nextLine();

        System.out.print("Enter the ordered date and time (in format YYYY:MM:DD HH:mm): ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm");

        try {
            this.orderedTime = LocalDateTime.parse(scanner.nextLine(), formatter).toLocalTime();
        } catch (DateTimeParseException e) {
            System.out.println("Incorrect date format.");
            return;
        }


        this.totalPrice = dish.getPrice() * this.amount;
    }


}