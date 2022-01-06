package com.chinh.wherefoodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chinh.wherefoodapp.R;
import com.chinh.wherefoodapp.SavedOrderFood;

import java.util.ArrayList;

public class ListFoodAdapter extends RecyclerView.Adapter<ListFoodAdapter.MyViewHolder> {

    Context context;
    ArrayList<SavedOrderFood> list;

    public ListFoodAdapter(Context context, ArrayList<SavedOrderFood> list) {
        this.context = context;
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtNameCart, txtTotalProduct, txtTotalPrice , txtPriceCart;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameCart = itemView.findViewById(R.id.txtNameCart);
            txtTotalProduct=itemView.findViewById(R.id.txtTotalProduct);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            txtPriceCart  = itemView.findViewById(R.id.txtPriceCart);
            imageView      = itemView.findViewById(R.id.imageView);
        }
    }

    @NonNull
    @Override
    public ListFoodAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_list_food_order,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListFoodAdapter.MyViewHolder holder, int position) {
        SavedOrderFood savedOrderFood = list.get(position);

//        holder.txtPriceCart.setText(new StringBuilder("$ ").append(cartModelList.get(position).getPrice()));
//        holder.txtNameCart.setText(new StringBuilder().append(cartModelList.get(position).getName()));
//        holder.txtQuantity.setText(new StringBuilder().append(cartModelList.get(position).getQuantity()));

        holder.txtPriceCart.setText(savedOrderFood.getPrice());
        holder.txtTotalPrice.setText(Float.toString(savedOrderFood.getTotalPrice()));
        holder.txtNameCart.setText(savedOrderFood.getName());
        holder.txtTotalProduct.setText((Integer.toString(savedOrderFood.getQuantity())));
        Glide.with(context)
                .load(list.get(position).getImage())
                .into(holder.imageView);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
