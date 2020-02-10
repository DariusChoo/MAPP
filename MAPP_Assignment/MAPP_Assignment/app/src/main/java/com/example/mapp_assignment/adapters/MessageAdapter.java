package com.example.mapp_assignment.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mapp_assignment.ChatMessageActivity;
import com.example.mapp_assignment.R;
import com.example.mapp_assignment.models.Chat;
import com.example.mapp_assignment.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter {
    private Context mContext;
    //    private User user = new User();
    private Chat mChats;

    public MessageAdapter(Context mContext, Chat mChats){
        this.mChats= mChats;
        this.mContext = mContext;
    }

    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        final Chat chat = mChats.get(position);
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("E h:mm a");
        String setDate = "";

        Log.d("adapter", "onBindViewHolder: " + chat.getGrpName());
        holder.chatname.setText(chat.getGrpName());
        holder.lastMsg.setText(chat.getLastMsg());
        //targetFormat.parse(this.timestamp.toString());
        try {
            setDate = (targetFormat.format(inputFormat.parse((chat.getTimestamp()).toString()))).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("CHAT ADAPTER", "onBindViewHolder: SETDATE FAILED: "+e);
        }

        Log.d("CHAT ADAPTER", "onBindViewHolder: "+setDate);
        holder.timestamp.setText(setDate);

        if(chat.getImageUrl().equals("default"))
        {
            holder.chat_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext)
                    .load(chat.getImageUrl())
                    .into(holder.chat_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, ChatMessageActivity.class);
                intent.putExtra("chatId", chat.getGrpID());
                Log.d("PASSSING DATA","CHAT ID: "+chat.getGrpID());
                mContext.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_message_item, parent, false);
        return new MessageAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView messages;
        public TextView timestamp2;


        public ViewHolder(View itemView)
        {
            super(itemView);
            messages = itemView.findViewById(R.id.messages);
            timestamp2 = itemView.findViewById(R.id.timestamp2);
        }

    }
}
