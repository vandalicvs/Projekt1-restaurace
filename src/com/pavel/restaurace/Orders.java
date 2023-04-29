package com.pavel.restaurace;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class Orders {
    private ArrayList<Table> activeOrders;
    private ArrayList<Table> pastOrders;

    public Orders() {
        this.activeOrders = new ArrayList<>();
        this.pastOrders = new ArrayList<>();
    }

    public void ordersRun(Menu menu) {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        while (!input.equals("b")) {

            System.out.println("(a) to add an order");
            System.out.println("(c) to change an order");
            System.out.println("(l) to list active orders");
            System.out.println("(e) export active orders");
            System.out.println("(i) import active orders");
            System.out.println("(p) export past orders");
            System.out.println("(b) to return to main menu");

            input = scanner.nextLine();

            if (input.equals("a")) {
                System.out.println("Enter the table number:");
                int tableNumber = scanner.nextInt();
                scanner.nextLine();

                boolean tableFound = false;
                for (Table table : activeOrders) {
                    if (table.getNumber() == tableNumber) {
                        System.out.println("Error: the table is already in the active orders.");
                        tableFound = true;
                        break;
                    }
                }

                if (!tableFound) {
                    try {
                        Table table = new Table(tableNumber);
                        OrderedDishes orderedDishes = new OrderedDishes();
                        addOrderedDishes(menu, orderedDishes);
                        table.addOrderedDishes(orderedDishes);

                        activeOrders.add(table);
                        System.out.println("Table " + table.getNumber() + " added to active orders.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }


                }
            } else if (input.equals("c")) {
                System.out.println("Enter the table number to adjust the order:");
                int tableNumber = scanner.nextInt();
                scanner.nextLine();

                Table tableToAdjust = null;
                for (Table table : activeOrders) {
                    if (table.getNumber() == tableNumber) {
                        tableToAdjust = table;
                        break;
                    }
                }

                if (tableToAdjust != null) {
                    System.out.println("(a) add another order to the table");
                    System.out.println("(f) fulfill order");
                    System.out.println("(n) add note to the table");
                    System.out.println("(p) close order and archive it");
                    System.out.println("(b) back");

                    input = scanner.nextLine();

                    if (input.equals("a")) {
                        try {
                            OrderedDishes orderedDishes = new OrderedDishes();
                            addOrderedDishes(menu, orderedDishes);
                            tableToAdjust.addOrderedDishes(orderedDishes);
                            System.out.println("Order added to table " + tableToAdjust.getNumber());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else if (input.equals("f")) {
                        fulfilmentOrder(tableNumber);
                    } else if (input.equals("p")) {
                        moveTableToPastOrders(tableNumber);
                    } else if (input.equals("n")) {
                        System.out.println("Enter the note to add:");
                        String note = scanner.nextLine();
                        tableToAdjust.setNote(note);
                        System.out.println("Note added to table " + tableToAdjust.getNumber());
                    } else if (!input.equals("b")) {
                        System.out.println("Invalid input, please try again.");
                    }
                } else {
                    System.out.println("Error: the table was not found in the active orders.");
                }
            } else if (input.equals("l")) {
                listActiveOrders();
            } else if (input.equals("q")) {
                listPastOrders();
            } else if (input.equals("e")) {
                exportActiveOrdersToFile();
            } else if (input.equals("p")) {
                exportPastOrdersToFile();
            } else if (input.equals("i")) {
                importActiveOrdersFromFile("Active_orders.txt");
            } else if (!input.equals("b")) {
                System.out.println("Invalid input, please try again.");
            }
        }
        System.out.println("Exiting order interface.");
    }

    private void addOrderedDishes(Menu menu, OrderedDishes orderedDishes) throws IllegalArgumentException {
        orderedDishes.orderedDishes(menu);
        if (orderedDishes.getName() == null) {
            throw new IllegalArgumentException("The dish is not on the menu.");
        }
    }


    public void listActiveOrders() {
        System.out.println("Active Orders:");
        for (Table table : activeOrders) {
            System.out.println("** orders for table no. " + table.getNumber() + " **");
            System.out.println("****");

            int orderNumber = 1;
            for (OrderedDishes orderedDishes : table.getOrderedDishesList()) {
                String fulfilledTime = orderedDishes.getFulfilledTime() != null
                        ? orderedDishes.getFulfilledTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                        : "TBD";
                System.out.printf("%d. %s %d (%.2f Kč): %s-%s\tčíšník č. %s\n",
                        orderNumber, orderedDishes.getName(), orderedDishes.getAmount(), orderedDishes.getTotalPrice(),
                        orderedDishes.getOrderedTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                        fulfilledTime, orderedDishes.getWaiter());
                orderNumber++;
            }
        }
    }

    public List<OrderedDishes> getOrderedDishesByTableNumber(int tableNumber) {
        List<OrderedDishes> orderedDishesList = new ArrayList<>();

        for (Table table : activeOrders) {
            if (table.getNumber() == tableNumber) {
                orderedDishesList.addAll(table.getOrderedDishesList());
                break;
            }
        }

        return orderedDishesList;
    }

    public void fulfilmentOrder(int tableNumber) {
        Scanner scanner = new Scanner(System.in);
        List<OrderedDishes> orderedDishes = getOrderedDishesByTableNumber(tableNumber);
        if (orderedDishes.isEmpty()) {
            System.out.println("No ordered dishes found for table " + tableNumber);
            return;
        }

        System.out.println("Ordered dishes for table " + tableNumber + ":");
        for (int i = 0; i < orderedDishes.size(); i++) {
            OrderedDishes orderedDish = orderedDishes.get(i);
            System.out.println((i + 1) + ". " + orderedDish.getName() + " (x" + orderedDish.getAmount() + ")");
        }

        System.out.println("Please select a dish to mark as fulfilled:");
        int selection = scanner.nextInt();
        scanner.nextLine();
        if (selection < 1 || selection > orderedDishes.size()) {
            System.out.println("Invalid selection");
            return;
        }

        System.out.println("Enter the time the dish was fulfilled (hh:mm):");
        String fulfilledTimeStr = scanner.nextLine();
        LocalTime fulfilledTime;
        try {
            fulfilledTime = LocalTime.parse(fulfilledTimeStr);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format. Time should be in hh:mm format");
            return;
        }

        OrderedDishes selectedDish = orderedDishes.get(selection - 1);
        selectedDish.setFulfilledTime(fulfilledTime);
        System.out.println(selectedDish.getName() + " (x" + selectedDish.getAmount() + ") marked as fulfilled at " + selectedDish.getFulfilledTime());

    }

    public void moveTableToPastOrders(int tableNumber) {
        for (Table table : activeOrders) {
            if (table.getNumber() == tableNumber) {
                for (OrderedDishes orderedDishes : table.getOrderedDishesList()) {
                    if (orderedDishes.getFulfilledTime() == null) {
                        orderedDishes.setFulfilledTime(LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))));
                    }
                }
                pastOrders.add(table);
                activeOrders.remove(table);
                return;
            }
        }
    }

    public void listPastOrders() {
        System.out.println("Archived Orders:");
        for (Table table : pastOrders) {
            System.out.println("** orders for table no. " + table.getNumber() + " **");
            System.out.println("****");

            int orderNumber = 1;
            for (OrderedDishes orderedDishes : table.getOrderedDishesList()) {
                String fulfilledTime = orderedDishes.getFulfilledTime() != null
                        ? orderedDishes.getFulfilledTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                        : "TBD";
                System.out.printf("%d. %s %d (%.2f Kč): %s-%s\tčíšník č. %s\n",
                        orderNumber, orderedDishes.getName(), orderedDishes.getAmount(), orderedDishes.getTotalPrice(),
                        orderedDishes.getOrderedTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                        fulfilledTime, orderedDishes.getWaiter());
                orderNumber++;
            }
        }
    }

    public void exportActiveOrdersToFile() {
        try {
            FileWriter writer = new FileWriter("Active_orders.txt");
            for (Table table : activeOrders) {
                writer.write(String.format("Table %d:\n", table.getNumber()));
                for (OrderedDishes orderedDishes : table.getOrderedDishesList()) {
                    writer.write(String.format("%s,%d,%s,%s,%s\n",
                            orderedDishes.getName(),
                            orderedDishes.getAmount(),
                            orderedDishes.getTotalPrice(),
                            orderedDishes.getOrderedTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                            orderedDishes.getFulfilledTime() != null
                                    ? orderedDishes.getFulfilledTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                                    : "Not fulfilled yet"));
                }
                writer.write("\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error exporting active orders to file: " + e.getMessage());
        }
    }

    public void importActiveOrdersFromFile(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            ArrayList<Table> newActiveOrders = new ArrayList<>();
            Table currentTable = null;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Table ")) {
                    int tableNumber = Integer.parseInt(line.substring(6, line.length() - 1));
                    currentTable = new Table(tableNumber);
                    newActiveOrders.add(currentTable);
                } else if (line.trim().length() > 0) {
                    String[] fields = line.split(",");
                    String name = fields[0];
                    int amount = Integer.parseInt(fields[1]);
                    double totalPrice = Double.parseDouble(fields[2]);
                    LocalTime orderedTime = LocalTime.parse(fields[3], DateTimeFormatter.ofPattern("HH:mm"));
                    LocalTime fulfilledTime = null;
                    if (!fields[4].equals("Not fulfilled yet")) {
                        fulfilledTime = LocalTime.parse(fields[4], DateTimeFormatter.ofPattern("HH:mm"));
                    }
                    OrderedDishes orderedDishes = new OrderedDishes(name, amount, totalPrice, orderedTime, fulfilledTime);
                    currentTable.addOrderedDishes(orderedDishes);
                }
            }
            activeOrders = newActiveOrders;
            scanner.close();
        } catch (IOException e) {
            System.err.println("Error importing active orders from file: " + e.getMessage());
        }
    }

    public void exportPastOrdersToFile() {
        try {
            FileWriter writer = new FileWriter("archived_orders.txt", true); // set second argument to true for append mode
            for (Table table : pastOrders) {
                writer.write(String.format("Table %d:\n", table.getNumber()));
                for (OrderedDishes orderedDishes : table.getOrderedDishesList()) {
                    writer.write(String.format("%s,%d,%s,%s,%s\n",
                            orderedDishes.getName(),
                            orderedDishes.getAmount(),
                            orderedDishes.getTotalPrice(),
                            orderedDishes.getOrderedTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                            orderedDishes.getFulfilledTime() != null
                                    ? orderedDishes.getFulfilledTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                                    : "Not fulfilled yet"));
                }
                writer.write("\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error exporting past orders to file: " + e.getMessage());
        }
    }

    public void listActiveUnfulfilledOrders() {
        System.out.println("Unfulfilled Orders:");
        int unfulfilledOrdersCount = 0;
        for (Table table : activeOrders) {
            System.out.println("** orders for table no. " + table.getNumber() + " **");
            System.out.println("****");

            int orderNumber = 1;
            for (OrderedDishes orderedDishes : table.getOrderedDishesList()) {
                if (orderedDishes.getFulfilledTime() == null) {
                    orderNumber++;
                    unfulfilledOrdersCount++;
                }
            }
        }
        System.out.println("Total unfulfilled orders: " + unfulfilledOrdersCount);
    }

    public void listActiveOrdersByOrderedTime() {
        System.out.println("Active Orders (Ordered by Ordered Time):");
        List<OrderedDishes> orderedDishesList = new ArrayList<>();
        for (Table table : activeOrders) {
            orderedDishesList.addAll(table.getOrderedDishesList());
        }

        orderedDishesList.stream()
                .filter(orderedDishes -> orderedDishes.getFulfilledTime() == null)
                .sorted(Comparator.comparing(OrderedDishes::getOrderedTime))
                .forEach(orderedDishes -> {
                    String fulfilledTime = orderedDishes.getFulfilledTime() != null
                            ? orderedDishes.getFulfilledTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                            : "TBD";
                    System.out.printf("%s %d (%.2f Kč): %s-%s\tčíšník č. %s\n",
                            orderedDishes.getName(), orderedDishes.getAmount(), orderedDishes.getTotalPrice(),
                            orderedDishes.getOrderedTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                            fulfilledTime, orderedDishes.getWaiter());
                });
    }

    public void listActiveOrdersByWaiter() {
        System.out.println("Active Orders (Ordered by Waiter):");
        List<OrderedDishes> orderedDishesList = new ArrayList<>();
        for (Table table : activeOrders) {
            orderedDishesList.addAll(table.getOrderedDishesList());
        }

        orderedDishesList.stream()
                .filter(orderedDishes -> orderedDishes.getFulfilledTime() == null)
                .sorted(Comparator.comparing(OrderedDishes::getWaiter))
                .forEach(orderedDishes -> {
                    String fulfilledTime = orderedDishes.getFulfilledTime() != null
                            ? orderedDishes.getFulfilledTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                            : "TBD";
                    System.out.printf("%s %d (%.2f Kč): %s-%s\tčíšník č. %s\n",
                            orderedDishes.getName(), orderedDishes.getAmount(), orderedDishes.getTotalPrice(),
                            orderedDishes.getOrderedTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                            fulfilledTime, orderedDishes.getWaiter());
                });
    }

    public void listDishesOrderedToday() {
        LocalDate today = LocalDate.now();
        System.out.println("Dishes ordered on " + today.format(DateTimeFormatter.ISO_LOCAL_DATE) + ":");

        Map<String, Integer> dishCounts = new HashMap<>();

        for (Table table : activeOrders) {
            for (OrderedDishes orderedDishes : table.getOrderedDishesList()) {
                LocalDateTime orderedDateTime = LocalDateTime.of(LocalDate.now(), orderedDishes.getOrderedTime());
                if (orderedDateTime.toLocalDate().equals(today)) {
                    String dishName = orderedDishes.getName();
                    dishCounts.put(dishName, dishCounts.getOrDefault(dishName, 0) + 1);
                }
            }
        }
        for (Table table : pastOrders) {
            for (OrderedDishes orderedDishes : table.getOrderedDishesList()) {
                LocalDateTime orderedDateTime = LocalDateTime.of(LocalDate.now(), orderedDishes.getOrderedTime());
                if (orderedDateTime.toLocalDate().equals(today)) {
                    String dishName = orderedDishes.getName();
                    dishCounts.put(dishName, dishCounts.getOrDefault(dishName, 0) + 1);
                }
            }
        }

        for (String dishName : dishCounts.keySet()) {
            System.out.println(dishName + ": " + dishCounts.get(dishName));
        }
    }

    public void listWaiterOrders() {
        System.out.println("Waiter orders:");
        Map<String, Double> waiterOrderTotals = new HashMap<>();
        Map<String, Integer> waiterOrderCounts = new HashMap<>();

        for (Table table : activeOrders) {
            for (OrderedDishes orderedDishes : table.getOrderedDishesList()) {
                String waiter = orderedDishes.getWaiter();
                double totalPrice = orderedDishes.getTotalPrice();
                waiterOrderTotals.merge(waiter, totalPrice, Double::sum);
                waiterOrderCounts.merge(waiter, 1, Integer::sum);
            }
        }
        for (Table table : pastOrders) {
            for (OrderedDishes orderedDishes : table.getOrderedDishesList()) {
                String waiter = orderedDishes.getWaiter();
                double totalPrice = orderedDishes.getTotalPrice();
                waiterOrderTotals.merge(waiter, totalPrice, Double::sum);
                waiterOrderCounts.merge(waiter, 1, Integer::sum);
            }
        }

        for (Map.Entry<String, Double> entry : waiterOrderTotals.entrySet()) {
            String waiter = entry.getKey();
            double totalPrice = entry.getValue();
            int totalOrders = waiterOrderCounts.get(waiter);
            System.out.println(waiter + ": " + totalPrice + ", Orders: " + totalOrders);
        }
    }


    public void calculateAverageTime() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the starting date and time in format yyyy-MM-dd HH:mm:ss:");
        LocalDateTime startDateTime = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println("Please enter the finishing date and time in format yyyy-MM-dd HH:mm:ss:");
        LocalDateTime finishDateTime = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        long totalTimeInMinutes = 0;
        long dishCount = 0;

        for (Table table : activeOrders) {
            for (OrderedDishes orderedDishes : table.getOrderedDishesList()) {
                LocalDateTime fulfilledTime = LocalDateTime.from(orderedDishes.getFulfilledTime());
                if (fulfilledTime != null && fulfilledTime.isAfter(startDateTime) && fulfilledTime.isBefore(finishDateTime)) {
                    long timeInMinutes = ChronoUnit.MINUTES.between(orderedDishes.getOrderedTime(), fulfilledTime);
                    totalTimeInMinutes += timeInMinutes;
                    dishCount++;
                }
            }
        }
        for (Table table : pastOrders) {
            for (OrderedDishes orderedDishes : table.getOrderedDishesList()) {
                LocalDateTime fulfilledTime = LocalDateTime.from(orderedDishes.getFulfilledTime());
                if (fulfilledTime != null && fulfilledTime.isAfter(startDateTime) && fulfilledTime.isBefore(finishDateTime)) {
                    long timeInMinutes = ChronoUnit.MINUTES.between(orderedDishes.getOrderedTime(), fulfilledTime);
                    totalTimeInMinutes += timeInMinutes;
                    dishCount++;
                }
            }
        }

        if (dishCount > 0) {
            double averageTimeInMinutes = (double) totalTimeInMinutes / dishCount;
            System.out.printf("Average time between order and fulfillment: %.2f minutes\n", averageTimeInMinutes);
        } else {
            System.out.println("No dishes found within the specified time frame.");
        }
    }
}