package com.chinh.wherefoodapp.Utility;


public class PlaceModel {
    int id,drawableId;
    String name;
    String placeType;

    public PlaceModel()
    {

    }
    public PlaceModel(int id, int drawId, String name, String placeType)
    {
        this.id = id;
        this.drawableId=drawId;
        this.name=name;
        this.placeType=placeType;
    }

    public void setDrawId(int drawId) {
        this.drawableId = drawId;
    }
    public int getDrawableId() {
        return drawableId;
    }


    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public void setPlaceTyle(String placeTyle) {
        this.placeType = placeTyle;
    }
    public String getPlaceTyle() {
        return placeType;
    }
}
