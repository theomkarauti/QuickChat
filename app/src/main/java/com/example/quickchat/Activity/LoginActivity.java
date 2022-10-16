package com.example.quickchat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView signup,signIn;
    EditText email,cPass;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        email = findViewById(R.id.email);
        cPass =findViewById(R.id.cPass);
        signIn =findViewById(R.id.signbtn);
        auth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                String reg_email = email.getText().toString();
                String reg_cPass = cPass.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



                if(TextUtils.isEmpty(reg_email) || TextUtils.isEmpty(reg_cPass))
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Enter Valid Data...", Toast.LENGTH_SHORT).show();

                }else if ( ! reg_email.matches(emailPattern))
                {
                    progressDialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Enter Valid Mail....", Toast.LENGTH_SHORT).show();

                }else if(reg_cPass.length()<6)
                {
                    progressDialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Please Enter Valid Password...", Toast.LENGTH_SHORT).show();
                }
                else {

                    //Authorised User SignIn......
                    auth.signInWithEmailAndPassword(reg_email, reg_cPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                progressDialog.dismiss();

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                Toast.makeText(getApplicationContext(), "LogIn Successful...!!", Toast.LENGTH_SHORT).show();

                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(getApplicationContext(), "Error in LogIn...", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }


            }
        });



        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this,RegisteredActivity.class);
                startActivity(i);
                finish();
            }
        });







    }
}