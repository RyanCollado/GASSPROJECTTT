package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView prcnt= findViewById(R.id.percent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(Menu.this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Menu.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }


       dbreference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               Integer gasData = snapshot.getValue(Integer.class);

               if (gasData != null) {
                   if (gasData >= 400) {
                       Log.d("Firebase", "YEZZIR");
                       tankimage.setImageResource(R.drawable.red_tank);
                       tanktext.setImageResource(R.drawable.leak_detected);
                       makeNotification();



                   } else {
                       Log.d("Firebase", "WALABRO");
                       tankimage.setImageResource(R.drawable.greentank);
                       tanktext.setImageResource(R.drawable.no_leakage);





                   }
               }

               int percentage = (int)((gasData / 400.0) * 100);
               percentage = Math.min(percentage, 100);
               // Set the percentage to the TextView
               prcnt.setText(percentage + "%");

               if (gasData < 80) {
                   prcnt.setTextColor(getResources().getColor(R.color.greenColor));
               } else if (gasData >= 80 && gasData < 280) {
                   prcnt.setTextColor(getResources().getColor(R.color.yellowColor));
               } else {
                   prcnt.setTextColor(getResources().getColor(R.color.redColor));
               }



           }


           @Override
           public void onCancelled(@NonNull  DatabaseError error) {
               Log.e("Firebase", "Failed to read value", error.toException());
           }
       });
    }

   public void makeNotification(){
        String channelID="CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getApplicationContext(), channelID );
        builder.setSmallIcon(R.drawable.ic_notifications);
        builder.setContentTitle("GAS LEAK DETECTED");
        builder.setContentText("Your gas sensor has detected a possible gas leak in the vicinity");
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent (getApplicationContext(), Menu.class);


       PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(),
               0, intent, PendingIntent.FLAG_MUTABLE);
       builder.setContentIntent(pendingIntent);
       NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel= notificationManager.getNotificationChannel(channelID);
            if(notificationChannel == null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel= new NotificationChannel(channelID,"Some Description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }

        }

        notificationManager.notify(0,builder.build());
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