package com.chinh.wherefoodapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chinh.wherefoodapp.Adapter.MyDrinkAdapter;
import com.chinh.wherefoodapp.Model.CartModel;
import com.chinh.wherefoodapp.Model.DrinkModel;
import com.chinh.wherefoodapp.Utility.SpaceItemDecoration;
import com.chinh.wherefoodapp.evenbus.MyUpdateCartEvent;
import com.chinh.wherefoodapp.listener.ICartLoadListener;
import com.chinh.wherefoodapp.listener.IDrinkLoadListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodRestaurantActivity extends AppCompatActivity implements IDrinkLoadListener, ICartLoadListener {

    @BindView(R.id.recycler_drink)
    RecyclerView recycler_drink;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
    @BindView(R.id.badge)
    NotificationBadge badge;
    @BindView(R.id.btnCart)
    FrameLayout btnCart;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    private String nameRes;
    private TextView nameR;

    IDrinkLoadListener drinkLoadListener;
    ICartLoadListener cartLoadListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if(EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event){
        countCartItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_restaurant);

        nameRes = getIntent().getStringExtra("name");

        chooseRestaurant();

        init();

        loadDrinkFromFireabas();
        countCartItem();

       nameR  = findViewById(R.id.tvPrice);
    }

    public String chooseRestaurant() {
        if(nameRes.equalsIgnoreCase("quán phở Hoàng") == true) return "Res1";
        else if(nameRes.equalsIgnoreCase("quán ăn mạnh quý") ==true) return "Res2";
        else if(nameRes.equalsIgnoreCase("nhà hàng ẩm thực 316") == true) return "Res3";
        else if(nameRes.equalsIgnoreCase("quán chay đóa sen vàng") == true) return "Res4";
        return null;
    }

    private void loadDrinkFromFireabas() {
        List<DrinkModel> drinkModels = new ArrayList<>();
        FirebaseDatabase .getInstance().getReference("Restaurant").child(chooseRestaurant())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot drinkSnapshot:snapshot.getChildren()){
                                DrinkModel drinkModel = drinkSnapshot.getValue(DrinkModel.class);
                                drinkModel.setKey(drinkSnapshot.getKey());
                                drinkModels.add(drinkModel);
                                nameR.setText(nameRes);
                            }
                            drinkLoadListener.onDrinkLoadSuccess(drinkModels);

                            nameRes = null;
                        }
                        else
                            drinkLoadListener.onDrindLoadFailed("Can't find drink");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        drinkLoadListener.onDrindLoadFailed(error.getMessage());
                    }
                });
    }




    private void init(){
        ButterKnife.bind(this);

        drinkLoadListener = this;
        cartLoadListener = this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recycler_drink.setLayoutManager(gridLayoutManager);
        recycler_drink.addItemDecoration(new SpaceItemDecoration());
        Intent newIntent = new Intent(this, CartActivity.class);
        Bundle bundle = getIntent().getExtras();
        newIntent.putExtra("name", nameRes);
        Log.d("D/TAG", "NAME RES: "+ nameRes );
//        double end_lat = newIntent.getDoubleExtra("lat", 0);
//        String s = String.valueOf(end_lat);
//        Log.d("DCM FoodRestaurant", s);
        if (bundle != null) {
            newIntent.putExtras(bundle);
        }

        btnCart.setOnClickListener(v -> startActivity(newIntent));
        btnBack.setOnClickListener(v->finish());
    }

    @Override
    public void onDrinkLoadSuccess(List<DrinkModel> drinkModelList) {
        MyDrinkAdapter adapter = new MyDrinkAdapter(this, drinkModelList,cartLoadListener);
        recycler_drink.setAdapter(adapter);
    }

    @Override
    public void onDrindLoadFailed(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        int cartSum =0;
        for(CartModel cartModel: cartModelList)
            cartSum += cartModel.getQuantity();
        badge.setNumber(cartSum);
    }

    @Override
    public void onCartdLoadFailed(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }

    private void countCartItem() {
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase
                .getInstance().getReference("Cart")
                .child("UNIQUE_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot cartSnapshot:snapshot.getChildren())
                        {
                            CartModel cartModel =cartSnapshot.getValue(CartModel.class);
                            cartModel.setKey(cartSnapshot.getKey());
                            cartModels.add(cartModel);
                        }
                        cartLoadListener.onCartLoadSuccess(cartModels);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartdLoadFailed(error.getMessage());
                    }
                });
    }
}
