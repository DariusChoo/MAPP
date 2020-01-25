package com.example.mapp_assignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Force Logout everytime the app is open as logout button has not been made
        // comment this so that user stays signed in when the log in or registers
        FirebaseAuth.getInstance().signOut();
        // Launch the layout -> splash.xml
        setContentView(R.layout.splashscreen);
        Thread splashThread = new Thread() {

            public void run() {
                try {
                    // sleep time in milliseconds (3000 = 3sec)
                    sleep(3000);
                }  catch(InterruptedException e) {
                    // Trace the error
                    e.printStackTrace();
                } finally
                {
                    // Launch the Explore/Main Page class
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        // To Start the thread
        splashThread.start();
    }
}