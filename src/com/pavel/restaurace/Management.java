package com.pavel.restaurace;

import java.util.Scanner;

public class Management {
    private Orders orders;

    public Management(Orders orders) {
        this.orders = orders;
    }

    public void managementRun() {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        while (!input.equals("b")) {
            System.out.println("(a) to list active orders");
            System.out.println("(u) number of unfulfilled orders");
            System.out.println("(d) list active orders by ordered date");
            System.out.println("(w) list active orders ordered by waiter");
            System.out.println("(f) list all dishes ordered today");
            System.out.println("(p) list total price of orders for waiter");
            System.out.println("(b) to return to main menu");
            input = scanner.nextLine();
            if (input.equals("a")) {
                orders.listActiveOrders();
            } else if (input.equals("u")) {
                orders.listActiveUnfulfilledOrders();
            } else if (input.equals("d")) {
                orders.listActiveOrdersByOrderedTime();
            } else if (input.equals("w")) {
                orders.listActiveOrdersByWaiter();
            } else if (input.equals("f")) {
                orders.listDishesOrderedToday();
            } else if (input.equals("p")) {
                orders.listWaiterOrders();
            } else if (!input.equals("b")) {
                System.out.println("Invalid input, please try again.");
            }
        }

        System.out.println("Exiting order interface.");
    }
}