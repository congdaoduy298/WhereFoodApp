package com.chinh.wherefoodapp.Webservices;


import com.chinh.wherefoodapp.Model.DirectionPlaceModel.DirectionResponseModel;
import com.chinh.wherefoodapp.Model.GooglePlaceModel.GoogleResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPI {

    @GET
    Call<GoogleResponseModel> getNearByPlaces(@Url String url);

    @GET
    Call<DirectionResponseModel> getDirection(@Url String url);

    @GET
    Call<String> getDataFromGoogleApi(@Url String url);
}