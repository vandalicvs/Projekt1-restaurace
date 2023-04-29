package com.pavel.restaurace;

import java.util.ArrayList;

public class Menu {
    private ArrayList<Dish> menuList;

    public Menu() {
        this.menuList = new ArrayList<>();
    }

    public void addDish(Dish dish) {
        this.menuList.add(dish);
    }

    public ArrayList<Dish> getDishes() {
        return this.menuList;
    }

    public void removeDish(Dish dish) {
        this.menuList.remove(dish);
    }

    public void removeAllDishes() {
        this.menuList.clear();
    }

    public void listDishes() {
        System.out.println("List of all dishes:");
        for (Dish dish : menuList) {
            System.out.println(dish.getTitle());
        }
    }

    public Dish getDishByName(String name) {
        for (Dish dish : menuList) {
            if (dish.getTitle().equals(name)) {
                return dish;
            }
        }
        return null;
    }




}