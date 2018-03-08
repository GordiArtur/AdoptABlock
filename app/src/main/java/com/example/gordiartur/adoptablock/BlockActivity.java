package com.example.gordiartur.adoptablock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class BlockActivity extends AppCompatActivity {

    private static final String TAG = BlockActivity.class.getName();

    /**
     * UserData static reference to access UserData
     */
    private UserData userData;

    /**
     * Update block_name_label with currently adopted block
     */
    private void updateBlockNameLabel() {
        TextView block_name_label = findViewById(R.id.block_street_name_text);
        if (userData.getBlockName() != null && !userData.getBlockName().isEmpty()) {
            block_name_label.setText(userData.getBlockName());
        }
    }

    /**
     * Update username_label with user's email
     */
    private void updateUserNameLabel() {
        TextView user_email_label = findViewById(R.id.block_owner_name_text);
        if (userData.getUserName() != null && !userData.getUserName().isEmpty()) {
            user_email_label.setText(userData.getUserName());
        }
    }

    /**
     * Change current block name by the one inputted by the user on button click
     * Save the new block name to Firebase
     * @param v View
     */
    public void block_adopt_block_clicked(View v) {
        if (!userData.isAuthenticated()) {
            return;
        }

        EditText text_val = findViewById(R.id.block_block_input_text);
        userData.setBlockName(text_val.getText().toString());
        updateBlockNameLabel();
    }

    /**
     * Updates all text labels in the activity
     */
    private void updateLabels() {
        updateBlockNameLabel();
        updateUserNameLabel();
    }

    /**
     * Retrieves all the values from Firebase to UserData
     */
    private void retrieveValues() {
        retrieveBlockName();
        retrieveUserName();
        retrieveEmail();
        retrieveOrganizationName();
    }

//    private void retrieveBlockName2() {
//
//        DatabaseReference userDatabase;
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = firebaseUser.getUid();
//
//
//        userDatabase = mDatabase.child("users").child(uid).child("block").child("block_name");
//
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                block_name = dataSnapshot.getValue(String.class);
//                updateLabels();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//            }
//        };
//        userDatabase.addListenerForSingleValueEvent(postListener);
//    }

    /**
     * Retrieves block name from firebase to UserData
     * Updates text labels
     */
    private void retrieveBlockName() {
        if (!userData.isAuthenticated()) {
            return;
        }

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData.setBlockName(dataSnapshot.getValue(String.class));
                updateLabels();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        userData.getBlockNameReference().addListenerForSingleValueEvent(postListener);
    }

    /**
     * Retrieves user name from firebase to UserData
     * Updates text labels
     */
    private void retrieveUserName() {
        if (!userData.isAuthenticated()) {
            return;
        }

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData.setUserName(dataSnapshot.getValue(String.class));
                updateLabels();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        userData.getUserNameReference().addListenerForSingleValueEvent(postListener);
    }

    /**
     * Retrieves email name from firebase to UserData
     * Updates text labels
     */
    private void retrieveEmail() {
        if (!userData.isAuthenticated()) {
            return;
        }

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData.setUserEmail(dataSnapshot.getValue(String.class));
                updateLabels();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        userData.getEmailReference().addListenerForSingleValueEvent(postListener);
    }

    /**
     * Retrieves organization name from firebase to UserData
     * Updates text labels
     */
    private void retrieveOrganizationName() {
        if (!userData.isAuthenticated()) {
            return;
        }

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData.setOrganizationName(dataSnapshot.getValue(String.class));
                updateLabels();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        userData.getOrganizationNameReference().addListenerForSingleValueEvent(postListener);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);

        userData = ((UserData)getApplicationContext());
        userData.setUserData();

        retrieveValues();
        updateLabels();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setSelectedItemId(R.id.navigation_block);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_block:
                    Intent intentBlock = new Intent(getApplicationContext(), BlockActivity.class);
                    startActivity(intentBlock);
                    return true;
                case R.id.navigation_profile:
                    Intent intentProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intentProfile);
                    return true;
                case R.id.navigation_map:
                    Intent intentMaps = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intentMaps);
                    return true;
                case R.id.navigation_more:
                    Intent intentMore = new Intent(getApplicationContext(), MoreActivity.class);
                    startActivity(intentMore);
                    return true;
            }
            return false;
        }
    };
}