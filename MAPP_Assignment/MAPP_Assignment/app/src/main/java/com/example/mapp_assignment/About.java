package com.example.mapp_assignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class About extends AppCompatActivity  {
    View rootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_about);

        TextView aboutCrux = (TextView)findViewById(R.id.details1);

        aboutCrux.setText("Crux is a platform for finding and building local communities. People use Crux to meet new people," +
                " learn new things, find support, get out of their comfort zones" +
                ", and pursue their passions, together.");

        TextView aboutBasic = (TextView)findViewById(R.id.details2);

        aboutBasic.setText("User can Join or Create group base on their interests" +
                " Once Group is joined or created" +
                ", User able to join or create event");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About Crux");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                Fragment selectedFragment = new ProfileFragment();

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left,R.anim.slide_in_left,R.anim.slide_out_right)
                        .replace(R.id.fragment_container, selectedFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



