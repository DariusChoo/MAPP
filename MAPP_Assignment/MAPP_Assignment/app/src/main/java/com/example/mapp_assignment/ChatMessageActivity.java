package com.example.mapp_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapp_assignment.adapters.ChatAdapter;
import com.example.mapp_assignment.models.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ChatMessageFragment";
    private RecyclerView recyclerView;

    //XML attributes
    CircleImageView profile_image;
    TextView grpName;

    //Database attributes
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser fuser;
    DatabaseReference reference;

    View view;

    Intent intent;


    private ArrayList<String> mGroupId = new ArrayList<>();
    private ArrayList<String> mMsg = new ArrayList<>();
    private ArrayList<Chat> mChats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = view.findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    //initialise Firebase connection
    private void initFirebaseConnection() {
        // Get instance of firebase authentication
        fAuth = FirebaseAuth.getInstance();
        // Get instance of firebase firestore
        fStore = FirebaseFirestore.getInstance();
    }

    private void initRecyclerView() {
        Log.d(TAG, "onCreateView: " + mChats.get(0).getGrpName());
        recyclerView = view.findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        ChatAdapter adapter = new ChatAdapter(getActivity(), mChats);
//        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

    }

    //Get chat data
    private void getChatData() {
        //        // Query group data
        Log.d(TAG, "getChatData: " + mGroupId.get(0));
        fStore.collection("chats")
                .whereIn("grpID", mGroupId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        mChats.clear();
                        Log.d(TAG, "onEvent: size" + queryDocumentSnapshots.size() );
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.d(TAG, "document size " + queryDocumentSnapshots.size());
                            if (queryDocumentSnapshots.size() == 0) {
                                // Appear default view
                            } else {
                                Log.d(TAG, "onEvent: Pass here");
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Chat chat = document.toObject(Chat.class);
                                mChats.add(chat);
                            }
                        }
                        initRecyclerView();
                    }
                });
    }

    //Get message data


}
