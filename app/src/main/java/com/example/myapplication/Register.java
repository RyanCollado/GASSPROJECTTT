package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity   {

EditText usrnameEditText,emailEditText,passwordEditText,confirmpasswordEditText,devkeyEditText,mobileEditText;
ProgressBar progressBar;

CheckBox checkbox;

FirebaseDatabase database;
DatabaseReference reference;
TextView regis;
MaterialAlertDialogBuilder materialAlertDialogBuilder;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        Toast.makeText(Register.this,"You can register now", Toast.LENGTH_LONG).show();

        progressBar = findViewById(R.id.progressBar);
        usrnameEditText = findViewById(R.id.editText_register_full_name);
        emailEditText = findViewById(R.id.editText_register_email);
        passwordEditText= findViewById(R.id.editText_register_password);
        confirmpasswordEditText=findViewById(R.id.editText_register_confirm_password);
        devkeyEditText=findViewById(R.id.editText_register_devicekey);
        mobileEditText= findViewById(R.id.editText_register_mobile);

        checkbox =findViewById(R.id.checkbx);
        regis=findViewById(R.id.linklog);
        materialAlertDialogBuilder= new MaterialAlertDialogBuilder(this);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    materialAlertDialogBuilder.setTitle("Terms and Conditions");
                    materialAlertDialogBuilder.setMessage("1. Allow the app to have access on your Wi-Fi for notifications\n2.Allow the app to have your mobile number for emergency purposes\n3. Allow the app to have your email for emergency purposes");
                    materialAlertDialogBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                        }
                    });

                    materialAlertDialogBuilder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            checkbox.setChecked (false);
                        }
                    });
                    materialAlertDialogBuilder.show();

                }
            }
        });
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Register.this,Loginscreen.class));

            }
        });


        Button buttonregister = findViewById(R.id.button_register);
        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textFullName= usrnameEditText.getText().toString();
                String textEmail= emailEditText.getText().toString();
                String textpassword= passwordEditText.getText().toString();
                String textconfirmpassword= confirmpasswordEditText.getText().toString();
                String textdevkey= devkeyEditText.getText().toString();
                String textmobile= mobileEditText.getText().toString();


                if(TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(Register.this,"Please enter your full name", Toast.LENGTH_LONG).show();
                    usrnameEditText.setError("Name is required");
                    usrnameEditText.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(Register.this,"Please enter your email", Toast.LENGTH_LONG).show();
                   emailEditText.setError("Email is required");
                    emailEditText.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(Register.this,"Please re-enter your email", Toast.LENGTH_LONG).show();
                    emailEditText.setError("Valid email is required");
                    emailEditText.requestFocus();
                }else if (TextUtils.isEmpty(textmobile)){
                    Toast.makeText(Register.this,"Please enter your mobile number", Toast.LENGTH_LONG).show();
                    mobileEditText.setError("Mobile No. is required");
                    mobileEditText.requestFocus();
                }else if (textmobile.length() != 10){
                    Toast.makeText(Register.this,"Please re-enter you mobile number", Toast.LENGTH_LONG).show();
                    mobileEditText.setError("Mobile No. should be 10 digits");
                    mobileEditText.requestFocus();
                }else if (TextUtils.isEmpty(textpassword)){
                    Toast.makeText(Register.this,"Please enter your password", Toast.LENGTH_LONG).show();
                    passwordEditText.setError("Password is required");
                   passwordEditText.requestFocus();

                }else if (textpassword.length() != 6) {
                    Toast.makeText(Register.this, "Please re-enter you password", Toast.LENGTH_LONG).show();
                    passwordEditText.setError("Password should be at least 6 digits");
                    passwordEditText.requestFocus();
                }else if (TextUtils.isEmpty(textconfirmpassword)){
                    Toast.makeText(Register.this,"Please confirm your password", Toast.LENGTH_LONG).show();
                    confirmpasswordEditText.setError("Password Confirmation is required");
                    confirmpasswordEditText.requestFocus();

                }else if (!textpassword.equals(textconfirmpassword)){
                    Toast.makeText(Register.this,"Password is not identical", Toast.LENGTH_LONG).show();
                    confirmpasswordEditText.setError("Password Confirmation is required");
                    confirmpasswordEditText.requestFocus();

                    passwordEditText.clearComposingText();
                    confirmpasswordEditText.clearComposingText();
                } else if (!checkbox.isChecked()){
                    Toast.makeText(Register.this,"Accept the terms and conditions", Toast.LENGTH_LONG).show();

                }else {
                  progressBar.setVisibility(View.VISIBLE);
                  registerUser(textFullName,textEmail,textpassword,textconfirmpassword,textmobile,textdevkey);
                }



            }












/*
        //mAuth=FirebaseAuth.getInstance();

        usrnameEditText=findViewById(R.id.usrname);
        passwordEditText=findViewById(R.id.password);
        dvckeyEditText=findViewById(R.id.dvckey);

        //progressBar= findViewById(R.id.progressbar);

        button= findViewById(R.id.register);
        button2= findViewById(R.id.buttonlog);

        //GO TO LOGIN BUTTON

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent (Register.this, Loginscreen.class);
                    startActivity(intent);
                    finish();
            }
        });

        //REGISTER BUTTON

button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       // progressBar.setVisibility(View.VISIBLE);
            String usrname,password,devicekey;
            usrname = usrnameEditText.getText().toString();
            password = passwordEditText.getText().toString();
            devicekey = dvckeyEditText.getText().toString();
        if (TextUtils.isEmpty(usrname) || TextUtils.isEmpty(password) || TextUtils.isEmpty(devicekey)) {
            Toast.makeText(Register.this, "One of the fields is empty", Toast.LENGTH_SHORT).show();
            return;
        }
            database= FirebaseDatabase.getInstance();
            reference= database.getReference("users");



        reference.orderByChild("usrname").equalTo(usrname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isDeviceKeyUnique = true;

                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String existingDeviceKey = userSnapshot.child("devicekey").getValue(String.class);
                        if (devicekey.equals(existingDeviceKey)) {
                            // The device key is not unique to this user's account
                            isDeviceKeyUnique = false;
                            break;
                        }
                    }
                }



        if (isDeviceKeyUnique){
                    String encodedUsername = usrname.replace(".", "_");
                    HelperClass helperClass = new HelperClass(usrname, password, devicekey);
                    reference.child(encodedUsername).setValue(helperClass);
                    Toast.makeText(Register.this, "You have signup successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Loginscreen.class);
                    startActivity(intent);


                } else {
            Toast.makeText(Register.this, "Device key is not unique", Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

    });


}


    })*/;







})


;}

    private void registerUser(String textFullName, String textEmail, String textpassword, String textconfirmpassword, String textmobile, String textdevkey) {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    auth.createUserWithEmailAndPassword(textEmail,textpassword).addOnCompleteListener(Register.this,
            new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        FirebaseUser firebaseuser= auth.getCurrentUser();




                        ReadWriteUserDetails writeUserDetails= new ReadWriteUserDetails(textFullName,textmobile,textdevkey);



                        DatabaseReference referenceProfile=FirebaseDatabase.getInstance().getReference("users");

                        referenceProfile.child(firebaseuser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if(task.isSuccessful()){
                                    firebaseuser.sendEmailVerification();
                                    Toast.makeText(Register.this, "User registered successfully",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Register.this, Loginscreen.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            |Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(intent);
                                    finish();
                                } else{
                                    Toast.makeText(Register.this, "User register failed",Toast.LENGTH_LONG).show();

                                }
                                progressBar.setVisibility(View.GONE);




                            }
                        });


                    }

                }
            });



    }
}