package com.colcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LogTicketActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText tfCustomer, tfSerialNum, tfCaseModel, tfCaseDesc, tfRequestedBy, tfCustomerPO;
    private Spinner spnWarranty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_ticket);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent i;
        //@TODO insert intents for navigation
        switch (id) {
            case R.id.nav_home:
                i = new Intent(this, HomePageActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.nav_log_ticket:
                break;
            case R.id.nav_view_closed:
                break;
            case R.id.nav_view_schedule:
                break;
            case R.id.nav_exit:
                System.exit(0);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * void initComponents()
     * Initializes the components to grab data from them
     */
    private void initComponents() {
        tfCustomer = findViewById(R.id.tfCustomer);
        tfSerialNum = findViewById(R.id.tfSerialNum);
        tfCaseModel = findViewById(R.id.tfCaseModel);
        tfCaseDesc = findViewById(R.id.tfCaseDesc);
        tfRequestedBy = findViewById(R.id.tfRequestedBy);
        tfCustomerPO = findViewById(R.id.tfCustomerPO);
        spnWarranty = findViewById(R.id.spnWarranty);
    }

    /**
     * void getCurrentDate()
     * A method that get the current date and time from the system to be logged for each ticket
     *
     * @return return the current date in format MM/dd/yyyy HH:mm:ss
     */
    private Timestamp getCurrentDate() {
        return Timestamp.now();
    }

    /**
     * @param customer   customer name
     * @param serialNum  serial number
     * @param caseModel  case model type
     * @param caseDesc   case description
     * @param reqBy      who requested the ticket
     * @param customerPO customer purchase order
     * @return valid whether all the fields are filled with valid information
     */
    private boolean isValidInfo(String customer, String serialNum, String caseModel, String caseDesc, String reqBy, String customerPO) {
        boolean valid = false;
        if (!customer.isEmpty() && !serialNum.isEmpty() && !caseModel.isEmpty() && !caseDesc.isEmpty() && !reqBy.isEmpty() && !customerPO.isEmpty()) {
            valid = true;
        }
        return valid;
    }

    /**
     * void onLogTicket(Button)
     * When the log ticket button is clicked a ticket will be logged and saved in firebase
     */
    public void onLogTicket(View v) {
        initComponents();

        // Get text from text fields entered by user
        String customer = tfCustomer.getText().toString();
        String warranty = spnWarranty.getSelectedItem().toString();
        String serialNum = tfSerialNum.getText().toString();
        String caseModel = tfCaseModel.getText().toString();
        String caseDesc = tfCaseDesc.getText().toString();
        String reqBy = tfRequestedBy.getText().toString();
        String customerPO = tfCustomerPO.getText().toString();
        String[] reqByArr = reqBy.split(" ");
        Map<String, Object> fullName = new HashMap<>();
        fullName.put("firstName", reqByArr[0]);
        fullName.put("middleName", reqByArr[1]);
        fullName.put("lastName", reqByArr[2]);
        if (isValidInfo(customer, serialNum, caseModel, caseDesc, reqBy, customerPO)) {
            // Add entered text to Hashmap for Firebase
            Map<String, Object> ticketData = new HashMap<>();
            ticketData.put("loggedDate", getCurrentDate());
            ticketData.put("customer", customer);
            ticketData.put("warranty", warranty);
            ticketData.put("serialNumber", serialNum);
            ticketData.put("caseModel", caseModel);
            ticketData.put("caseDescription", caseDesc);
            ticketData.put("requestedBy", fullName);
            ticketData.put("customerPO", customerPO);
            ticketData.put("scheduled", false);

            // Create database instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Insert data from user into "Tickets" collection
            db.collection("tickets").add(ticketData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                // OnSuccess => when ticket has been saved in Firebase
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(LogTicketActivity.this, "Ticket was Logged", Toast.LENGTH_LONG).show();
                    System.out.println("Document ID: " + documentReference.getId());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                // OnFailure => Error has occurred (TAG: Firebase Error:)
                public void onFailure(@NonNull Exception e) {
                    Log.d("Firebase Error: ", e.getMessage());
                }
            });
        } else {
            Toast.makeText(LogTicketActivity.this, "Please fill in all information", Toast.LENGTH_LONG).show();
        }
    }
}
