package com.example.mapp_assignment.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mapp_assignment.R;
import com.example.mapp_assignment.models.Event;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventTabRecyclerAdapter extends RecyclerView.Adapter<EventTabRecyclerAdapter.ViewHolder>{
        private static final String TAG = "EventTabRecyclerAdapter";
    private ArrayList<Event> events = new ArrayList<>();
    private FragmentActivity mFragmentActivity;


    public EventTabRecyclerAdapter(FragmentActivity fragmentActivity, ArrayList<Event> events) {
        this.events = events;
        mFragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // change inflated layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_event_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

//        Glide.with(mFragmentActivity)
//                .asBitmap()
//                .load(mImages.get(position))
//                .into(holder.image);

        holder.event_date.setText(events.get(position).getEventDate());
        holder.group_name.setText(events.get(position).getGroupName());
        holder.event_name.setText(events.get(position).getEventName());
        holder.event_location.setText(events.get(position).getLocation());


//        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: clicked on: " + mImageNames.get(position));
//                Toast.makeText(mContext,mImageNames.get(position), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView event_date;
        TextView group_name;
        TextView event_name;
        TextView event_location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            event_date = itemView.findViewById(R.id.text_view_event_date);
            group_name = itemView.findViewById(R.id.text_view_group_name);
            event_name = itemView.findViewById(R.id.text_view_eventname);
            event_location = itemView.findViewById(R.id.text_view_event_location);
        }
    }
}
