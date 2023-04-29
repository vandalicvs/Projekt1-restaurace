package com.pavel.restaurace;

import java.util.ArrayList;

public class DishList {
    private ArrayList<Dish> dishes;

    public DishList() {
        this.dishes = new ArrayList<>();
    }

    public void addDish(Dish dish) {
        this.dishes.add(dish);
    }

    public void removeDish(Dish dish) {
        this.dishes.remove(dish);
    }

    public void removeAllDishes() {
        this.dishes.clear();
    }

    public ArrayList<Dish> getAllDishes() {
        return this.dishes;
    }

    public Dish getDishByTitle(String title) {
        for (Dish dish : this.dishes) {
            if (dish.getTitle().equals(title)) {
                return dish;
            }
        }
        return null;
    }
}