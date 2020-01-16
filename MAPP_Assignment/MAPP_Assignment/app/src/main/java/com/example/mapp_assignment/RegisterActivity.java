package com.example.mapp_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.mapp_assignment.models.*;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Register Activity";
    private final String defaultProfileImageUrl = "https://firebasestorage.googleapis.com/v0/b/crux-23cf1.appspot.com/o/default%2Fdefault_proifle_img.jpg?alt=media&token=9e65875e-c926-402f-8cbe-2ef69cc50ce5";

    TextInputLayout mUserName, mEmail, mPassword, mRetypedPassword;
    FirebaseAuth fAuth;
    ProgressBar mProgressBar;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get field values
        mUserName = findViewById(R.id.edit_text_username);
        mEmail = findViewById(R.id.edit_text_email);
        mPassword = findViewById(R.id.edit_text_password);
        mRetypedPassword = findViewById(R.id.edit_text_retypepassword);
        mProgressBar = findViewById(R.id.progressBarRegister);

        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.text_btn_login).setOnClickListener(this);

        // Get instance of firebase authentication
        fAuth = FirebaseAuth.getInstance();
        // Get instance of firebase firestore
        fStore = FirebaseFirestore.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register: {
                if(validateFields()){
                    String userName = mUserName.getEditText().getText().toString();
                    String email = mEmail.getEditText().getText().toString();
                    String password = mPassword.getEditText().getText().toString();

                    registerUser(userName,email, password );
                }

                break;
            }
            case R.id.text_btn_login:{
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                break;
            }
        }
    }


    private boolean validateFields(){
        if (TextUtils.isEmpty(mEmail.getEditText().getText().toString())) {
            mEmail.setError("Email is required");
            return false;
        }

        if (TextUtils.isEmpty(mPassword.getEditText().getText().toString())) {
            mPassword.setError("Password is required");
            return false;
        }

        if (mPassword.getEditText().getText().toString().length() < 6) {
            mPassword.setError("Password Must be more than 6 characters");
            return false;
        }

        if (!mPassword.getEditText().getText().toString().equals(mRetypedPassword.getEditText().getText().toString())) {
            mRetypedPassword.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    private void registerUser(final String userName, final String email, String password) {
        showProgressBar();
        //Register User
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User is created", Toast.LENGTH_SHORT).show();
                    final String userId = fAuth.getCurrentUser().getUid();

                    DocumentReference newUserDocRef = fStore.collection("user").document(userId);

                    User user = new User();
                    user.setUserName(userName);
                    user.setEmail(email);
                    user.setImageURL(defaultProfileImageUrl);
                    user.setUserId(userId);
                    user.setEventCount(0);
                    user.setGroupCount(0);

                    newUserDocRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "User profile is created for " + userId);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "On Failure: " + e.toString());
                        }
                    });

                    hideProgressBar();

                } else {
                    Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                }
            }
        });
    }


    // Note increment value
//    public Task<Void> incrementCounter(final DocumentReference ref, final int numShards) {
//        int shardId = (int) Math.floor(Math.random() * numShards);
//        DocumentReference shardRef = ref.collection("shards").document(String.valueOf(shardId));
//
//        return shardRef.update("count", FieldValue.increment(1));
//    }

    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
