package com.chinh.wherefoodapp;

import static org.greenrobot.eventbus.EventBus.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chinh.wherefoodapp.Adapter.MyCartAdapter;
import com.chinh.wherefoodapp.Model.CartModel;
import com.chinh.wherefoodapp.databinding.ActivityMainBinding;
import com.chinh.wherefoodapp.databinding.NavDrawerLayoutBinding;
import com.chinh.wherefoodapp.databinding.ToolbarLayoutBinding;
import com.chinh.wherefoodapp.evenbus.MyUpdateCartEvent;
import com.chinh.wherefoodapp.listener.ICartLoadListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements ICartLoadListener {

    @BindView(R.id.recycler_cart)
    RecyclerView recyclerCart;
    @BindView(R.id.mainLayout_Cart)
    RelativeLayout mainLayout_Cart;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    @BindView(R.id.btnConfirmPay)
    Button btnConfirmPay;


    ICartLoadListener cartLoadListener;
    private NavDrawerLayoutBinding navDrawerLayoutBinding;
    private ActivityMainBinding activityMainBinding;
    private ToolbarLayoutBinding toolbarLayoutBinding;
    private String nameRes;
    private ArrayList<String> userSavedHistoryId;
    private FirebaseAuth firebaseAuth;
    private Bundle savedInstanceState;


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
        loadCartFromFirebase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        firebaseAuth = FirebaseAuth.getInstance();
        nameRes = getIntent().getStringExtra("name");

        initUI();
        loadCartFromFirebase();
        getUserSavedLocations();

    }


    private String getTimeNow()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

    private void SavedHistoryFirebase( List<CartModel> cartModels) {


        btnConfirmPay.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("History");
            String key =  myRef.push().getKey();

            ResAndTime resandTime = new ResAndTime(key, nameRes, getTimeNow(),cartModels);
            myRef.child(key).setValue(resandTime,  new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                    Intent intent = new Intent(CartActivity.this, MapsActivity.class);
                    Bundle bundle = getIntent().getExtras();
                    intent.putExtras(bundle);

                    saveUserHistory(key);
                    FirebaseDatabase.getInstance()
                            .getReference("Cart")
                            .child("UNIQUE_ID").removeValue();
                    startActivity(intent);
                }
            });
        });
    }
    private void saveUserHistory(String key) {
        userSavedHistoryId.add(key); //them id vao array list
        Task<Void> databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getUid()).child("History").setValue(userSavedHistoryId); //set id vao user -> saved locations

    }

    private void getUserSavedLocations() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getUid()).child("History");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key= ds.getValue(String.class);
                        userSavedHistoryId.add(key);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadCartFromFirebase() {
        List<CartModel> cartModels = new ArrayList<>();
        userSavedHistoryId = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child("UNIQUE_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            for(DataSnapshot cartSnapshot:snapshot.getChildren()){
                                CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                                cartModel.setKey(cartSnapshot.getKey());
                                cartModels.add(cartModel);

                            }
                            cartLoadListener.onCartLoadSuccess(cartModels);

                            SavedHistoryFirebase(cartModels);

                        }
                        else
                            cartLoadListener.onCartdLoadFailed("Cart empty");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                         cartLoadListener.onCartdLoadFailed(error.getMessage());
                    }
                });
    }

    private void initUI() {
        ButterKnife.bind(this);

        cartLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerCart.setLayoutManager(layoutManager);
        recyclerCart.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));

        btnBack.setOnClickListener(view -> finish());

    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        double sum = 0;
        for(CartModel cartModel: cartModelList){
            sum+=cartModel.getTotalPrice();
        }
        txtTotal.setText(new StringBuilder("$ ").append(sum));
        MyCartAdapter adapter = new MyCartAdapter(this,cartModelList);
        recyclerCart.setAdapter(adapter);
    }

    @Override
    public void onCartdLoadFailed(String message) {
        Snackbar.make(mainLayout_Cart,message,Snackbar.LENGTH_LONG).show();
    }

}