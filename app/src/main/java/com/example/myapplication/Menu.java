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
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Menu extends AppCompatActivity  {
 DrawerLayout drawer;

ImageView menu;

LinearLayout Menu,Guide,Aaccount;
DatabaseReference dbreference;

    Handler handler;

    TextView datetimetxt1, datetimetxtup1;

    Runnable updateTimeRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        FirebaseApp.initializeApp(this);

        drawer = findViewById(R.id.drawer_layout);
        menu= findViewById(R.id.menu);
        Menu=findViewById(R.id.Menu);
        Guide=findViewById(R.id.Guidebook);
        Aaccount=findViewById(R.id.account);
        datetimetxt1 = findViewById(R.id.datetimetxt);
        datetimetxtup1 =findViewById(R.id.datetimetxtup);
        String currentDateTime = getCurrentDateTime();

        handler= new Handler();


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String datetimeText = sharedPreferences.getString("datetimeText", "");
        datetimetxt1.setText(datetimeText);
        updateTimeRunnable= new Runnable() {
            @Override
            public void run() {
                String currentDateTime = getCurrentDateTime();
                datetimetxtup1.setText( currentDateTime);

                // Schedule the Runnable to run again in 1000 milliseconds (1 second)
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(updateTimeRunnable);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawer);
            }
        });

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });


        Guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Menu.this,Guide.class);
            }
        });



        Aaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Menu.this,Account.class);
            }
        });










        dbreference = FirebaseDatabase.getInstance("https://gass-database-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Sensor/device1/gas_status");
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
                       tanktext.setImageResource(R.drawable.leak_detected2);
                       makeNotification();
                       String currentDateTime = getCurrentDateTime();

                       // Save the current date and time to SharedPreferences
                       SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                       SharedPreferences.Editor editor = sharedPreferences.edit();
                       editor.putString("datetimeText", currentDateTime);
                       editor.apply();

                       // Set the text with the updated date and time
                       datetimetxt1.setText(currentDateTime);


                   } else {
                       Log.d("Firebase", "WALABRO");
                       tankimage.setImageResource(R.drawable.greentank);
                       tanktext.setImageResource(R.drawable.noleak2);





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
                   tankimage.setImageResource(R.drawable.yellowtank);
                   tanktext.setImageResource(R.drawable.caution);
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

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
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


public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
}
public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
}


public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent (activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
}


@Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawer);
}



}