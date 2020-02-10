package com.example.mapp_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapp_assignment.adapters.ChatAdapter;
import com.example.mapp_assignment.models.Chat;
import com.example.mapp_assignment.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);
        initFirebaseConnection();
        Log.d("INIT CONN","SUCCESS");
        getChatData();
        Log.d("GETTING CHAT DATA","SUCCESS");
    }

    //@Override
//    protected void onCreate(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.d("Entered On Create","SUCCESS");
//        super.onCreate(savedInstanceState);
//
//        //setContentView(R.layout.activity_message);
//
//        view = inflater.inflate(R.layout.activity_message, container, false);
//        Log.d("INFLATE VIEW","SUCCESS");
//
//        initFirebaseConnection();
//        Log.d("INIT CONN","SUCCESS");
//
//        getChatData();
//        Log.d("GETTING CHAT DATA","SUCCESS");
//
//        Log.d("RETURNING VIEW","PLZ");
//        setContentView(view);
//    }

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
        Bundle getData = getIntent().getExtras();
        String chatId = getData.getString("chatId");
        Log.d(TAG, "getChatData: " + chatId);
        DocumentReference docRef = fStore.collection("chats").document(chatId);
        docRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        })
        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, "onSuccess: " + documentSnapshot.getData());
                Chat chat = documentSnapshot.toObject(Chat.class);
            }
        });

//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "onFailure: " + e.toString());
//                    }
//                })
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.w(TAG, "Listen failed.", e);
//                            return;
//                        }
//
//                        mChats.clear();
//                        Log.d(TAG, "onEvent: size" + queryDocumentSnapshots.size() );
//
//
//                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                            Log.d(TAG, "document size " + queryDocumentSnapshots.size());
//                            if (queryDocumentSnapshots.size() == 0) {
//                                // Appear default view
//                            } else {
//                                Log.d(TAG, "onEvent: Pass here");
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                Chat chat = document.toObject(Chat.class);
//                                mChats.add(chat);
//                            }
//                        }
//                        initRecyclerView();
//                    }
//                });
    }

    //Get message data


}
