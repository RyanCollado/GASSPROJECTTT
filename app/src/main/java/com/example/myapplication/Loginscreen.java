package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Loginscreen extends AppCompatActivity {
    EditText usrnameEditText,passwordEditText,dvckeyEditText;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),MainMenu.class);
            startActivity(intent);
            finish();
        }
    }

    Button buttonLogin,registerbut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);

        registerbut = findViewById(R.id.register);
        buttonLogin = findViewById(R.id.buttonlog);


        mAuth=FirebaseAuth.getInstance();

        usrnameEditText=findViewById(R.id.usrname);
        passwordEditText=findViewById(R.id.password);
        progressBar= findViewById(R.id.progressbar);


        //REGISTER BUTTON
        registerbut.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
             Intent intent = new Intent (Loginscreen.this, MainActivity.class);
             startActivity(intent);
    }
});
        //LOGIN BUTTON

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String usrname,password;
                usrname = usrnameEditText.getText().toString();
                password = passwordEditText.getText().toString();


                if (TextUtils.isEmpty(usrname)){
                    Toast.makeText(Loginscreen.this, "One of the fields is empty", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Loginscreen.this, "One of the fields is empty", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
//FIREBASE CODE
                mAuth.signInWithEmailAndPassword(usrname, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                   Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                   Intent intent = new Intent(getApplicationContext(),MainMenu.class);
                                   startActivity(intent);
                                   finish();
                                } else {

                                    Toast.makeText(Loginscreen.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
    }
}