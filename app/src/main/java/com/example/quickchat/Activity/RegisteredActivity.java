package com.example.quickchat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickchat.R;
import com.example.quickchat.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisteredActivity extends AppCompatActivity {

    Uri imageUri;

    FirebaseAuth auth;

    FirebaseDatabase database;

    FirebaseStorage storage;

    DatabaseReference reference;

    EditText reg_name,reg_password,reg_emil,reg_cPassword;
    TextView signIn,reg_btn;
    CircleImageView profile_image;
    String imageURI;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        database = FirebaseDatabase.getInstance();
          storage= FirebaseStorage.getInstance();


        reg_name = findViewById(R.id.reg_name);
        reg_emil = findViewById(R.id.reg_email);
        reg_password = findViewById(R.id.reg_pass);
        reg_cPassword = findViewById(R.id.reg_cPass);

        signIn = findViewById(R.id.signin);
        reg_btn = findViewById(R.id.reg_btn);
        profile_image = findViewById(R.id.profile_image) ;
        auth= FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        progressDialog.setCancelable(false);

        String status = "status chod DP dekh..!!";


        //registered button on click listner.....

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                Toast.makeText(getApplicationContext(), "Reg btn press", Toast.LENGTH_SHORT).show();

                String name = reg_name.getText().toString();
                String email = reg_emil.getText().toString();
                String password = reg_password.getText().toString();
                String cPassword = reg_cPassword.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String status = "status chod DP dekh..!!";


                // validation

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(cPassword))
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Plz Entered Valid Data.... ", Toast.LENGTH_SHORT).show();
                }else if ( ! email.matches(emailPattern) )
                {
                    reg_emil.setError("Please Enter Valid Email...");
                    Toast.makeText(RegisteredActivity.this,"Plz Enter Valid Email",Toast.LENGTH_SHORT);

                }else if(!password.equals(cPassword) )
                {
                    progressDialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();

                }else
                    if ( password.length()< 6){
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Enter 6 Character Password", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        // user creation with email nd password....

                        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                                    StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());



                                    if(imageUri!=null)
                                    {

                                        storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                                if(task.isSuccessful())
                                                {

                                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {

                                                          imageURI= uri.toString();
                                                          Users users = new Users(auth.getUid(),name,email,imageURI,status);
                                                          reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                              @Override
                                                              public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful()){
                                                                progressDialog.dismiss();

                                                                Toast.makeText(getApplicationContext(), "User Created succefully..", Toast.LENGTH_SHORT).show();

                                                                startActivity(new Intent(RegisteredActivity.this,HomeActivity.class));
                                                            }
                                                            else{

                                                                Toast.makeText(getApplicationContext(), "Error in creating new Users..", Toast.LENGTH_SHORT).show();
                                                            }

                                                              }
                                                          });

                                                        }
                                                    });
                                                }else {

                                                    String status = "status chod DP dekh..!!";
                                                    imageURI= "https://firebasestorage.googleapis.com/v0/b/quickchat-4575f.appspot.com/o/User-Profile-PNG-High-Quality-Image.png?alt=media&token=40483b81-4acb-4b44-a917-a0149ed02024";
                                                    Users users = new Users(auth.getUid(),name,email,imageURI,status);
                                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful()){
                                                                progressDialog.dismiss();

                                                                startActivity(new Intent(RegisteredActivity.this,HomeActivity.class));
                                                            }
                                                            else{

                                                                Toast.makeText(getApplicationContext(), "Error in creating new Users..", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    });}
                                            }
                                        });
                                    }



                                }

                            }
                        });
                    }


            }
        });




        //SignIn button On ClickListner......
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              startActivity(new Intent(RegisteredActivity.this,LoginActivity.class));
            }
        });




        //Import image from phome gallry....


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture:"), 10);
            }
        });


    }
//image Picker..
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10)
        {
            if(data!=null)
            {

                imageUri = data.getData();
                profile_image.setImageURI(imageUri);



            }


        }

    }
}
