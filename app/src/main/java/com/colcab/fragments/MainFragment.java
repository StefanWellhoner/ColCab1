package com.colcab.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.colcab.MainActivity;
import com.colcab.R;
import com.colcab.helpers.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private FrameLayout loadingBar;
    //Authentication
    public static final String NODE_USERS = "users";
    private FirebaseAuth mAuth;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadingBar = getActivity().findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.btn_log_ticket).setOnClickListener(this);
        view.findViewById(R.id.btn_open_tickets).setOnClickListener(this);
        view.findViewById(R.id.btn_closed_tickets).setOnClickListener(this);
        navController = Navigation.findNavController(view);

        //Getting token
        mAuth = FirebaseAuth.getInstance();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if(task.isSuccessful()){
                            String token = task.getResult().getToken();
                            //toast.setText("Token : " + token);
                            saveToken(token);
                        }else{
                            //textView.setText(task.getException().getMessage());
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        //User not logged in
        /*
        if(mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MainFragment.this, SignUpFragment.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
         */

    }

    //Saving the token of each device sign up
    private void saveToken(String token){
        String email = mAuth.getCurrentUser().getEmail();
        //GetSet
        Users user = new Users(email, token);
        //Database name of users
        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users");
        //Storing token
        dbUsers.child(mAuth.getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(MainFragment.this,"Token is saved", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_log_ticket:
                navController.navigate(R.id.action_mainFragment_to_logTicketFragment);
                break;
            case R.id.btn_open_tickets:
                navController.navigate(R.id.action_mainFragment_to_openTicketsFragment);
                break;
            case R.id.btn_closed_tickets:
                navController.navigate(R.id.action_mainFragment_to_closedTicketsFragment);
                break;
        }
    }
}