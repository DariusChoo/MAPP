package com.example.mapp_assignment;

import android.content.Intent;
import android.os.Bundle;
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

public class ExploreFragment extends Fragment {

    Toolbar tb;
    ScrollView scrollView;
    GridView gridview;
    String[] categoryNames = {"Outdoors","Gaming","Technology","Music","Learning","Art"};
    int[] categoryIcon = {R.drawable.mountainco,R.drawable.gamecontrollerico,R.drawable.techico,R.drawable.musicico,R.drawable.learningico,R.drawable.artico};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_explore, container, false);
        // Fix the starting view at top
        scrollView = RootView.findViewById(R.id.scrollExplore);
        scrollView.smoothScrollTo(0, 0);

        gridview = RootView.findViewById(R.id.gridview);

        CustomAdapter customAdapter = new CustomAdapter();
        gridview.setAdapter(customAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ExploreCategoryActivity.class);
                intent.putExtra("name", categoryNames[i]);
                intent.putExtra("images", categoryIcon[i]);

                startActivity(intent);
            }
        });

        return RootView;
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
