package com.example.myapplication;

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
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
private DrawerLayout drawer;
DatabaseReference dbreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        FirebaseApp.initializeApp(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = findViewById(R.id.drawer_layout);



        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawer,toolbar,R.string.navigation_draw_open,R.string.navigation_draw_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        dbreference = FirebaseDatabase.getInstance("https://gass-database-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Sensor/gas_status");
        ImageView tankimage = findViewById(R.id.tankimg);
        ImageView tanktext= findViewById(R.id.tank_text);


       dbreference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               Integer gasData = snapshot.getValue(Integer.class);

               if (gasData != null) {
                   if (gasData >= 400) {
                       Log.d("Firebase", "YEZZIR");
                       tankimage.setImageResource(R.drawable.red_tank);
                       tanktext.setImageResource(R.drawable.leak_detected);
                   } else {
                       Log.d("Firebase", "WALABRO");
                       tankimage.setImageResource(R.drawable.greentank);
                       tanktext.setImageResource(R.drawable.no_leakage);
                   }
               }
           }


           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Log.e("Firebase", "Failed to read value", error.toException());
           }
       });
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int itemId = item.getItemId();
        if (itemId == R.id.nav_account) {
            Intent intent = new Intent(Menu.this, Account.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_settings) {
            Intent intent = new Intent(Menu.this, Settings.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_guide) {
            Intent intent = new Intent(Menu.this, Guide.class);
            startActivity(intent);
        }

        // Optionally, set the selected item visually in the NavigationView
        item.setChecked(true);






        return true;
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed()  ;
        }


    }
}