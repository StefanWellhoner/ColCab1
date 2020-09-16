package com.colcab;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.colcab.helpers.Dates;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavController navController;
    private AppBarConfiguration appBarConfig;
    private FrameLayout loadingBar;
    //Ticket Notification
    private static final String CHANNEL_ONE = "ColCab";
    private static final String CHANNEL_TWO = "Scheduled Dates";
    private static final String CHANNEL_THREE = "User Log in";
    private NotificationManagerCompat notificationManager;
    //Variable referencing to firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference colref = db.collection("tickets");
    //Global variable for connection
    public static boolean isConnected;
    private TextView connectionStatus;
    //Animation
    private int shortAnimationDuration;
    private TextView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        final Set<Integer> topLevelDestinations = new ArraySet<>();
        topLevelDestinations.add(R.id.mainFragment);
        topLevelDestinations.add(R.id.addContractorFragment);
        topLevelDestinations.add(R.id.businessIntelligenceFragment);

        appBarConfig = new AppBarConfiguration.Builder(topLevelDestinations).setDrawerLayout(drawerLayout).build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
        NavigationUI.setupWithNavController(this.<NavigationView>findViewById(R.id.nav_view), navController);

        loadingBar = findViewById(R.id.loadingBar);
        //Connection
        connectionStatus = findViewById(R.id.textviewConnection);
        connectionStatus.setVisibility(View.GONE);


        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);
        //Notification Channel
        NotificationChannel channel = new NotificationChannel(CHANNEL_ONE, CHANNEL_THREE, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(CHANNEL_TWO);
        //Notification Manager
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
        notificationManager = NotificationManagerCompat.from(this);
    }

    //Connectivity Check
    public BroadcastReceiver connectivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                boolean noConnectivity = intent.getBooleanExtra(
                        ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
                );
                if (noConnectivity) {
                    isConnected = false;
                    loadingBar.setVisibility(View.GONE);
                    connectionStatus.setAlpha(0f);
                    connectionStatus.setVisibility(View.VISIBLE);
                    //Animation
                    connectionStatus.animate()
                            .alpha(1f)
                            .setDuration(shortAnimationDuration)
                            .setListener(null);
                } else {
                    isConnected = true;
                    connectionStatus.setVisibility(View.GONE);
                    connectionStatus.animate()
                            .alpha(0f)
                            .setDuration(shortAnimationDuration)
                            .setListener(null);
                }
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        //Unregister the Connection receiver
        unregisterReceiver(connectivity);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Connection Check
        IntentFilter intentFilterConn = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivity, intentFilterConn);
        //Displaying the dates of the tickets
        colref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //Counter for sending multiple notification id=count
                int count = 0;
                //Array used to store for ID and Date from firebase
                ArrayList<String> theDates = new ArrayList<>();
                ArrayList<String> uID = new ArrayList<>();
                //Getting the information from firebase
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Dates date = documentSnapshot.toObject(Dates.class);
                    date.setDocumentID(documentSnapshot.getId());
                    String id = date.getDocumentID();
                    String customer = date.getCustomer();
                    String caseModel = date.getCaseModel();
                    String dateScheduled = date.getScheduledDate();
                    //Storing values in array
                    theDates.add(dateScheduled);
                    uID.add(id);
                    //Get the current date
                    Timestamp currentDate = Timestamp.now();
                    Date cur = currentDate.toDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                    String dates2 = sdf.format(cur);
                    //Format for the firebase date
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                    if (dateScheduled != null) {
                        //Set the format of the dates
                        LocalDate date1 = LocalDate.parse(dates2, formatter);
                        LocalDate date2 = LocalDate.parse(dateScheduled, formatter);
                        //Get the number between dates <0>

                        long daysBetween = ChronoUnit.DAYS.between(date1, date2);
                        count++;
                        // Getting rid of the negative value
                        // Only displaying notification of tickets if the days between is smaller than 5
                        if (daysBetween > 0 && daysBetween < 5) {
                            //Send notification
                            System.out.println(daysBetween);
                            displayNotification(daysBetween, id, customer, caseModel, count, false);
                        }
                        if (daysBetween < 0) {
                            displayNotification(daysBetween, id, customer, caseModel, count, true);
                        }
                    }
                }
            }
        });
    }

    //Displaying tickets smaller than 5 days in notification
    private void displayNotification(long numDays, String id, String customer, String caseModel, int count, boolean overdue) {
        //On click notification action
        //Intent to view tickets but on main
        Intent activityIntents = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntents, 0);
        String daysLeft = overdue ? "Days overdue: " : "Days left: ";
        if (overdue) {
            numDays = numDays * -1;
        }
        //Single notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ONE)
                        .setSmallIcon(R.drawable.ic_baseline_access_alarms_24)
                        .setContentTitle("Scheduled Tickets")
                        .setStyle(new NotificationCompat.InboxStyle()
                                .addLine("ID: " + id)
                                .addLine("Customer: " + customer)
                                .addLine("Casemodel: " + caseModel)
                                .addLine(daysLeft + numDays))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setColor(ContextCompat.getColor(this, R.color.colorSecondary))
                        .setContentIntent(contentIntent);
        //.addAction(fragment_scheduled_tickets, false);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        notificationManagerCompat.notify(count, mBuilder.build());
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfig) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            default:
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                return NavigationUI.onNavDestinationSelected(item, navController)
                        || super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}