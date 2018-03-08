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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BlockActivity extends AppCompatActivity {

    private static final String TAG = BlockActivity.class.getName();

    /**
     * Firebase Authentication instance
     * used to access current user information
     */
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    /**
     * Firebase Database instance
     * used to access all database
     */
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private final String uid = instantiateUID();
    private final String user_display_name = instantiateUserDisplayName();
    private final String user_path = "users"; // Firebase path to "users" node
    private final String block_name_path = "block_name"; // Firebase path to "block_name" node
    private String block_name; // Used to display the block name to the user

    /**
     * Save the block name to the database
     * @TODO Convert to boolean
     */
    private void saveBlockName() {
        if (firebaseUser == null) {
            return;
        }

        DatabaseReference userDatabase;

        userDatabase = mDatabase.child(user_path).child(uid).child(block_name_path);
        userDatabase.setValue(block_name);
    }

    /**
     * Update block_name from Firebase database based on user ID
     */
    private void retrieveBlockName() {
        if (firebaseUser == null) {
            return;
        }

        DatabaseReference userDatabase;
        userDatabase = mDatabase.child(user_path).child(uid).child(block_name_path);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                block_name = dataSnapshot.getValue(String.class);
                updateBlockNameLabel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        userDatabase.addListenerForSingleValueEvent(postListener);
    }

    /**
     * Update block_name_label with currently adopted block
     */
    private void updateBlockNameLabel() {
        if (firebaseUser == null) {
            return;
        }

        TextView block_name_label = findViewById(R.id.block_street_name_text);
        if (block_name != null && !block_name.isEmpty()) {
            block_name_label.setText(block_name);
        }
    }

    /**
     * Update username_label with user's email
     */
    private void updateUserNameLabel() {
        if (firebaseUser == null) {
            return;
        }

        TextView user_email_label = findViewById(R.id.block_owner_name_text);
        if (!user_display_name.isEmpty()) {
            user_email_label.setText(user_display_name);
        }
    }

    /**
     * Change current block name by the one inputted by the user on button click
     * @param v View
     */
    public void block_adopt_block_clicked(View v) {
        if (firebaseUser == null) {
            return;
        }

        EditText text_val = findViewById(R.id.block_block_input_text);
        block_name = text_val.getText().toString();

        // Update block name if input is not empty
        if (!block_name.isEmpty()) {
            TextView block_name_text = findViewById(R.id.block_street_name_text);
            block_name_text.setText(block_name);
        }
        saveBlockName();
    }

    /**
     * Firebase user id
     * @return user id as String
     */
    private String instantiateUID() {
        if (firebaseUser != null) {
            return firebaseUser.getUid();
        } else {
            return null;
        }
    }

    /**
     * Firebase user display name
     * @return user display name as String
     * @TODO Change to Username instead of Email once we have it
     */
    private String instantiateUserDisplayName() {
        if (firebaseUser != null) {
            return firebaseUser.getEmail();
        } else {
            return null;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setSelectedItemId(R.id.navigation_block);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Update street_name variable with database variable if exists
        retrieveBlockName();
        updateUserNameLabel();
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