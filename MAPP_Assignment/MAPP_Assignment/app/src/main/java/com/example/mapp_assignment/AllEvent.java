package com.example.mapp_assignment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapp_assignment.adapters.EventTabRecyclerAdapter;
import com.example.mapp_assignment.adapters.YourGroupRecyclerAdapter;
import com.example.mapp_assignment.models.Event;
import com.example.mapp_assignment.models.Group;
import com.example.mapp_assignment.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllEvent extends Fragment {

    private static final String TAG = "AllEvent";
    View rootView;

    private String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private User user;

    private ArrayList<Event> mEventList = new ArrayList<>();

    public AllEvent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_all_event, container, false);

        initFirebaseConnection();
        getUserGroup();

         return rootView;
    }

    private void initFirebaseConnection() {
        // Get instance of firebase authentication
        fAuth = FirebaseAuth.getInstance();
        // Get instance of firebase firestore
        fStore = FirebaseFirestore.getInstance();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView:");

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        EventTabRecyclerAdapter adapter = new EventTabRecyclerAdapter(getActivity(), mEventList);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "initRecyclerView: adapterCOunt" + adapter.getItemCount());

    }

    private void getUserGroup(){
        // Get User id
        String userId = fAuth.getCurrentUser().getUid();

        fStore.collection("user").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                user = document.toObject(User.class);
                                loadEventData();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void loadEventData() {
        // Query group data
        fStore.collection("events")
                .whereIn("groupId", user.getGroupsId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        mEventList.clear();

                        Log.d(TAG, "SIZE OF GROUP " + queryDocumentSnapshots.size());

                        if(queryDocumentSnapshots.size() == 0){
                            Log.d(TAG, "onEvent: Hello");
                        }else{
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Log.d(TAG, "document size " + queryDocumentSnapshots.size());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Event event = document.toObject(Event.class);
                                mEventList.add(event);
                            }
                            initRecyclerView();
                        }


                    }
                });
    }

}
