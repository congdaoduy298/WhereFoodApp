package com.chinh.wherefoodapp.listener;

import com.chinh.wherefoodapp.Model.CartModel;
import com.chinh.wherefoodapp.Model.DrinkModel;

import java.util.List;

public interface ICartLoadListener {
    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartdLoadFailed(String message);

}
