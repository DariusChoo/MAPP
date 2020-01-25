package com.example.mapp_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapp_assignment.adapters.YourGroupRecyclerAdapter;
import com.example.mapp_assignment.models.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GroupFragment extends Fragment {

    private static final String TAG = "GroupFragment";
    private View rootView;
    private TextView mCreateGroup;
    private TextView mNoGroupTextView;
    private TextView mNoGroupGoExplore;
    private ProgressBar mProgressCircle;

    private String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    // temp vars, insert firebase values to this arrayList
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mGroupId = new ArrayList<>();

    private final String defaultProfileImageUrl = "https://firebasestorage.googleapis.com/v0/b/crux-23cf1.appspot.com/o/default%2Fdefault_proifle_img.jpg?alt=media&token=9e65875e-c926-402f-8cbe-2ef69cc50ce5";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_group, container, false);

        mCreateGroup = (TextView) rootView.findViewById(R.id.text_view_create_group);
        mNoGroupTextView = rootView.findViewById(R.id.text_view_no_group);
        mNoGroupGoExplore = rootView.findViewById(R.id.text_view_go_explore);

        // Initialize Firebase Connection
        initFirebaseConnection();
        // Display Popular Events through RecyclerView
        loadGroupData();
        // Initialize OnclickListener in this activty
        initOnclickLister();

        return rootView;
    }


    private void initFirebaseConnection() {
        // Get instance of firebase authentication
        fAuth = FirebaseAuth.getInstance();
        // Get instance of firebase firestore
        fStore = FirebaseFirestore.getInstance();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        YourGroupRecyclerAdapter adapter = new YourGroupRecyclerAdapter(getActivity(), mNames, mImageUrls, mGroupId);
        recyclerView.setAdapter(adapter);
    }

    private void initOnclickLister() {
        //Create group button
        mCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = new CreateGroupFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_down,R.anim.fade_out,R.anim.fade_in,R.anim.fade_out)
                        .replace(R.id.fragment_container, selectedFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        // Go to explore
        mNoGroupGoExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = new ExploreFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    private void loadGroupData() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        // Get User id
        String userId = fAuth.getCurrentUser().getUid();
        // Query group data
        fStore.collection("groups")
                .whereArrayContains("membersId", userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        mNames.clear();
                        mImageUrls.clear();
                        mGroupId.clear();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.d(TAG, "document size " + queryDocumentSnapshots.size());
                            if (queryDocumentSnapshots.size() == 0) {
                                // Appear default view
                                mNoGroupTextView.setVisibility(View.VISIBLE);
                                mNoGroupGoExplore.setVisibility(View.VISIBLE);
                            } else {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Group group = document.toObject(Group.class);
                                mImageUrls.add(group.getImageURL());
                                mNames.add(group.getGroupName());
                                mGroupId.add(document.getId());
                            }
                        }
                        initRecyclerView();
                    }
                });
    }
}
