package com.example.mapp_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ExploreCategoryActivity extends AppCompatActivity {

    TextView gridData;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.explore_cat_activity_grid_item);

        gridData = findViewById(R.id.griddata);
        imageView = findViewById(R.id.imageView);
        Intent intent = getIntent();
        String receivedName =  intent.getStringExtra("name");
        Log.d("ExpCatAct","Received name: "+receivedName);
        int receivedImage = intent.getIntExtra("image",0);

        gridData.setText(receivedName);
        imageView.setImageResource(receivedImage);
        //enable back Button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}
