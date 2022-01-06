package com.chinh.wherefoodapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chinh.wherefoodapp.OrderHistoryToDetail;
import com.chinh.wherefoodapp.R;
import com.chinh.wherefoodapp.ResAndTime;
import com.chinh.wherefoodapp.Utility.LoadingDialog;
import com.chinh.wherefoodapp.ViewHistoryItemOrder;
import com.chinh.wherefoodapp.databinding.FragmentOrderHistoryBinding;
import com.chinh.wherefoodapp.databinding.LayoutHistoryOrderedBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistoryFragment extends Fragment implements OrderHistoryToDetail {
    private FragmentOrderHistoryBinding binding;
    private LoadingDialog loadingDialog;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ResAndTime> resAndTimes;
    private FirebaseRecyclerAdapter<String, OrderHistoryFragment.ViewHolder> firebaseRecyclerAdapter;
    private OrderHistoryToDetail orderHistoryToDetail;
    private ArrayList<String> SavedKeyListFood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false);

        orderHistoryToDetail = this;
        firebaseAuth = FirebaseAuth.getInstance();
        resAndTimes = new ArrayList<>();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Orders History");

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new LoadingDialog(requireActivity());
        binding.savedRecyclerViewHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.savedRecyclerViewHistory);
        getOrderHistory();

    }

    private void getOrderHistory() {
        loadingDialog.StartLoading();
        Query query = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getUid()).child("History");

        FirebaseRecyclerOptions<String> options = new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(query, String.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<String, OrderHistoryFragment.ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderHistoryFragment.ViewHolder holder,int position, @NonNull String saveOrderId) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("History").child(saveOrderId);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            ResAndTime resAndTime = snapshot.getValue(ResAndTime.class);
                            holder.binding.setResAndTime(resAndTime);
                            holder.binding.setListener(orderHistoryToDetail);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public OrderHistoryFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutHistoryOrderedBinding binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),
                        R.layout.layout_history_ordered, parent, false);
                return new OrderHistoryFragment.ViewHolder(binding);

            }
        };

        binding.savedRecyclerViewHistory.setAdapter(firebaseRecyclerAdapter);
        loadingDialog.stopLoading();
    }


    @Override
    public void onResume() {
        super.onResume();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        firebaseRecyclerAdapter.stopListening();
    }

    @Override
    public void onViewOrder(ResAndTime resAndTime) {

        Intent fragmentToViewHistory  = new Intent(requireContext(), ViewHistoryItemOrder.class);
        fragmentToViewHistory.putExtra("restaurant", resAndTime.restaurant);
        fragmentToViewHistory.putExtra("timeOrder", resAndTime.timeOrder);
        fragmentToViewHistory.putExtra("key", resAndTime.key);

        startActivity(fragmentToViewHistory);

        Toast.makeText(requireContext(),"View Ordered detail ",Toast.LENGTH_LONG).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private LayoutHistoryOrderedBinding binding;

        public ViewHolder(@NonNull LayoutHistoryOrderedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}