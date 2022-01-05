package com.chinh.wherefoodapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chinh.wherefoodapp.Model.CartModel;
import com.chinh.wherefoodapp.R;
import com.chinh.wherefoodapp.evenbus.MyUpdateCartEvent;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {

        private Context context;
        private List<CartModel> cartModelList;

        public MyCartAdapter(Context context, List<CartModel> cartModelList) {
            this.context = context;
            this.cartModelList = cartModelList;
        }

        @NonNull
        @Override
        public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyCartViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.layout_cart_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyCartViewHolder holder, int position) {

            Glide.with(context)
                    .load(cartModelList.get(position).getImage())
                    .into(holder.imageView);
            holder.txtPriceCart.setText(new StringBuilder("$ ").append(cartModelList.get(position).getPrice()));
            holder.txtNameCart.setText(new StringBuilder().append(cartModelList.get(position).getName()));
            holder.txtQuantity.setText(new StringBuilder().append(cartModelList.get(position).getQuantity()));

            //Event
            holder.btnMinus.setOnClickListener(v->{
                minusCartItem(holder,cartModelList.get(position));
            });

            holder.btnPlus.setOnClickListener(v->{
                plusCartItem(holder,cartModelList.get(position));
            });

            holder.btnDelete.setOnClickListener(v->{
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Delete Item")
                        .setMessage("Do you really want to delete item")
                        .setNegativeButton("CANCLE", (dialogInterface, which) -> dialogInterface.dismiss())
                        .setPositiveButton("Ok", (dialogInterface1, which1) -> {

                            //Temp Remove
                            notifyItemRemoved(position);

                            deleteFromFirebase(cartModelList.get(position));
                            dialogInterface1.dismiss();
                        }).create();
                dialog.show();
            });

        }

    private void deleteFromFirebase(CartModel cartModel) {
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child("UNIQUE_ID")
                .child(cartModel.getKey())
                .removeValue()
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }

    private void plusCartItem(MyCartViewHolder holder, CartModel cartModel) {
        cartModel.setQuantity(cartModel.getQuantity()+1);
        cartModel.setTotalPrice(cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()));

        //Update quantity
        holder.txtQuantity.setText(new StringBuilder().append(cartModel.getQuantity()));
        updateFirebase(cartModel);
    }

    private void minusCartItem(MyCartViewHolder holder, CartModel cartModel) {
            if(cartModel.getQuantity()>1){
                cartModel.setQuantity(cartModel.getQuantity()-1);
                cartModel.setTotalPrice(cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()));

                //Update quantity
                holder.txtQuantity.setText(new StringBuilder().append(cartModel.getQuantity()));
                updateFirebase(cartModel);
            }
    }

    private void updateFirebase(CartModel cartModel) {
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child("UNIQUE_ID")
                .child(cartModel.getKey())
                .setValue(cartModel)
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }
    public class MyCartViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.btnMinus)
            ImageView btnMinus;
            @BindView(R.id.btnPlus)
            ImageView btnPlus;
            @BindView(R.id.btnDelete)
            ImageView btnDelete;
            @BindView(R.id.imageView)
            ImageView imageView;
            @BindView(R.id.txtNameCart)
            TextView txtNameCart;
            @BindView(R.id.txtPriceCart)
            TextView txtPriceCart;
            @BindView(R.id.txtQuantity)
            TextView txtQuantity;

            Unbinder unbinder;
            public MyCartViewHolder(@NonNull View itemView) {
                super(itemView);

                unbinder = ButterKnife.bind(this,itemView);
            }
        }
    }
