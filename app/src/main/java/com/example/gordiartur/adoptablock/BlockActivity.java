package com.example.gordiartur.adoptablock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BlockActivity extends AppCompatActivity {

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


    private final String uid = firebaseUser.getUid(); // Firebase user id
    private final String user_email = firebaseUser.getEmail(); // Firebase user email
    private final String user_path = "users"; // Firebase path to "users" node
    private final String block_name_path = "block_name"; // Firebase path to "block_name" node
    private String block_name; // Used to display the block name to the user

    /**
     * Save the block name to the database
     * @TODO Convert to boolean
     */
    private void saveBlockName() {
        DatabaseReference userDatabase;

        userDatabase = mDatabase.child(user_path).child(uid).child(block_name_path);
        userDatabase.setValue(block_name);
    }

    /**
     * Update block_name from Firebase database based on user ID
     */
    private void retrieveBlockName() {
        DatabaseReference userDatabase;

        userDatabase = mDatabase.child(user_path).child(uid).child(block_name_path);
        block_name = userDatabase.getKey();
    }

    /**
     * Update block_name_label with currently adopted block
     */
    private void updateBlockNameLabel() {
        TextView block_name_label = findViewById(R.id.block_street_name_text);
        if (!block_name.isEmpty()) {
            block_name_label.setText(block_name);
        }
    }

    /**
     * Update username_label with user's email
     * @TODO Update email to username once we have it
     */
    private void updateUserNameLabel() {
        TextView user_email_label = findViewById(R.id.block_owner_name_text);
        if (!user_email.isEmpty()) {
            user_email_label.setText(user_email);
        }
    }

    /**
     * Change current block name by the one inputted by the user on button click
     * @param v View
     */
    public void block_adopt_block_clicked(View v) {
        EditText text_val = findViewById(R.id.block_block_input_text);
        block_name = text_val.getText().toString();

        // Update block name if input is not empty
        if (!block_name.isEmpty()) {
            TextView block_name_text = findViewById(R.id.block_street_name_text);
            block_name_text.setText(block_name);
        }
        saveBlockName();
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
        updateBlockNameLabel();
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