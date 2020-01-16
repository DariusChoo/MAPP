package com.example.mapp_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.mapp_assignment.models.Group;

public class CreateGroupFragment extends Fragment{

    private static final String TAG = "CreateGroupFragment";
    private final String defaultProfileImageUrl = "https://firebasestorage.googleapis.com/v0/b/crux-23cf1.appspot.com/o/default%2Fdefault_proifle_img.jpg?alt=media&token=9e65875e-c926-402f-8cbe-2ef69cc50ce5";


    View rootView;
    private TextInputLayout textGroupName;
    private TextInputLayout textGroupDescription;
    private Spinner spinnerGroupCategory;
    private TextInputLayout textGroupInterest;
    Button createGroup;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_create_group,container, false);

        textGroupName = rootView.findViewById(R.id.text_input_group_name);
        textGroupDescription = rootView.findViewById(R.id.text_input_group_description);
        spinnerGroupCategory = rootView.findViewById(R.id.group_category);
        textGroupInterest = rootView.findViewById(R.id.text_input_group_interest);

        createGroup = rootView.findViewById(R.id.button_create_group);

        // Get instance of firebase authentication
        fAuth = FirebaseAuth.getInstance();
        // Get instance of firebase firestore
        fStore = FirebaseFirestore.getInstance();


        // Create ArrayAdapter and set items and layout for spinner
        ArrayAdapter<String>myAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.category));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroupCategory.setAdapter(myAdapter);

        //Create group button
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup(v);
            }
        });


        return rootView;
    }


    private boolean validateGroupName(){
        String groupNameInput = textGroupName.getEditText().getText().toString().trim();

        if(groupNameInput.isEmpty()){
            textGroupName.setError("Please enter a group name");
            return false;
        }else{
            textGroupName.setError(null);
            return true;
        }
    }

    private boolean validateGroupDescription(){
        String groupDescriptionInput = textGroupDescription.getEditText().getText().toString().trim();

        if(groupDescriptionInput.isEmpty()){
            textGroupName.setError("Please enter a group description");
            return false;
        }else if(groupDescriptionInput.length() > 50){
            textGroupName.setError("Description too long");
            return false;
        }
        else{
            textGroupName.setError(null);
            return true;
        }
    }

    public void createGroup(View v){
        if(!validateGroupName() | !validateGroupDescription()){
            return;
        }else{
            // Get User id
            String userId = fAuth.getCurrentUser().getUid();
            // Get User input fields
            String groupNameInput = textGroupName.getEditText().getText().toString().trim();
            String groupDescriptionInput = textGroupDescription.getEditText().getText().toString().trim();
            String groupCategoryInput  = spinnerGroupCategory.getSelectedItem().toString();
            String groupInterestInput = textGroupInterest.getEditText().getText().toString().trim();
            // Declare and initialize Group object
            Group newGroup = new Group();
            newGroup.setCreatorId(userId);
            newGroup.setGroupMemberCount(0);
            newGroup.setGroupName(groupNameInput);
            newGroup.setGroupDescription(groupDescriptionInput);
            newGroup.setImageURL(defaultProfileImageUrl);
            newGroup.setCategory(groupCategoryInput);
            newGroup.setInterest(groupInterestInput);
            // Add group object to collection
            CollectionReference grpCollection = fStore.collection("groups");
            grpCollection.add(newGroup)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot written with ID" + documentReference.getId());
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
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }

//        String input = "Group name: " + textGroupName.getEditText().getText().toString();
//        input += "\n";
//        input +="Grouo Description: " + textGroupDescription.getEditText().getText().toString();
//
//        Toast.makeText(getActivity(), input,Toast.LENGTH_SHORT).show();
    }

}
