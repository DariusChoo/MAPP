package com.example.mapp_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapp_assignment.adapters.YourGroupRecyclerAdapter;

import java.util.ArrayList;

public class GroupFragment extends Fragment{

    private static final String TAG = "GroupFragment";
    View rootView;

    // temp vars, insert firebase values to this arrayList
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private final String defaultProfileImageUrl = "https://firebasestorage.googleapis.com/v0/b/crux-23cf1.appspot.com/o/default%2Fdefault_proifle_img.jpg?alt=media&token=9e65875e-c926-402f-8cbe-2ef69cc50ce5";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_group, container, false);

        TextView textView = (TextView) rootView.findViewById(R.id.text_view_create_group);
//        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_black_24dp, 0, 0, 0);

       //Create group button
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = new CreateGroupFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        // Display Popular Events through RecyclerView
        getImages();

        return rootView;
    }


    private void getImages() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        //mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        mImageUrls.add("https://dotesports-media.nyc3.cdn.digitaloceanspaces.com/wp-content/uploads/2019/09/12195522/league-of-legends.jpg");
        mNames.add("League of Legend group");

        mImageUrls.add(defaultProfileImageUrl);
        mNames.add("Cat gang lovers");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Sea Lovers");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("TODAY 11:00 PM");


        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("TODAY 11:00 PM");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("TODAY 11:00 PM");


        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("TODAY 11:00 PM");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("TODAY 11:00 PM");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("TODAY 11:00 PM");

        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        YourGroupRecyclerAdapter adapter = new YourGroupRecyclerAdapter(getContext(), mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }

}
