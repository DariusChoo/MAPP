package com.example.mapp_assignment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.mapp_assignment.models.Chat;
import com.example.mapp_assignment.models.Event;
import com.example.mapp_assignment.models.TimePickerFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class CreateEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "CreateEventFragment";

    View rootView;

    private TextInputLayout mEventName;
    private TextInputLayout mEventDesc;
    private TextInputLayout mEventDate;
    private TextInputLayout mEventTime;
    private TextInputLayout mEventLocation;
    private TextInputEditText mEditEventDate;
    private TextInputEditText mEditEventTime;
    private CardView mCardViewEventImage;
    private ImageView eventImageView;
    private Uri mImageUri;
    private View mProgressLayout;
    private Toolbar toolbar;

    private BottomNavigationView btmView;

    Button createEvent;
    TextView loc;
    private Event newEvent = new Event();

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private StorageReference mStorageRef;
    private String userId;
    private String groupId;
    private String groupName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_create_event, container, false);


        mEventName = rootView.findViewById(R.id.text_input_event_name);
        mEventDesc = rootView.findViewById(R.id.text_input_event_description);
        mEventLocation = rootView.findViewById(R.id.text_input_event_location);
        mEditEventDate = rootView.findViewById(R.id.edit_text_date);
        mEditEventTime = rootView.findViewById(R.id.edit_text_time);
        eventImageView = rootView.findViewById(R.id.image_event);
        mCardViewEventImage = rootView.findViewById(R.id.card_view_event_image);
        mProgressLayout = rootView.findViewById(R.id.progress_layout);
        mEventDate = rootView.findViewById(R.id.text_input_date);
        mEventTime = rootView.findViewById(R.id.text_input_time);

        btmView = getActivity().findViewById(R.id.bottom_navigation);
        btmView.setVisibility(View.GONE);

        createEvent = rootView.findViewById(R.id.button_create_event);
//        mEventDate = rootView.findViewById(R.id.text_input_date);

        disableSoftInputFromAppearing(mEditEventDate);
        disableSoftInputFromAppearing(mEditEventTime);

        initToolbar();
        initOnClickListener();
        initOnFocusListener();
        initFirebaseConnection();

        return rootView;
    }

    public static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
            editText.setShowSoftInputOnFocus(false);
        } else { // API 11-20
            editText.setTextIsSelectable(true);
        }
    }

    private void initToolbar(){
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String currentDateString = DateFormat.getDateInstance().format(calendar.getTime());
        mEditEventDate.setText(currentDateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);

        String currTimeString = hourOfDay + ":" + minute;
        mEditEventTime.setText(currTimeString);
    }

    private void initFirebaseConnection() {
        // Get instance of firebase authentication
        fAuth = FirebaseAuth.getInstance();
        // Get instance of firebase firestore
        fStore = FirebaseFirestore.getInstance();
        // Get instance of firebase cloud storage referencing group image
        mStorageRef = FirebaseStorage.getInstance().getReference("eventImage");
        // Get user id
        userId = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "User id" + userId);
    }

    private void initOnClickListener() {
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressLayout.setVisibility(View.VISIBLE);
                try {
                    createEvent();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        //Add group photo
        mCardViewEventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "INPAGE", Toast.LENGTH_SHORT).show();
                openImageGallery();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            mImageUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getApplicationContext().getContentResolver().openInputStream(mImageUri));
                eventImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

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

    private void initOnFocusListener(){
        mEditEventDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DialogFragment datePicker = new DatePickerFragment();
                    datePicker.show(getFragmentManager(), "date picker");
                } else {
                    // Hide your calender here
                }
            }
        });

        mEditEventTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getFragmentManager(), "time picker");
                } else {
                    // Hide your calender here
                }
            }
        });
    }

    // create new group
    public void createEvent() throws ParseException {

        mEventName = rootView.findViewById(R.id.text_input_event_name);
        mEventDesc = rootView.findViewById(R.id.text_input_event_description);
        mEventLocation = rootView.findViewById(R.id.text_input_event_location);
        mEditEventDate = rootView.findViewById(R.id.edit_text_date);
        mEditEventTime = rootView.findViewById(R.id.edit_text_time);
        eventImageView = rootView.findViewById(R.id.image_event);
        mCardViewEventImage = rootView.findViewById(R.id.card_view_event_image);
        mProgressLayout = rootView.findViewById(R.id.progress_layout);
        // Get User id
        String userId = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "USER ID  > " + userId);
        // Get User input fields
        String eventNameInput = mEventName.getEditText().getText().toString().trim();
        String eventDescriptionInput = mEventDesc.getEditText().getText().toString().trim();
        String eventLocationInput = mEventLocation.getEditText().getText().toString().trim();
        String eventDateInput = mEventDate.getEditText().getText().toString().trim();
        String eventTimeInput = mEventTime.getEditText().getText().toString().trim();


        ArrayList<String> partcipantId = new ArrayList<>();
        partcipantId.add(userId);

        groupId = getArguments().getString("groupId");
        groupName = getArguments().getString("groupName");
        // Declare and initialize Group object
        newEvent = new Event();
        newEvent.setGroupName(groupName);
        newEvent.setGroupId(groupId);
        newEvent.setEventName(eventNameInput);
        newEvent.setEventDescription(eventDescriptionInput);
        newEvent.setLocation(eventLocationInput);
        newEvent.setEventDate(eventDateInput);
        newEvent.setEventTime(eventTimeInput);
        newEvent.setParticipantsId(partcipantId);

        // Add group object to collection
        CollectionReference evemtCollection = fStore.collection("events");
        evemtCollection.add(newEvent)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Event cannot be created, Please try again.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID" + documentReference.getId());
                        // Upload group image to Cloud storage
                        uploadEventImage(documentReference.getId());
                    }

                });
    }

    private void updateUser(String eventId){
        fStore.collection("user").document(userId)
                .update(
                        "eventsId", FieldValue.arrayUnion(eventId),
                        "eventCount", FieldValue.increment(1)
                );
    }

    // Upload group image to Firebase storage
    private void uploadEventImage(final String eventId) {
        Log.d(TAG, "uploadEventImage: IN");
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(groupId + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, "uploadEventImage: SUCCESS");
                            Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                            // Get uri while listening
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d(TAG, " URI onSuccess: " + uri.toString());
                                    // Update user profile
                                    updateEventImageUrl(uri.toString(), eventId);
                                    //btmView.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "uploadEventImage: FAILURE");
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
    private void updateEventImageUrl(String uri, final String eventId){

        newEvent.setEventImageUrl(uri);

        DocumentReference newGroup = fStore.collection("events").document(eventId);

        newGroup.update(
                "eventImageUrl", uri,
                "eventId", eventId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        updateUser(eventId);
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
