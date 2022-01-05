package com.chinh.wherefoodapp;

public class SavedUserPayment extends SavedOrderFood{
    private String totalPrice;

    public SavedUserPayment() {
    }
    public SavedUserPayment(String name, String image, String price, String quantity,String totalPrice)
    {

        this.totalPrice=totalPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

}


