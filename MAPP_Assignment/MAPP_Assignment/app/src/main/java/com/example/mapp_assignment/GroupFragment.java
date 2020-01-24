package com.example.mapp_assignment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapp_assignment.adapters.YourGroupRecyclerAdapter;
import com.example.mapp_assignment.models.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GroupFragment extends Fragment {

    private static final String TAG = "GroupFragment";
    private View rootView;
    private TextView mCreateGroup;
    private TextView mNoGroupTextView;
    private TextView mNoGroupGoExplore;

    private String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    // temp vars, insert firebase values to this arrayList
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private final String defaultProfileImageUrl = "https://firebasestorage.googleapis.com/v0/b/crux-23cf1.appspot.com/o/default%2Fdefault_proifle_img.jpg?alt=media&token=9e65875e-c926-402f-8cbe-2ef69cc50ce5";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_group, container, false);

        mCreateGroup = (TextView) rootView.findViewById(R.id.text_view_create_group);
        mNoGroupTextView = rootView.findViewById(R.id.text_view_no_group);
        mNoGroupGoExplore = rootView.findViewById(R.id.text_view_go_explore);
        RecyclerView rec = rootView.findViewById(R.id.recycler_view);
        rec.setVisibility(View.INVISIBLE);
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

    private void initOnclickLister() {
        //Create group button
        mCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = new CreateGroupFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

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
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 0) {

                            } else {
                                // Remove default view
                                mNoGroupTextView.setVisibility(View.GONE);
                                mNoGroupGoExplore.setVisibility(View.GONE);

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Group group = document.toObject(Group.class);
                                    mImageUrls.add(group.getImageURL());
                                    mNames.add(group.getGroupName());
                                }
                                initRecyclerView();
                            }

                        } else {
                            Log.d(TAG, "onComplete: Error Loading Group data" );
                        }
                    }
                });
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
