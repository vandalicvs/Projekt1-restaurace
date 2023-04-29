package com.pavel.restaurace;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Cook {
    private Menu menu;
    private DishList dishList;

    public Cook(Menu menu, DishList dishList) {
        this.menu = menu;
        this.dishList = dishList;
    }

    public void cookRun() {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        while (!input.equals("b")) {
            options();

            input = scanner.nextLine();
            if (input.equals("a")) {
                Dish newDish = Dish.addDish();
                dishList.addDish(newDish);
                System.out.println("New dish added: " + newDish.getTitle());
            } else if (input.equals("m")) {
                this.menu.listDishes();
            } else if (input.equals("l")) {
                System.out.println("List of all dishes:");
                for (Dish dish : dishList.getAllDishes()) {
                    System.out.println(dish.getTitle());
                }
            } else if (input.equals("e")) {
                this.exportMenu();
            } else if (input.equals("i")) {
                this.importMenu();
            } else if (input.equals("x")) {
                exportMenu();
            } else if (input.equals("n")) {
                System.out.println("Enter the name of the dish to add to the menu:");
                String dishName = scanner.nextLine();
                boolean found = false;
                for (Dish dish : dishList.getAllDishes()) {
                    if (dish.getTitle().equals(dishName)) {
                        found = true;
                        if (menu.getDishes().contains(dish)) {
                            System.out.println("This dish is already on the menu.");
                        } else {
                            menu.addDish(dish);
                            System.out.println("Dish added to menu: " + dish.getTitle());
                        }
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Dish not found in available dishes.");
                }
            } else if (input.equals("r")) {
                System.out.println("Enter the name of the dish to remove:");
                String dishName = scanner.nextLine();
                boolean removed = false;
                for (Dish dish : menu.getDishes()) {
                    if (dish.getTitle().equals(dishName)) {
                        menu.removeDish(dish);
                        removed = true;
                        System.out.println("Dish removed successfully.");
                        break;
                    }
                }
                if (!removed) {
                    System.out.println("Dish not found.");
                }

            } else if (input.equals("c")) {
                this.menu.removeAllDishes();
                System.out.println("All dishes removed successfully.");
            } else if (!input.equals("b")) {
                System.out.println("Invalid input, please try again.");
            }
        }

        System.out.println("Exiting cook interface.");
    }

    public void options() {
        System.out.println("(a) add new dish to list of dishes");
        System.out.println("(l) list all available dishes");
        System.out.println("(e) export all available dishes to txt");
        System.out.println("(m) To view the menu press 'm'");
        System.out.println("(n) add dish to the menu");
        System.out.println("(i) import menu from txt");
        System.out.println("(x) export menu to txt");
        System.out.println("(r) remove a dish from menu press 'r'");
        System.out.println("(c) remove all dishes from menu press 'c'");
        System.out.println("(b) back to main menu");
    }
    public void exportMenu() {
        try {
            BufferedWriter writer1 = new BufferedWriter(new FileWriter("dishListExport.txt"));
            for (Dish dish : dishList.getAllDishes()) {
                writer1.write(dish.getTitle() + ";" + dish.getPrice() + ";" + dish.getPrepTime() + ";" + String.join(",", dish.getPhotoURLs()) + ";" + dish.getCategory());
                writer1.newLine();
            }
            writer1.flush();
            writer1.close();
            System.out.println("All dishes in dishList exported successfully in dishListExport.txt");

            BufferedWriter writer2 = new BufferedWriter(new FileWriter("menuExport.txt"));
            for (Dish dish : menu.getDishes()) {
                writer2.write(dish.getTitle() + ";" + dish.getPrice() + ";" + dish.getPrepTime() + ";" + String.join(",", dish.getPhotoURLs()) + ";" + dish.getCategory());
                writer2.newLine();
            }
            writer2.flush();
            writer2.close();
            System.out.println("All dishes in menu exported successfully in menuExport.txt");
        } catch (Exception e) {
            System.out.println("Error exporting dishes to file.");
            e.printStackTrace();
        }
    }

    public void importMenu() {
        try {
            BufferedReader reader1 = new BufferedReader(new FileReader("dishListExport.txt"));
            String line = reader1.readLine();
            while (line != null) {
                String[] fields = line.split(";");
                if (fields.length == 5) {
                    String title = fields[0];
                    double price = Double.parseDouble(fields[1]);
                    int prepTime = Integer.parseInt(fields[2]);
                    List<String> photoURLs = new ArrayList<>(Arrays.asList(fields[3].split(",")));
                    String category = fields[4];
                    Dish dish = new Dish(title, price, prepTime, new ArrayList<>(photoURLs), category);
                    dishList.addDish(dish);
                } else {
                    throw new Exception("Invalid file: dishListExport.txt");
                }
                line = reader1.readLine();
            }
            reader1.close();
            System.out.println("All dishes in dishListExport.txt imported successfully.");

            BufferedReader reader2 = new BufferedReader(new FileReader("menuExport.txt"));
            line = reader2.readLine();
            while (line != null) {
                String[] fields = line.split(";");
                if (fields.length == 5) {
                    String title = fields[0];
                    double price = Double.parseDouble(fields[1]);
                    int prepTime = Integer.parseInt(fields[2]);
                    List<String> photoURLs = Arrays.asList(fields[3].split(","));
                    String category = fields[4];
                    Dish dish = new Dish(title, price, prepTime, new ArrayList<>(photoURLs), category);
                    menu.addDish(dish);
                } else {
                    throw new Exception("Invalid file: menuExport.txt");
                }
                line = reader2.readLine();
            }
            reader2.close();
            System.out.println("All dishes in menuExport.txt imported successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}