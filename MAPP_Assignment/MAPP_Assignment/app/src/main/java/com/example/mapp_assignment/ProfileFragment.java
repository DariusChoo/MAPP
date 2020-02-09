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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.mapp_assignment.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProfileFragment extends Fragment {

    View rootView;

    private Toolbar toolbar;
    private TextView mUserName;
    private ImageView mProfileImage;
    private TextView mGroupCount;
    private TextView mEventCount;
    private Uri mImageUri;

    FirebaseAuth fAuth;;
    FirebaseFirestore fStore;
    StorageReference mStorageRef;

    private String userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mProfileImage  = rootView.findViewById(R.id.image_profile);
        mUserName = rootView.findViewById(R.id.text_username);
        mGroupCount = rootView.findViewById(R.id.group_count);
        mEventCount = rootView.findViewById(R.id.event_count);

        initActionToolbar();
        initOnClickListener();
        initFirebaseConnection();
        getUserProfile();

        return rootView;
    }

    private void initFirebaseConnection() {
        // Get instance of firebase authentication
        fAuth = FirebaseAuth.getInstance();
        // Get instance of firebase firestore
        fStore = FirebaseFirestore.getInstance();
        // Get instance of firebase cloud storage referencing group image
        mStorageRef = FirebaseStorage.getInstance().getReference("profileImage");
        // Get User id
        userId = fAuth.getCurrentUser().getUid();

        Log.d(TAG, "user id is: " + userId);
    }

    private void initActionToolbar() {
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);
    }

    private void initOnClickListener() {
        //Add group photo
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "INPAGE", Toast.LENGTH_SHORT).show();
                openImageGallery();
            }
        });
    }

    // Load the image selected by user in ImageView
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            mImageUri = data.getData();
            Bitmap bitmap;
            try {
                 bitmap = BitmapFactory.decodeStream(getActivity().getApplicationContext().getContentResolver().openInputStream(mImageUri));
//                mProfileImage.setImageBitmap(bitmap);
                uploadProfileImage();

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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

    // Upload group image to Firebase storage
    private void uploadProfileImage() {
        Log.d(TAG, "uploadProfileImage: IN");
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(userId + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, "uploadProfileImage: SUCCESS");
                            Toast.makeText(getContext(), "New Profile Picture updated", Toast.LENGTH_LONG).show();
                            // Get uri while listening
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d(TAG, " URI onSuccess: " + uri.toString());
                                    // Update user profile
                                    updateImageUrl(uri.toString(), userId);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "uploadProfileImage: FAILURE");
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

    // update image url of group
    private void updateImageUrl(String uri, String userId){
        DocumentReference newGroup = fStore.collection("user").document(userId);

        newGroup.update("imageURL", uri)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

    }


    private void getUserProfile(){
        DocumentReference userRef= fStore.collection("user").document(userId);

        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());

                    User user = snapshot.toObject(User.class);
                    mGroupCount.setText(String.valueOf(user.getGroupCount()));
                    mEventCount.setText(String.valueOf(user.getEventCount()));
                    mUserName.setText(user.getUserName());
                    Glide.with(getActivity())
                            .asBitmap()
                            .centerCrop()
                            .load(user.getImageURL())
                            .transition(BitmapTransitionOptions.withCrossFade())
                            .apply(RequestOptions.circleCropTransform())
                            .into(mProfileImage);

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // On click for items in menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                return true;
            case R.id.item_setting:
                startActivity(new Intent(getContext(), ProfileSetting.class));
                return true;
            case R.id.item_about:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), About.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
