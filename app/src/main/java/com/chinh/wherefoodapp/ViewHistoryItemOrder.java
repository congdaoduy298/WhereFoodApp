package com.chinh.wherefoodapp;

import static org.greenrobot.eventbus.EventBus.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chinh.wherefoodapp.Adapter.ListFoodAdapter;
import com.chinh.wherefoodapp.Adapter.MyCartAdapter;
import com.chinh.wherefoodapp.Model.CartModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewHistoryItemOrder extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    ListFoodAdapter listFoodAdapter;
    ArrayList<SavedOrderFood> list;
    private ImageView regBack;
    private String key;
    private String nameRes;
    private List<ResAndTime> resAndTimes;
    private Button ViewRestaurants;
    private FirebaseAuth firebaseAuth;
    private TextView txtNameHistory;
    private TextView txtPhoneHistory;
    private TextView allTotalPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history_item);

        txtNameHistory= (TextView)this.findViewById(R.id.txtNameHistory);
        txtPhoneHistory= (TextView)this.findViewById(R.id.txtNameHistory);
        regBack =(ImageView)this.findViewById(R.id.reg_back) ;
        ViewRestaurants=(Button)this.findViewById(R.id.ViewRestaurants);
        allTotalPrice =(TextView)this.findViewById(R.id.allTotalPrice);

        key = getIntent().getStringExtra("key");

        nameRes = getIntent().getStringExtra("restaurant");

        regBack.setOnClickListener(v -> { onBackPressed();});

        ViewRestaurants.setOnClickListener(v -> {
            Intent intent  = new Intent(this, FoodRestaurantActivity.class);
            intent.putExtra("name", nameRes);
            startActivity(intent);
        });

        recyclerView  = findViewById(R.id.recyclerViewOrder);
        database = FirebaseDatabase.getInstance().getReference().child("History").child(key).child("listFood");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        listFoodAdapter = new ListFoodAdapter(this, list);
        recyclerView.setAdapter(listFoodAdapter);



        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    SavedOrderFood savedOrderFood = dataSnapshot.getValue(SavedOrderFood.class);
                    list.add(savedOrderFood);
                }
                onCartLoadSuccess(list);
                listFoodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        public void onCartLoadSuccess(List< CartModel > cartModelList) {
//            double sum = 0;
//            for(CartModel cartModel: cartModelList){
//                sum+=cartModel.getTotalPrice();
//            }
//            txtTotal.setText(new StringBuilder("$ ").append(sum));
//            MyCartAdapter adapter = new MyCartAdapter(this,cartModelList);
//            recyclerCart.setAdapter(adapter);
//        }
//
//        LoadInfoUser();
    }

    private void onCartLoadSuccess(ArrayList<SavedOrderFood> list) {
        double sum = 0;
        for(SavedOrderFood savedOrderFood: list){
            sum+=savedOrderFood.getTotalPrice();
        }
        allTotalPrice.setText(Double.toString(sum));
    }
//    private void LoadInfoUser() {
//        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users")
//                .child(firebaseAuth.getUid());
//        firebaseDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//
//                txtNameHistory.setText(user.name);
//                txtPhoneHistory.setText(user.phone);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
//    }
}
