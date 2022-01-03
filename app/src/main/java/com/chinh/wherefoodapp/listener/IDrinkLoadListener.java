package com.chinh.wherefoodapp.listener;

import com.chinh.wherefoodapp.Model.DrinkModel;

import java.util.List;

public interface IDrinkLoadListener {

    void onDrinkLoadSuccess(List<DrinkModel> drinkModelList);
    void onDrindLoadFailed(String message);
}
