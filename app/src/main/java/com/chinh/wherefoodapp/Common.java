package com.chinh.wherefoodapp;

import com.chinh.wherefoodapp.Webservices.RetrofitAPI;
import com.chinh.wherefoodapp.Webservices.RetrofitClient;

public class Common {

    public static RetrofitAPI getGoogleApi(){
        return RetrofitClient.getRetrofitClient().create(RetrofitAPI.class);

    }

}