package com.example.gordiartur.adoptablock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private UserData userData;

    /**
     * Sets up the text views
     */
    private void setUp() {
        userData = ((UserData) getApplicationContext());
        updateUserNameLabel();
        updateEmailLabel();
        updateBlockNameLabel();
    }

    /**
     * Update username_label with user's username
     */
    private void updateUserNameLabel() {
        TextView user_name_label = findViewById(R.id.profile_username_field);
        if (userData.getUserName() != null && !userData.getUserName().isEmpty()) {
            user_name_label.setText(userData.getUserName());
        } else {
            user_name_label.setText(R.string.profile_sign_up_to_view);
        }
    }

    /**
     * Update username_label with user's email
     */
    private void updateEmailLabel() {
        TextView user_email_label = findViewById(R.id.profile_email_field);
        if (userData.isAuthenticated()) {
            user_email_label.setText(userData.getUserEmail());
        }
    }

    /**
     * Update block_name_label with currently adopted block
     */
    private void updateBlockNameLabel() {
        TextView block_name_label = findViewById(R.id.profile_block_field);
        if (userData.getBlockName() != null && !userData.getBlockName().isEmpty()) {
            block_name_label.setText(userData.getBlockName());
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setUp();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setSelectedItemId(R.id.navigation_profile);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
