package com.colcab.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.colcab.MainActivity;
import com.colcab.R;
import com.colcab.helpers.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class SignUpFragment extends Fragment {

    public static final String CHANNEL_ONE= "User Successfully Registered!";
    //private static final String CHANNEL_TWO = "Thank you";
    //private static final String CHANNEL_THREE = "Simplified Coding";

    private NavController navController;
    //Authentication
    private Button buttonSignUp;
    private EditText editTextPassword, editTextUsername;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    public SignUpFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //After Signup
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if(task.isSuccessful()){
                            String token = task.getResult().getToken();
                            saveToken(token);
                        }else{

                        }
                    }
                });
        mAuth = FirebaseAuth.getInstance();

    }
    //Also in mainfragment
    //Checks if user has signup
    @Override
    public void onStart() {
        super.onStart();
        //User not logged in
        /*
         if(mAuth.getCurrentUser() == null) {
            //navController.navigate(R.id.action_SignUpFragment_to_MainFragment);
            Intent intent = new Intent(this, this);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
         */

    }
    //Saving token to the RealTime Database
    private void saveToken(String token){
        String email = mAuth.getCurrentUser().getEmail();
        //GetSet
        Users user = new Users(email, token);
        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users");
        //Save to firebase
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
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        //Setting components
        //text fields containing user's entered data
        editTextUsername = v.findViewById(R.id.editTextUsername);
        editTextPassword = v.findViewById(R.id.editTextPassword);
        progressBar = v.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        navController = Navigation.findNavController(v);
        //Button Sign Up
        v.findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
                //displayNotification();
            }
        });
    }
    //Create user
    private void createUser(){
        final String email = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        //Validation
        if(email.isEmpty()){
            editTextUsername.setError("Email Required!");
            editTextUsername.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password Required!");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Password should be more than 6 characters!");
            editTextPassword.requestFocus();
            return;
        }
        //Make progress bar visible
        progressBar.setVisibility(View.VISIBLE);
        //Saving values to Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Successfully saved
                        if(task.isSuccessful()){
                            startMainActivity();
                        }else{
                            //User already exist
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                userLogin(email, password);
                            }else{
                                //Error
                                progressBar.setVisibility(View.VISIBLE);
                                //Toast.makeText(SignUpFragment.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
    //Token is device specific
    //if i logged in with a different username/ email the token will not change
    private void userLogin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Successfully Logged in
                        if(task.isSuccessful()){
                            startMainActivity();
                        }else{
                            //Error
                            progressBar.setVisibility(View.INVISIBLE);
                            //Toast.makeText(SignUpFragment.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    private void startMainActivity(){
        //Navigating to Main activity
        //navController.navigate(R.id.mainFragment);
        //Intent intent = new Intent(this, MainFragment.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //startActivity(intent);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }
}