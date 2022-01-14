package com.chinh.wherefoodapp;

import com.chinh.wherefoodapp.Model.CartModel;

import java.util.List;

public class ResAndTime {

    public String key, restaurant, timeOrder;
    public List<CartModel> listFood;
    public double lat;
    public double lng;

    public ResAndTime(String key, String restaurant, String timeOrder, List<CartModel> listFood, double lat, double lng) {
        this.key = key;
        this.restaurant = restaurant;
        this.timeOrder = timeOrder;
        this.listFood = listFood;
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public ResAndTime() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
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
