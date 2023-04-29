package com.pavel.restaurace;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private int number;
    private String note;
    private List<OrderedDishes> orderedDishesList;

    public Table(int number) {
        this.number = number;
        this.orderedDishesList = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<OrderedDishes> getOrderedDishesList() {
        return orderedDishesList;
    }

    public void addOrderedDishes(OrderedDishes orderedDishes) {
        orderedDishesList.add(orderedDishes);
    }

}