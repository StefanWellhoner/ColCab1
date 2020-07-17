package com.colcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void onLogTicket(View view) {
        Intent i = new Intent(this, LogTicketActivity.class);
        startActivity(i);
    }

    public void onOpenTicket(View view) {
        Intent i = new Intent(this, OpenTicketsActivity.class);
        startActivity(i);
    }

    public void onClosedTicket(View view) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        //@TODO insert intents for navigation
        switch (id) {
            case R.id.nav_home:
                intent = new Intent(this, HomePageActivity.class);
                break;
            case R.id.nav_log_ticket:
                intent = new Intent(this, LogTicketActivity.class);
                break;
            case R.id.nav_view_closed:
                intent = new Intent(this, ViewOpenTicketActivity.class);
                break;
            case R.id.nav_view_schedule:
                intent = new Intent(this, OpenTicketsActivity.class);
                break;
            case R.id.nav_exit:
                finish();
                break;
        }
        startActivity(intent);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}