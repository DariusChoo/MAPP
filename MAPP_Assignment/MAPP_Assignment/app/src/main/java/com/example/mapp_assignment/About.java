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

public class About extends Fragment  {
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.profile_about, container, false);

        TextView aboutCrux = (TextView)rootView.findViewById(R.id.details1);

        aboutCrux.setText("Crux is a platform for finding and building local communities. People use Crux to meet new people," +
                " learn new things, find support, get out of their comfort zones" +
                ", and pursue their passions, together.");

        TextView aboutBasic = (TextView)rootView.findViewById(R.id.details2);

        aboutBasic.setText("User can Join or Create group base on their interests" +
                " Once Group is joined or created" +
                ", User able to join or create event");


        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("About Crux");
        setHasOptionsMenu(true);


        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStackImmediate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



