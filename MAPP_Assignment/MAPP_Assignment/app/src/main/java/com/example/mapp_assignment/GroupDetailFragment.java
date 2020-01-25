package com.example.mapp_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.mapp_assignment.models.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class GroupDetailFragment extends Fragment {

    private static final String TAG = "GroupDetailFragment";

    private View rootView;
    private ImageView mGroupImageView;
    private TextView mGroupName;
    private TextView mGroupDescription;
    private FloatingActionButton mButtonCreateEvent;
    private Button mButtonJoinGroup;

    private String userId;
    private String groupId;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_group_details,container, false);

        groupId = getArguments().getString("groupId");

        mGroupName = rootView.findViewById(R.id.text_group_name);
        mGroupImageView = rootView.findViewById(R.id.image_group);
        mGroupDescription = rootView.findViewById(R.id.text_group_description);
        mButtonCreateEvent = rootView.findViewById(R.id.button_create_event);
        mButtonJoinGroup = rootView.findViewById(R.id.button_join_group);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);

        initFirebaseConnection();
        initOnClickListener();
        loadGroupData();


        return rootView;
    }

    @Override
    public  void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.group_detail_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_leave_group:
//                FirebaseAuth.getInstance().signOut();
//                // Launch the Explore/Main Page class
//                Intent intent = new Intent(getContext(), LoginActivity.class);
//                startActivity(intent);
                userLeaveGroup();
                Toast.makeText(getContext(), "Leave group", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                getFragmentManager().popBackStackImmediate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initFirebaseConnection() {
        // Get instance of firebase authentication
        fAuth = FirebaseAuth.getInstance();
        // Get instance of firebase firestore
        fStore = FirebaseFirestore.getInstance();
        // Get User id
        userId = fAuth.getCurrentUser().getUid();
    }

    private void initOnClickListener(){
        //GO to create event
        mButtonCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = new CreateEventFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_down,R.anim.fade_out,R.anim.fade_in,R.anim.fade_out)
                        .replace(R.id.fragment_container, selectedFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Join group
        mButtonJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userJoinGroup();
                Fragment selectedFragment = new GroupFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_down,R.anim.fade_out,R.anim.fade_in,R.anim.fade_out)
                        .replace(R.id.fragment_container, selectedFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void userLeaveGroup(){
        fStore.collection("groups").document(groupId)
                .update(
                        "membersId", FieldValue.arrayRemove(userId),
                        "groupMemberCount", FieldValue.increment(-1)
                );

    }

    private void userJoinGroup(){
        fStore.collection("groups").document(groupId)
                .update(
                        "membersId", FieldValue.arrayUnion(userId),
                        "groupMemberCount", FieldValue.increment(1)
                );
    }

    private void loadGroupData(){
        fStore.collection("groups").document(groupId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(e !=null){
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            Group group = documentSnapshot.toObject(Group.class);
                            // Show Create Event button when user is owner of group
                            if(group.getCreatorId().equals(userId)){
                                mButtonCreateEvent.show();
                            };
                            // Show Join Group button if not a member of group
                            if(!group.getMembersId().contains(userId)){
                                mButtonJoinGroup.setVisibility(View.VISIBLE);
                            }

                            mGroupName.setText(group.getGroupName());
                            mGroupDescription.setText(group.getGroupDescription());
                            Glide.with(getActivity())
                                    .asBitmap()
                                    .centerCrop()
                                    .load(group.getImageURL())
                                    .transition(BitmapTransitionOptions.withCrossFade())
                                    .into(mGroupImageView);

                        } else {
                            Log.w(TAG, "No data acquired", e);
                        }
                    }
                });
    }
}
