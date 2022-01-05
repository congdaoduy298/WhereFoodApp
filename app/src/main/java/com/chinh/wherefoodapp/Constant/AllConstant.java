package com.chinh.wherefoodapp.Constant;


import com.chinh.wherefoodapp.R;
import com.chinh.wherefoodapp.Utility.PlaceModel;

import java.util.ArrayList;
import java.util.Arrays;

public interface AllConstant {

    int STORAGE_REQUEST_CODE = 1000;
    int LOCATION_REQUEST_CODE = 2000;
    String IMAGE_PATH = "/Profile/image_profile.jpg";


    ArrayList<PlaceModel> placesName = new ArrayList<>(
            Arrays.asList(
                    new PlaceModel(1, R.drawable.ic_restaurant, "Restaurant", "restaurant"),
                    new PlaceModel(2, R.drawable.ic_shopping_cart, "Groceries", "supermarket")

            )
    );
}
