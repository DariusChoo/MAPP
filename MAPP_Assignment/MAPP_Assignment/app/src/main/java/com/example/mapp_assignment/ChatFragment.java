package com.example.mapp_assignment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapp_assignment.adapters.ChatAdapter;
import com.example.mapp_assignment.models.Chat;
import com.example.mapp_assignment.models.Group;
import com.example.mapp_assignment.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";
    private View rootView;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    User user = new User();

    private String chatId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    View view;

    // Group/chat details
//    private ArrayList<String> mGroupNames = new ArrayList<>();
//    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mGroupId = new ArrayList<>();
    private ArrayList<Chat> mChats = new ArrayList<>();

    // Message details
    private ArrayList<String> mLastMessage = new ArrayList<>();
    private ArrayList<String> mTimestamp = new ArrayList<>();
    //private ArrayList<String> mSender = new ArrayList();


    private final String defaultProfileImageUrl = "https://firebasestorage.googleapis.com/v0/b/crux-23cf1.appspot.com/o/default%2Fdefault_proifle_img.jpg?alt=media&token=9e65875e-c926-402f-8cbe-2ef69cc50ce5";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("CHAT FRAGMENT", "ENTERED ON CREATE VIEW");
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        initFirebaseConnection();

        mChats = new ArrayList<>();

        Log.d("CHAT FRAGMENT", "INIT FIREBASE");
        getChatData();
        //readChats();


        //Check if there is item in recycler view
        int count = 0;
        if (chatAdapter != null) {
            count = chatAdapter.getItemCount();
        } else {
            count = 1;
        }
        Log.d("CHAT RECYCLER VIEW COUNT: ", Integer.toString(count));

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mChats = new ArrayList<>();


<<<<<<< HEAD
        //Throw chatdata into adapter
        


        //Check if there is item in recycler view
        int count = 0;
        if (chatAdapter != null) {
            count = chatAdapter.getItemCount();
        } else {
            count = 100000;
        }
        Log.d("CHAT RECYCLER VIEW COUNT: ", Integer.toString(count));

        recyclerView = view.findViewById(R.id.recycler_view);
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//



        return view;
=======
        return view;
    }

    private void initRecyclerView(){
        Log.d(TAG, "onCreateView: " + mChats.get(0).getGrpName());
        recyclerView = view.findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ChatAdapter adapter = new ChatAdapter(getActivity(),mChats);
        recyclerView.setAdapter(adapter);
    }
    //initialise Firebase connection
    private void initFirebaseConnection() {
        // Get instance of firebase authentication
        fAuth = FirebaseAuth.getInstance();
        // Get instance of firebase firestore
        fStore = FirebaseFirestore.getInstance();
>>>>>>> a365d1a832d4c7085cfa3164734edb4998c8c248
    }

    private void getUserGroups() {

        String userId = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "getChatData: " + userId);

        DocumentReference docRef = fStore.collection("user").document(userId);

        docRef.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, "onSuccess: " + documentSnapshot.getData());
                        User user = documentSnapshot.toObject(User.class);

<<<<<<< HEAD
                        for (int i = 0; i < user.getGroupsId().size(); i++) {
                            Log.d(TAG, "onSuccess: group id: " + user.getGroupsId().get(i));
                            mGroupId.add(user.getGroupsId().get(i));
                        }

                        fStore.collection("chats")
                                .whereIn("grpID", mGroupId)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            Log.w(TAG, "Listen failed.", e);
                                            return;
                                        }

//                        mGroupNames.clear();
//                        mImageUrls.clear();
//                        mGroupId.clear();
                                        mChats.clear();

                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            Log.d(TAG, "document size " + queryDocumentSnapshots.size());
                                            if (queryDocumentSnapshots.size() == 0) {
                                                // Appear default view
                                            } else {
                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                                Chat chat = document.toObject(Chat.class);
                                                mChats.add(chat);
//                                mImageUrls.add(chat.getImageUrl());
//                                mGroupNames.add(chat.getGrpName());
//                                mGroupId.add(chat.getGrpID());
                                                // mLastMessage.add(chat.getLastMsg());
                                            }
                                        }
                                        //initRecyclerView();
                                    }
                                });

                        Log.d(TAG, "readChats: " + user.getUserName());
=======
                        for(int i=0;i<user.getGroupsId().size();i++){
                            Log.d(TAG, "onSuccess: group id: " +  user.getGroupsId().get(i));
                        }
                    }
                });

        Log.d(TAG, "readChats: "+user.getUserName());
    }


    private void readChats() {

        //Realtime Database
//        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");


        String userId = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "readChats: " + userId);

        //Getting chats from userid
        DocumentReference docRef = fStore.collection("user").document(userId);
        Log.d(TAG, "readChats: PASSED DOCUMENT REFERENCE");


        docRef.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: DOCREF GETTING CHATS FROM USERID " + e);
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, "onSuccess: ENTERED ON SUCCESS");

                User user = documentSnapshot.toObject(User.class);

                Log.d("USER GROUP SIZE", "" + user.getGroupsId().size());

                //LOOP THROUGH GROUPSID
                for (int i = 0; i < user.getGroupsId().size(); i++) {
                    mGroupId.add(user.getGroupsId().get(i));
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d(TAG, "onComplete: COMPLETED LISTENER");
            }
        });

        Log.d(TAG, "readChats: GOT CHATS FROM USERID");
        Log.d(TAG, "readChats: "+user.getUserName());


        // Query group data
        fStore.collection("chats")
                .whereIn("groupid", mGroupId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

//                        mGroupNames.clear();
//                        mImageUrls.clear();
//                        mGroupId.clear();
                        mChats.clear();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.d(TAG, "document size " + queryDocumentSnapshots.size());
                            if (queryDocumentSnapshots.size() == 0) {
                                // Appear default view
                            } else {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Chat chat = document.toObject(Chat.class);
                                mChats.add(chat);
//                                mImageUrls.add(chat.getImageUrl());
//                                mGroupNames.add(chat.getGrpName());
//                                mGroupId.add(chat.getGrpID());
                                // mLastMessage.add(chat.getLastMsg());
                            }
                        }
                        //initRecyclerView();
                    }
                });

    }

}

