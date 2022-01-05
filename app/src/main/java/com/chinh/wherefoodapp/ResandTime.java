package com.chinh.wherefoodapp;

import com.chinh.wherefoodapp.Model.CartModel;

import java.util.List;

public class ResandTime {

    public String restaurant;
    public String timeOrder;
    public List<CartModel> listFood;

   public ResandTime( String restaurant, String timeOrder)
    {
        this.restaurant = restaurant;
        this.timeOrder = timeOrder;
    }
    public ResandTime()
    {

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

    public ResandTime(String restaurant, String timeOrder, List<CartModel> listFood) {
        this.restaurant = restaurant;
        this.timeOrder = timeOrder;
        this.listFood = listFood;
    }

    public List<CartModel> getListFood() {
        return listFood;
    }

    public void setListFood(List<CartModel> listFood) {
        this.listFood = listFood;
    }
}
