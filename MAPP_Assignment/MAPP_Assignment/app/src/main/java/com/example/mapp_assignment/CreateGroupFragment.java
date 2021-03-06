package com.example.mapp_assignment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mapp_assignment.models.Chat;
import com.example.mapp_assignment.models.Group;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class CreateGroupFragment extends Fragment {

    private static final String TAG = "CreateGroupFragment";
    private final String defaultProfileImageUrl = "https://firebasestorage.googleapis.com/v0/b/crux-23cf1.appspot.com/o/default%2Fdefault_proifle_img.jpg?alt=media&token=9e65875e-c926-402f-8cbe-2ef69cc50ce5";


    View rootView;

    private TextInputLayout mGroupName;
    private TextInputLayout mGroupDescription;
    private Spinner mGroupCategory;
    private TextInputLayout mGroupInterest;
    private CardView mCardViewGroupImage;
    private ImageView groupImageView;
    private Uri mImageUri;
    private View mProgressLayout;
    private Toolbar toolbar;
    Button createGroup;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private StorageReference mStorageRef;
    private String userId;
    private Group newGroup = new Group();
    private String groupImageUrl;

    private BottomNavigationView btmView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        groupImageView = rootView.findViewById(R.id.image_group);
        mGroupName = rootView.findViewById(R.id.text_input_group_name);
        mGroupDescription = rootView.findViewById(R.id.text_input_group_description);
        mGroupCategory = rootView.findViewById(R.id.group_category);
        mGroupInterest = rootView.findViewById(R.id.text_input_group_interest);
        createGroup = rootView.findViewById(R.id.button_create_group);
        mCardViewGroupImage = rootView.findViewById(R.id.card_view_group_image);
        mProgressLayout = rootView.findViewById(R.id.progress_layout);

        btmView = getActivity().findViewById(R.id.bottom_navigation);
        btmView.setVisibility(View.GONE);

        initFirebaseConnection();
        initToolbar();
        initArrayAdapter();
        initOnClickListener();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            mImageUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getApplicationContext().getContentResolver().openInputStream(mImageUri));
                groupImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void initFirebaseConnection() {
        // Get instance of firebase authentication
        fAuth = FirebaseAuth.getInstance();
        // Get instance of firebase firestore
        fStore = FirebaseFirestore.getInstance();
        // Get instance of firebase cloud storage referencing group image
        mStorageRef = FirebaseStorage.getInstance().getReference("groupImage");
        // Get user id
        userId = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "User id" + userId);
    }

    private void initToolbar(){
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);
    }

    private void initArrayAdapter() {
        // Create ArrayAdapter and set items and layout for spinner
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.category));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGroupCategory.setAdapter(myAdapter);
    }

    private void initOnClickListener() {
        //Add group photo
        mCardViewGroupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "INPAGE", Toast.LENGTH_SHORT).show();
                openImageGallery();
            }
        });
        //Create group button
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateGroupName() | !validateGroupDescription()) {
                    return;
                } else {
                    mProgressLayout.setVisibility(View.VISIBLE);
                    createGroup();
                }
            }
        });
    }
    // Open file chooser
    private void openImageGallery() {
        Intent intent = new Intent();
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.setAction(intent.ACTION_PICK);
        startActivityForResult(intent, 0);
    }

    // Get file extension
    private String getFileExtension(Uri uri) {
        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private boolean validateGroupName() {
        String groupNameInput = mGroupName.getEditText().getText().toString().trim();

        if (groupNameInput.isEmpty()) {
            mGroupName.setError("Please enter a group name");
            return false;
        } else {
            mGroupName.setError(null);
            return true;
        }
    }

    private boolean validateGroupDescription() {
        String groupDescriptionInput = mGroupDescription.getEditText().getText().toString().trim();

        if (groupDescriptionInput.isEmpty()) {
            mGroupDescription.setError("Please enter a group description");
            return false;
        } else if (groupDescriptionInput.length() > 100) {
            mGroupDescription.setError("Description is too long");
            return false;
        } else {
            mGroupDescription.setError(null);
            return true;
        }
    }
    // create new group
    public void createGroup() {
        // Get User id
        String userId = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "USER ID  > " + userId);
        // Get User input fields
        String groupNameInput = mGroupName.getEditText().getText().toString().trim();
        String groupDescriptionInput = mGroupDescription.getEditText().getText().toString().trim();
        String groupCategoryInput = mGroupCategory.getSelectedItem().toString();
        String groupInterestInput = mGroupInterest.getEditText().getText().toString().trim();
        ArrayList<String>memId = new ArrayList<>();
        memId.add(userId);


        // Declare and initialize Group object
        newGroup = new Group();
        newGroup.setCreatorId(userId);
        newGroup.setGroupMemberCount(1);
        newGroup.setGroupName(groupNameInput);
        newGroup.setGroupDescription(groupDescriptionInput);
        newGroup.setImageURL(groupImageUrl);
        newGroup.setCategory(groupCategoryInput);
        newGroup.setInterest(groupInterestInput);
        newGroup.setMembersId(memId);

        // Add group object to collection
        CollectionReference grpCollection = fStore.collection("groups");
        grpCollection.add(newGroup)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Group cannot be created, Please try again.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID" + documentReference.getId());
                        // Upload group image to Cloud storage
                        uploadGroupImage(documentReference.getId());
                    }

                });
    }

    private void updateUser(){
        fStore.collection("user").document(userId)
                .update(
                        "groupsId", FieldValue.arrayUnion(newGroup.getGroupId()),
                        "groupCount", FieldValue.increment(1)
                );
    }
    // Upload group image to Firebase storage
    private void uploadGroupImage(final String groupId) {
        Log.d(TAG, "uploadGroupImage: IN");
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(groupId + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, "uploadGroupImage: SUCCESS");
                            Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                            // Get uri while listening
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d(TAG, " URI onSuccess: " + uri.toString());
                                    // Update user profile
                                    updateGroupImageUrl(uri.toString(), groupId);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "uploadGroupImage: FAILURE");
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Please choose an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void createChat() {
        Log.d(TAG, "createChat: " + newGroup.getGroupName() + newGroup.getGroupId());

        Chat newChat = new Chat();
        newChat.setGrpID(newGroup.getGroupId());
        newChat.setGrpName(newGroup.getGroupName());
        newChat.setImageUrl(newGroup.getImageURL());
        newChat.setLastMsg("");
        newChat.setTimestamp(null);

        // Add group object to collection
        CollectionReference chatCollection = fStore.collection("chats");
        chatCollection.document(newGroup.getGroupId())
                .set(newChat)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                });
    }

    // update image url of group
    private void updateGroupImageUrl(String uri, String groupId){
        // Update group object
        newGroup.setGroupId(groupId);
        newGroup.setImageURL(uri);

        DocumentReference newGroup = fStore.collection("groups").document(groupId);

        newGroup.update(
                "imageURL", uri,
                "groupId", groupId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        createChat();
                        updateUser();
                        btmView.setVisibility(View.VISIBLE);
                        // Back to group page
                        Fragment selectedFragment = new GroupFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        mProgressLayout.setVisibility(View.GONE);
    }
}
