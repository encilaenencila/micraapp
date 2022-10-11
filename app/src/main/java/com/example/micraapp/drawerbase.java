package com.example.micraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class drawerbase extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;


    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawerbase, null);
        FrameLayout container = drawerLayout.findViewById(R.id.framelayoutcontent);
        container.addView(view);
        super.setContentView(drawerLayout);
        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_emergency:
                startActivity(new Intent(this, callmodule.class));

                overridePendingTransition(0, 0);
                break;

            case R.id.nav_dashboard:
                startActivity(new Intent(this, Dashboard.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_report:
                startActivity(new Intent(this, category.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.announcer:
                startActivity(new Intent(this, Announcement.class));
                overridePendingTransition(0, 0);
                break;
         /*   case R.id.crimereport:
                startActivity(new Intent(this, BarChartActivity.class));
                overridePendingTransition(0, 0);
                break;*/
        /*   case R.id.nav_logout:
               startActivity(new Intent(this,MainActivity.class));
               finish();
               overridePendingTransition(0,0);
               break;*/
        }
        return false;
    }

    protected void allocateActivitytitle(String tittleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(tittleString);
        }
    }


}