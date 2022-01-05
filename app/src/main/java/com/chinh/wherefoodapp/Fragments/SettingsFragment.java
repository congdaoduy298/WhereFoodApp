package com.chinh.wherefoodapp.Fragments;

import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chinh.wherefoodapp.Login;
import com.chinh.wherefoodapp.R;
import com.chinh.wherefoodapp.Utility.LoadingDialog;
import com.chinh.wherefoodapp.databinding.FragmentSettingsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private FirebaseAuth firebaseAuth;
    private LoadingDialog loadingDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container,false);
        firebaseAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(getActivity());

        binding.txtUsername.setOnClickListener(username -> {
            usernameDialog();
        });

        binding.cardEmail.setOnClickListener(view -> {
            SettingsFragmentDirections.ActionBtnSettingToEmailConfirmationFragment directions =
                    SettingsFragmentDirections.actionBtnSettingToEmailConfirmationFragment();

            Navigation.findNavController(getView()).navigate(directions);
        });

        binding.cardPassword.setOnClickListener(view -> {

            SettingsFragmentDirections.ActionBtnSettingToEmailConfirmationFragment directions =
                    SettingsFragmentDirections.actionBtnSettingToEmailConfirmationFragment();
            directions.setIsPassword(true);

            Navigation.findNavController(requireView()).navigate(directions);

        });

        binding.cardLogout.setOnClickListener(view ->{
            firebaseAuth.signOut();
            Intent intent = new Intent(getActivity().getApplication(), Login.class);
            startActivity(intent);
        });

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Settings");
        binding.txtEmail.setText(firebaseAuth.getCurrentUser().getEmail() );
        binding.txtUsername.setText(firebaseAuth.getCurrentUser().getDisplayName());

        if (firebaseAuth.getCurrentUser().isEmailVerified())
        {
            binding.txtEEmail.setVisibility(View.GONE);

        }
        else {
            binding.txtEEmail.setVisibility(View.VISIBLE);
        }
        binding.txtEEmail.setOnClickListener(verify -> {
            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Mail sent verify the email", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onComplete: profile email " + task.getException());
                    }
                }
            });
        });

    }

    private void usernameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.username_dialog_layout, null, false);
        builder.setView(view);
        TextInputEditText edtUsername = view.findViewById(R.id.edtDialogUsername);

        builder.setTitle("Edit Username");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = edtUsername.getText().toString().trim();
                if (!username.isEmpty()) {

                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();
                    firebaseAuth.getCurrentUser().updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                                Map<String, Object> map = new HashMap<>();
                                map.put("username", username);
                                databaseReference.child(firebaseAuth.getUid()).updateChildren(map);

                                binding.txtUsername.setText(username);
                                Toast.makeText(getContext(), "Username is updated", Toast.LENGTH_SHORT).show();

                            } else {
                                Log.d("TAG", "onComplete: " + task.getException());
                                Toast.makeText(getContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Username is required", Toast.LENGTH_SHORT).show();
                }
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
    }
}