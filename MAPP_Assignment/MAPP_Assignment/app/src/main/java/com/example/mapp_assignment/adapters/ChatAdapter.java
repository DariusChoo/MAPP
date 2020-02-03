package com.example.mapp_assignment.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mapp_assignment.R;
import com.example.mapp_assignment.models.Chat;
import com.example.mapp_assignment.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Chat> mChats;

    public ChatAdapter(Context mContext, ArrayList<Chat> mChats){
        this.mChats= mChats;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_user, parent, false);
        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = mChats.get(position);
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
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView chatname;
        public TextView lastMsg;
        public TextView timestamp;
        public ImageView chat_image;


        public ViewHolder(View itemView)
        {
            super(itemView);
            lastMsg = itemView.findViewById(R.id.lastMsg);
            chatname = itemView.findViewById(R.id.chat_name);
            chat_image = itemView.findViewById(R.id.chat_image);
            timestamp = itemView.findViewById(R.id.timestamp);
        }

    }

}
