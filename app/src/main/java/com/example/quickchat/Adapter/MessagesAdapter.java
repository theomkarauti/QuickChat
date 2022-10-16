package com.example.quickchat.Adapter;

import static com.example.quickchat.Activity.ChatActivity.rImage;
import static com.example.quickchat.Activity.ChatActivity.sImage;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickchat.Model.Message;
import com.example.quickchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder > {



    Context context;
    ArrayList <Message> messageArrayList;
    int ITEM_SEND= 1;
    int ITEM_RECIVE = 2;
    private static final String TAG = "Activity:";

    public MessagesAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
        Toast.makeText(context.getApplicationContext(), "messageAdapter called...", Toast.LENGTH_SHORT).show();


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==ITEM_SEND)
        {

            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);

            return new sendrViewHolder(view);

        }else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_receiver_item,parent,false);

            return new receiverViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message message = messageArrayList.get(position);

        //Load  Message nd Profile Image

        if(holder.getClass()==sendrViewHolder.class)
        {
            Log.d("SenderMsg",""+message.getMessage());
            sendrViewHolder viewHolder = (sendrViewHolder) holder;
            viewHolder.sendermessage.setText(message.getMessage());
            Picasso.get().load(sImage).into(viewHolder.circleImageView);

        }else {

            Log.d("ReceierMsg",""+message.getMessage());
            receiverViewHolder viewHolder = (receiverViewHolder) holder;
            viewHolder.receivermsg.setText(message.getMessage());
            Picasso.get().load(rImage).into(viewHolder.circleImageView);


        }

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

        Message message=messageArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderID()))
        {
            return  ITEM_SEND;
        }else
        {
            return ITEM_RECIVE;
        }
    }

    class sendrViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView sendermessage;


        public sendrViewHolder(@NonNull View itemView) {
            super(itemView);

            Toast.makeText(itemView.getContext(), "sendrViewHolder", Toast.LENGTH_SHORT).show();

            circleImageView = itemView.findViewById(R.id.profile_image);
            sendermessage = itemView.findViewById(R.id.sendermessage);
        }
    }


    class receiverViewHolder extends  RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView receivermsg;

        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile_image);
            receivermsg = itemView.findViewById(R.id.receivermsg);

        }
    }

}
