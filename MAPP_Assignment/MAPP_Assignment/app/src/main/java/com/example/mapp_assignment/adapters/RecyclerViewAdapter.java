package com.example.mapp_assignment.adapters;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.mapp_assignment.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
//
//    private static final String TAG = "RecyclerViewAdapter";
//    private ArrayList<String> mImageNames = new ArrayList<>();
//    private ArrayList<String> mImages = new ArrayList<>();
//    private Context mContext;
//
//
//    public RecyclerViewAdapter(ArrayList<String> mImageNames, ArrayList<String> mImages,Context mContext) {
//        this.mImageNames = mImageNames;
//        this.mImages = mImages;
//        this.mContext = mContext;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // change inflated layout
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_popular_event_layout,parent,false);
//        ViewHolder holder = new ViewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        Log.d(TAG, "onBindViewHolder: called.");
//
//        Glide.with(mContext)
//                .asBitmap()
//                .load(mImages.get(position))
//                .into(holder.image);
//
//        holder.imageName.setText(mImageNames.get(position));
//
//
//        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: clicked on: " + mImageNames.get(position));
//                Toast.makeText(mContext,mImageNames.get(position), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mImageNames.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//
//        CircleImageView image;
//        TextView imageName;
//        RelativeLayout parentLayout;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            image = itemView.findViewById(R.id.image);
//            imageName = itemView.findViewById(R.id.image_name);
//            parentLayout = itemView.findViewById(R.id.parent_layout);
//        }
//    }
//}