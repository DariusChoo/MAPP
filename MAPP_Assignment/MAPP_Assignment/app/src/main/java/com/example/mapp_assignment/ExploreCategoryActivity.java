package com.example.mapp_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
