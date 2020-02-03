package com.example.mapp_assignment.adapters;

import android.content.Context;
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

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context mContext;
    private List<Chat> mChats;

    public ChatAdapter(Context mContext, List<Chat> mChats){
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

        holder.chatname.setText(chat.getGrpName());
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
        public ImageView chat_image;

        public ViewHolder(View itemView)
        {
            super(itemView);
            chatname = itemView.findViewById(R.id.chat_name);
            chat_image = itemView.findViewById(R.id.chat_image);
        }

    }

}
