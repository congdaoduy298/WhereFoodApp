package com.chinh.wherefoodapp;

public class SavedOrderFood {
    private String quantity,name,image,price;

    public SavedOrderFood() {
    }
    public SavedOrderFood( String name, String image, String price, String quantity)
    {
        this.name = name;
        this.image=image;
        this.price=price;
        this.quantity=quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getKey() {
//        return key;
//    }
//
//    public void setKey(String key) {
//        this.key = key;
//    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

//    public String getTotalPrice() {
//        return totalPrice;
//    }
//
//    public void setTotalPrice(String totalPrice) {
//        this.totalPrice = totalPrice;
//    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
