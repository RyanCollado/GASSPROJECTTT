package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Loginscreen extends AppCompatActivity {




   EditText usrnameEditText,passwordEditText;
    ProgressBar progressBar;
FirebaseAuth authProfile;


static final String TAG= "Loginscreen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);


        usrnameEditText= findViewById(R.id.editText_login_email);
        passwordEditText=findViewById(R.id.editText_login_password);
        progressBar=findViewById(R.id.progressBar);
        authProfile= FirebaseAuth.getInstance();

        TextView reg=findViewById(R.id.linkreg);

reg.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(Loginscreen.this,Register.class));
    }
});





        Button buttonlogin = findViewById(R.id.button_login);
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail= usrnameEditText.getText().toString();
                String textPass= passwordEditText.getText().toString();



                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(Loginscreen.this,"Please enter your email",Toast.LENGTH_SHORT).show();
                    usrnameEditText.setError("Email is required");
                    usrnameEditText.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    usrnameEditText.setError("Valid Email is required");
                    usrnameEditText.requestFocus();
                    Toast.makeText(Loginscreen.this,"Please enter your valid email",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(textPass)){
                    Toast.makeText(Loginscreen.this,"Please enter your password",Toast.LENGTH_SHORT).show();
                    passwordEditText.setError("Password is required");
                    passwordEditText.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail,textPass);
                }
            }
        });




























/*
        registerbut = findViewById(R.id.register);
        buttonLogin = findViewById(R.id.buttonlog);

        usrnameEditText=findViewById(R.id.Email);
        passwordEditText=findViewById(R.id.password);
        progressBar= findViewById(R.id.progressbar);



        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (!validateUsername()| !validatePassword()){

                } else{
                    checkUser();
                }
            }



        });



        //REGISTER BUTTON
        registerbut.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
             Intent intent = new Intent (Loginscreen.this, Register.class);
             startActivity(intent);
    }
});




    } */

    /*
    public Boolean validateUsername(){
        String val = usrnameEditText.getText().toString();

        if (val.isEmpty()){

            usrnameEditText.setError("Username cannot be empty");
            return false;
        } else{
            usrnameEditText.setError(null);
            return true;
        }
    }




    public Boolean validatePassword(){
        String val = passwordEditText.getText().toString();

        if (val.isEmpty()){

            passwordEditText.setError("Password cannot be empty");
            return false;
        } else{
            passwordEditText.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String UserEmail= usrnameEditText.getText().toString().trim();
        String UserPass= passwordEditText.getText().toString().trim();


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("usrname"). equalTo(UserEmail );

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    usrnameEditText.setError(null);
                    String passwordFromDB= snapshot.child(UserEmail).child("password").getValue(String.class);
                    String encodedEmail = UserEmail.replace(".", "_").replace("@", "_");
                    if (passwordFromDB.equals(UserPass)){
                        usrnameEditText.setError(null);

                        String nameFromDB = snapshot.child(encodedEmail).child("usrname").getValue(String.class);
                        String devkeyFromDB = snapshot.child(encodedEmail).child("devicekey").getValue(String.class);


                        Intent intent = new Intent(Loginscreen.this, Account.class);

                        intent.putExtra("usrname", nameFromDB);
                        intent.putExtra("password", passwordFromDB);
                        intent.putExtra("devicekey", devkeyFromDB);



                        saveDataToSharedPreferences(UserEmail, passwordFromDB,devkeyFromDB);

                        startActivity(intent);
                    } else{
                        passwordEditText.setError("Invalid Credentials");
                        passwordEditText.requestFocus();

                    }
                } else{
                    usrnameEditText.setError("User does not exist");
                    usrnameEditText.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void saveDataToSharedPreferences(String userEmail, String password, String devicekey) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("usrname", userEmail);
        editor.putString("password", password);
        editor.putString("devicekey",devicekey);
        editor.apply();
    }

     */
}

    private void loginUser(String textEmail, String textPass) {

        authProfile.signInWithEmailAndPassword(textEmail,textPass).addOnCompleteListener(Loginscreen.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Loginscreen.this,"You are logged in now",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(Loginscreen.this, Menu.class));
                    finish();

                } else{

                    try{
                        throw task.getException();
                    } catch(FirebaseAuthInvalidUserException e){
                        usrnameEditText.setError("User does not exists or is not longer valid");
                        usrnameEditText.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        usrnameEditText.setError("Invalid credentials");
                        usrnameEditText.requestFocus();

                    } catch (Exception e){
                        Log.e(TAG, e.getMessage() );
                        Toast.makeText(Loginscreen.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(Loginscreen.this, "Something went wrong",Toast.LENGTH_SHORT).show();

                }
                progressBar.setVisibility(View.GONE);


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(authProfile.getCurrentUser() !=null){
            Toast.makeText(Loginscreen.this, "Already logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Loginscreen.this, Menu.class));
            finish();

        }
        else{
            Toast.makeText(Loginscreen.this, "You can log in now", Toast.LENGTH_SHORT).show();
        }
    }
}
