package com.chinh.wherefoodapp;

import com.chinh.wherefoodapp.Model.CartModel;

import java.util.List;

public class KeyResTime {
    public String restaurant,key,timeOrder;
    public List<CartModel> listFood;

    public KeyResTime(String restaurant, String key, String timeOrder, List<CartModel> listFood) {
        this.restaurant = restaurant;
        this.key = key;
        this.timeOrder = timeOrder;
        this.listFood = listFood;
    }

    public KeyResTime() {
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTimeOrder() {
        return timeOrder;
    }

    public void setTimeOrder(String timeOrder) {
        this.timeOrder = timeOrder;
    }

    public List<CartModel> getListFood() {
        return listFood;
    }

    public void setListFood(List<CartModel> listFood) {
        this.listFood = listFood;
    }
}
