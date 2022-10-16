package com.example.quickchat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickchat.Adapter.MessagesAdapter;
import com.example.quickchat.Model.Message;
import com.example.quickchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String  ReciverUID, ReciverName, profileImageString, SenderUID;  // thi
    CircleImageView profileImage;
    TextView reciverName;
    FirebaseDatabase database;  //
    FirebaseAuth auth;
    public static String sImage;
    public static String rImage;
    CardView sendBtn;
    DatabaseReference reference;
    EditText edtMessage;
    String senderRoom, reciverRoom;
//    DatabaseReference chatReference;
    RecyclerView rvMessages;

    ArrayList<Message> messageArrayList;
    private static final String TAG = "Activity:";

    MessagesAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);





        ReciverName = getIntent().getStringExtra("name");

        profileImageString = getIntent().getStringExtra("ReceiverImage");

        ReciverUID = getIntent().getStringExtra("uid");

        profileImage = findViewById(R.id.profile_image);
        reciverName = findViewById(R.id.uname);

        Picasso.get().load(profileImageString).into(profileImage);
        reciverName.setText("" + ReciverName);




        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

         reference = database.getReference().child("user").child(auth.getUid());


        // Recycler view of Chats...

        messageArrayList = new ArrayList<>();

        rvMessages = findViewById(R.id.rvmessageAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(linearLayoutManager);
        messagesAdapter =  new MessagesAdapter(ChatActivity.this,messageArrayList);
        rvMessages.setAdapter(messagesAdapter);


        //rvMessages.setLayoutManager(linearLayoutManager);

        //messagesAdapter = new MessagesAdapter(getApplicationContext(),messageArrayList);

        //Log.d(TAG,"Class");

        //rvMessages.setAdapter(messagesAdapter);


        SenderUID = auth.getUid();

        senderRoom = SenderUID + ReciverUID;
        reciverRoom = ReciverUID + SenderUID;

//        chatReference = database.getReference().child("chats").child(auth.getUid()).child("message");
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("message");
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshpt",""+snapshot.toString());

                messageArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("snapshot1",""+dataSnapshot.getValue());
                    Message message = dataSnapshot.getValue(Message.class);
                    messageArrayList.add(message);
                    Log.d("mymessage:",message.getMessage());


                }

                messagesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        
        
        sendBtn = findViewById(R.id.sendBtn);
        edtMessage = findViewById(R.id.edtMessage);



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                sImage = snapshot.child("imageUri").getValue().toString();
                rImage = profileImageString;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = edtMessage.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter  Message...", Toast.LENGTH_SHORT).show();
                    return;
                }
                edtMessage.setText("");
                Date date = new Date();
                Message message1 = new Message(message, auth.getUid(), date.getTime());


                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("message")
                        .push().setValue(message1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        database.getReference().child("chats")
                                .child(reciverRoom)
                                .child("message")
                                .push().setValue(message1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                            }
                        });


                    }
                });


            }
        });


    }
}