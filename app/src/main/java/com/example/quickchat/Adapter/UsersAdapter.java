package com.example.quickchat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickchat.Activity.ChatActivity;
import com.example.quickchat.Activity.HomeActivity;
import com.example.quickchat.Model.Users;
import com.example.quickchat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.viewHolder> {

    Context homeActivity;
    ArrayList<Users> usersArrayList;


    public UsersAdapter(HomeActivity homeActivity, ArrayList<Users> usersArrayList) {

        this.homeActivity = homeActivity;
        this.usersArrayList = usersArrayList;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(homeActivity)
                .inflate(R.layout.item_user_row, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Users users = usersArrayList.get(position);
        holder.user_name.setText(users.name);
        holder.user_status.setText(users.status);
        Picasso.get().load(users.imageUri).into(holder.user_Profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "ButtonClicked..", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("name", users.getName());
                intent.putExtra("ReceiverImage", users.getImageUri());
                intent.putExtra("uid", users.getUid());

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  v.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {

        return usersArrayList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {

        CircleImageView user_Profile;
        TextView user_name;
        TextView user_status;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            user_Profile = itemView.findViewById(R.id.profile_image);
            user_name = itemView.findViewById(R.id.user_name);
            user_status = itemView.findViewById(R.id.status);

        }
    }


}


