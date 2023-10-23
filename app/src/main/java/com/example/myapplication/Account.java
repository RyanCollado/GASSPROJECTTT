package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Account extends AppCompatActivity  {
    FirebaseAuth auth;
    Button button,button2;
    TextView textname,textmobile,textemail,textdevkey;
    ProgressBar progressBar;
    String name,mobile,email,devkey;
    FirebaseAuth authprofile;

    FirebaseUser user;
    DrawerLayout drawer;

    ImageView menu;

    LinearLayout Menu,Guide,Aaccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawer,toolbar,R.string.navigation_draw_open,R.string.navigation_draw_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        auth = FirebaseAuth.getInstance();


        user = auth.getCurrentUser();


        drawer = findViewById(R.id.drawer_layout);
        menu= findViewById(R.id.menu);
        Menu=findViewById(R.id.Menu);
        Guide=findViewById(R.id.Guidebook);
        Aaccount=findViewById(R.id.account);



        textname= findViewById(R.id.fullnametxt);
        textmobile=findViewById(R.id.mobiletxt);
        textemail=findViewById(R.id.emailtxt);
        textdevkey=findViewById(R.id.devkeytxt);
        progressBar=findViewById(R.id.progressBar);

        authprofile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= authprofile.getCurrentUser();


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawer);
            }
        });

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Account.this,Menu.class);
            }
        });


        Guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(Account.this,Guide.class);
            }
        });



        Aaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });




        if(firebaseUser==null){
            Toast.makeText(Account.this,"User details are not available",Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }



        //showUserData();





Button button2 =findViewById(R.id.logoutbut);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authprofile.signOut();
                Toast.makeText(Account.this,"User has been signout",Toast.LENGTH_LONG).show();
                Intent intent =new Intent(Account.this, Loginscreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent. FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

























    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID= firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails= snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails != null){
                    name= readUserDetails.fullname;
                    email=firebaseUser.getEmail();
                    devkey=readUserDetails.devicekey;
                    mobile=readUserDetails.mobile;

                    textname.setText(name);
                    textemail.setText(email);
                    textmobile.setText(mobile);
                    textdevkey.setText(devkey);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Account.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });


    }


  /*  public void showUserData(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Intent intent= getIntent();

        String nameUser = sharedPreferences.getString("usrname", "");
        String passUser = sharedPreferences.getString("password", "");
        String devkeyUser = sharedPreferences.getString("devicekey", "");

        text1.setText("Username: "+nameUser);
        text2.setText("Password: "+passUser);
        text3.setText("Device Key: "+devkeyUser);



    } */



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