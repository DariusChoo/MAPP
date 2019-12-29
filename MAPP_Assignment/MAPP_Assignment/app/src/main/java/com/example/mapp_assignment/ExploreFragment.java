package com.example.mapp_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapp_assignment.adapters.HorizontalRecyclerViewAdapter;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {

    private static final String TAG = "ExploreFragment";

    View rootView;
    Toolbar tb;
    ScrollView scrollView;
    GridView gridview;
    String[] categoryNames = {"Outdoors","Gaming","Technology","Music","Learning","Art"};
    int[] categoryIcon = {R.drawable.mountainco,R.drawable.gamecontrollerico,R.drawable.techico,R.drawable.musicico,R.drawable.learningico,R.drawable.artico};

    // temp vars, insert firebase values to this arrayList
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        // Fix the starting view at top
        scrollView = rootView.findViewById(R.id.scrollExplore);
        scrollView.smoothScrollTo(0, 0);


        // Display Popular Events through RecyclerView
        getImages();
        // Initialize GridView
        initGridView();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ExploreCategoryActivity.class);
                intent.putExtra("name", categoryNames[i]);
                intent.putExtra("images", categoryIcon[i]);

                startActivity(intent);
            }
        });

        return rootView;
    }

    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        mNames.add("TODAY 11:00 PM");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("TODAY 11:00 PM");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("TODAY 11:00 PM");

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

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        HorizontalRecyclerViewAdapter adapter = new HorizontalRecyclerViewAdapter(getContext(), mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }

    private void initGridView(){
        gridview = rootView.findViewById(R.id.gridview);
        CustomAdapter customAdapter = new CustomAdapter();
        gridview.setAdapter(customAdapter);
    }


    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount(){
            return categoryIcon.length;
        }

        @Override
        public Object getItem(int i){
            return null;
        }

        @Override
        public long getItemId(int i){
            return 0;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup){
            View view1 = getLayoutInflater().inflate(R.layout.explore_cat_row_data,null);

            TextView name = view1.findViewById(R.id.categories);
            ImageView img = view1.findViewById(R.id.images);

            name.setText(categoryNames[i]);
            img.setImageResource(categoryIcon[i]);
            return view1;
        }
    }
}
