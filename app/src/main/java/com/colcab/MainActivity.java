package com.colcab;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.colcab.helpers.Dates;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private static final String CHANNEL_ONE= "Simple way of displaying";
    private static final String CHANNEL_TWO = "Your mom";
    private static final String CHANNEL_THREE = "Your mom AS WELL";
    private NotificationManagerCompat notificationManager;
    //Variable referecening to firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private DocumentReference ref = db.collection("tickets").document("First");
    private CollectionReference colref = db.collection("tickets");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        final Set<Integer> topLevelDestinations = new ArraySet<>();
        topLevelDestinations.add(R.id.mainFragment);
        topLevelDestinations.add(R.id.addContractorFragment);

        appBarConfig = new AppBarConfiguration.Builder(topLevelDestinations).setDrawerLayout(drawerLayout).build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
        NavigationUI.setupWithNavController(this.<NavigationView>findViewById(R.id.nav_view), navController);

        loadingBar = findViewById(R.id.loadingBar);

        //Notification Channel
        NotificationChannel channel = new NotificationChannel(CHANNEL_ONE, CHANNEL_THREE, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(CHANNEL_TWO);
        //Notification Manager
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
        notificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Displaying the dates of the tickets
        colref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String dates = "";
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

                    System.out.println(uID);
                    System.out.println(theDates);
                    //Get the current date
                    // I am using 2 date formatters and they don't have the same format for 20 August, 2020
                    //Getting current date is tricky not using the  LocalDate formatter
                    //Calendar cal = Calendar.getInstance();
                    //String currentDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(cal.getTime());
                    Timestamp currentDate = Timestamp.now();
                    Date cur = currentDate.toDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                    String dates2 = sdf.format(cur);
                    //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    //Format for the firebase date
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                    //Set the format of the dates
                    LocalDate date1 = LocalDate.parse(dates2, formatter);
                    LocalDate date2 = LocalDate.parse(dateScheduled, formatter);
                    //Get the number between dates <0>
                    long daysBetween = ChronoUnit.DAYS.between(date1, date2);
                    //Date strDate = simpleDateFormat.parse(datum);
                    count++;
                    // Getting rid of the negative value
                    // Only displaying notification of tickets if the days between is smaller than 5
                    //if(daysBetween > 0){ //example
                    if(daysBetween > 0 && daysBetween < 5){
                        //Send notification
                        System.out.println(daysBetween);
                        displayNotification(daysBetween, id, customer, caseModel, count,false);
                    }
                    if (daysBetween < 0){
                        displayNotification(daysBetween, id, customer, caseModel, count,true);
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
        if (overdue){
            numDays = numDays * -1;
        }
        //Single notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ONE)
                        .setSmallIcon(R.drawable.ic_baseline_access_alarms_24)
                        .setContentTitle("Scheduled Dates of the Week")
                        .setStyle(new NotificationCompat.InboxStyle()
                                .addLine(id)
                                .addLine(customer)
                                .addLine(caseModel)
                                .addLine(daysLeft + numDays))
                        //.setContentText(toString(numDays))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setContentIntent(contentIntent);
        //Adding button
        //.addAction(R.mipmap.ic_launcher, "View Ticket", activtyIntent);

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