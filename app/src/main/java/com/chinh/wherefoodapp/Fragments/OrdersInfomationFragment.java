package com.chinh.wherefoodapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chinh.wherefoodapp.R;
import com.chinh.wherefoodapp.SavedDrinkListInterface;
import com.chinh.wherefoodapp.SavedOrderFood;
import com.chinh.wherefoodapp.SavedUserPayment;
import com.chinh.wherefoodapp.Utility.LoadingDialog;
import com.chinh.wherefoodapp.databinding.FragmentOrderHistoryBinding;
import com.chinh.wherefoodapp.databinding.FragmentOrdersInfomationBinding;
import com.chinh.wherefoodapp.databinding.LayoutOrderedItemBinding;
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


public class OrdersInfomationFragment extends Fragment implements SavedDrinkListInterface{
    private FragmentOrdersInfomationBinding binding;
    private LoadingDialog loadingDialog;
    private FirebaseAuth firebaseAuth;
    private ArrayList<SavedOrderFood> savedOrderFoodArrayList;
    private FirebaseRecyclerAdapter<String, OrdersInfomationFragment.ViewHolder> firebaseRecyclerAdapter;
    private SavedDrinkListInterface savedDrinkListInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrdersInfomationBinding.inflate(inflater,container,false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Orders Information");
        savedDrinkListInterface = this;
        firebaseAuth = FirebaseAuth.getInstance();
        savedOrderFoodArrayList = new ArrayList<>();
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new LoadingDialog(requireActivity());
        binding.recyclerViewOrder.setLayoutManager(new LinearLayoutManager(requireContext()));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.recyclerViewOrder);
        getOrderInformation();

    }


    private void getOrderInformation() {
        loadingDialog.StartLoading();
        Query query = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getUid()).child("Restaurant");

        FirebaseRecyclerOptions<String> options = new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(query, String.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<String, OrdersInfomationFragment.ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrdersInfomationFragment.ViewHolder holder,int position, @NonNull String saveOrderId) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Restaurant").child("Res1").child(saveOrderId);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            SavedOrderFood savedOrderFood = snapshot.getValue(SavedOrderFood.class);
//                            SavedUserPayment savedUserPayment = snapshot.getValue(SavedUserPayment.class);
                            holder.binding.setSavedOrderFood(savedOrderFood);
                            holder.binding.setListener(savedDrinkListInterface);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public OrdersInfomationFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutOrderedItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),
                        R.layout.layout_ordered_item, parent, false);
                return new OrdersInfomationFragment.ViewHolder(binding);

            }
        };

        binding.recyclerViewOrder.setAdapter(firebaseRecyclerAdapter);
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
    public void onViewOrder(SavedOrderFood savedOrderFood) {
//
//        Navigation.findNavController(getView()).navigate(OrderHistoryFragmentDirections.actionBtnSavedOrdersToOrdersInfomationFragment());
//
//        Toast.makeText(requireContext(),"detail ",Toast.LENGTH_LONG).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private LayoutOrderedItemBinding binding;

        public ViewHolder(@NonNull LayoutOrderedItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}