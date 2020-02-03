package com.example.mapp_assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapp_assignment.adapters.YourGroupRecyclerAdapter;

public class ExploreCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    TextView gridData;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore_cat_activity_grid_item);

        gridData = findViewById(R.id.griddata);
        imageView = findViewById(R.id.imageView);
        ImageButton backCat = findViewById(R.id.backCat);
        Intent intent = getIntent();
        String receivedName =  intent.getStringExtra("name");
        Log.d("ExpCatAct","Received name: "+receivedName);
        int receivedImage = intent.getIntExtra("image",0);

        gridData.setText(receivedName);
        imageView.setImageResource(receivedImage);

        //enable back Button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    private void initRecyclerView() {
//        Log.d("initRecyclerView","init recyclerview");
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(layoutManager);
//        YourGroupRecyclerAdapter adapter = new YourGroupRecyclerAdapter(getActivity(), mNames, mImageUrls, mGroupId);
//        recyclerView.setAdapter(adapter);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

}


    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.backCat:
                Intent i = new Intent(this, ExploreFragment.class);
                startActivity(i);
                break;

        }
    }
}
