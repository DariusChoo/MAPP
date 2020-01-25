package com.example.mapp_assignment.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.example.mapp_assignment.GroupDetailFragment;
import com.example.mapp_assignment.R;

import java.util.ArrayList;

public class YourGroupRecyclerAdapter extends RecyclerView.Adapter<YourGroupRecyclerAdapter.ViewHolder> {

    private static final String TAG = "HorizontalRecyclerViewAdapter";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mGroupId = new ArrayList<>();
    private FragmentActivity mFragmentActivity;

    public YourGroupRecyclerAdapter(FragmentActivity fragmentActivity, ArrayList<String> names, ArrayList<String> imageUrls, ArrayList<String> id) {
        mNames = names;
        mImageUrls = imageUrls;
        mFragmentActivity = fragmentActivity;
        mGroupId = id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_your_group_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mFragmentActivity)
                .asBitmap()
                .load(mImageUrls.get(position))
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(holder.image);

        holder.name.setText(mNames.get(position));

//        holder.image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: clicked on an image: " + mNames.get(position));
//                Toast.makeText(mFragmentActivity, mNames.get(position), Toast.LENGTH_SHORT).show();
//
////                Fragment selectedFragment = new GroupDetailFragment();
////                mContext.getSupportFragmentManager().beginTransaction()
////                        .replace(R.id.fragment_container, selectedFragment, "findThisFragment")
////                        .addToBackStack(null)
////                        .commit();
//            }
//        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle data = new Bundle();
                data.putString("groupId", mGroupId.get(position));

                Fragment selectedFragment = new GroupDetailFragment();
                selectedFragment.setArguments(data);
                mFragmentActivity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left,R.anim.slide_in_left,R.anim.slide_out_right)
                        .replace(R.id.fragment_container, selectedFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view_group_image);
            name = itemView.findViewById(R.id.text_view_group_name);
        }
    }
}
