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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity   {

EditText usrnameEditText,passwordEditText,dvckeyEditText;
ProgressBar progressBar;
FirebaseAuth mAuth;

Button button,button2;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

        usrnameEditText=findViewById(R.id.usrname);
        passwordEditText=findViewById(R.id.password);
        dvckeyEditText=findViewById(R.id.dvckey);

        progressBar= findViewById(R.id.progressbar);

        button= findViewById(R.id.register);
        button2= findViewById(R.id.buttonlog);

        //GO TO LOGIN BUTTON

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent (MainActivity.this, Loginscreen.class);
                    startActivity(intent);
                    finish();
            }
        });

        //REGISTER BUTTON

button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
            String usrname,password;
            usrname = usrnameEditText.getText().toString();
            password = passwordEditText.getText().toString();


            if (TextUtils.isEmpty(usrname)){
                Toast.makeText(MainActivity.this, "One of the fields is empty", Toast.LENGTH_SHORT).show();
                return;
            }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this, "One of the fields is empty", Toast.LENGTH_SHORT).show();
            return;
        }
//FIREBASE CODE
        mAuth.createUserWithEmailAndPassword(usrname, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Intent intent = new Intent (MainActivity.this, Loginscreen.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(MainActivity.this, "Account Successfully Created!.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }


});


    }
}