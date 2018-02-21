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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BlockActivity extends AppCompatActivity {
    private static final String TAG = BlockActivity.class.getName();
    private DatabaseReference mDatabase;
    private String test_block_name_database;

    /**
     * Test Google Firebase by setting and getting values between the app and the cloud database
     *
     * use test user: test2@gmail.com pass: hunter2
     */
    private void testDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("test_field/would_nested_work?");
        myRef.setValue("Artur was not here");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Listens and updates the app in real time if a database value changes
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    /**
     * Sets the street name based on the stored value in the database
     * @TODO Convert to boolean
     */
    private void setStreetName() {

    }

    /**
     * Change current block name by the one inputted by the user
     * @param v View
     */
    public void block_adopt_block_clicked(View v) {
        EditText text_val = findViewById(R.id.block_block_input_text);
        TextView block_name_text = findViewById(R.id.block_street_name_text);
        String block_name = text_val.getText().toString();

        // Update block name if input is not empty
        if (!block_name.isEmpty()) {
            block_name_text.setText(block_name);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setSelectedItemId(R.id.navigation_block);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Test database functions
        testDatabase();
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