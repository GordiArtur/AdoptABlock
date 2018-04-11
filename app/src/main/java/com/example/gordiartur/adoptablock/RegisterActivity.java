package com.example.gordiartur.adoptablock;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mNameView;
    private String finalEmail;
    private String finalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // UI Setup
        mEmailView = findViewById(R.id.registrationEmail);
        mPasswordView = findViewById(R.id.registrationPassword);
        mNameView = findViewById(R.id.registrationName);
        Button mRegistrationSubmitButton = findViewById(R.id.registrationSubmitButton);
        Button mReturnButton = findViewById(R.id.registrationReturnButton);
        mRegistrationSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEmailView.getText().toString().isEmpty()) {
                    mEmailView.setHint(getString(R.string.error_field_required));
                    mEmailView.setHintTextColor(Color.RED);
                } else if(mPasswordView.getText().toString().isEmpty()) {
                    mPasswordView.setHint(getString(R.string.error_field_required));
                    mPasswordView.setHintTextColor(Color.RED);
                } else {
                    finalEmail = mEmailView.getText().toString();
                    finalName = mNameView.getText().toString();
                    attemptRegistration(finalEmail, mPasswordView.getText().toString(), finalName);
                }
            }
        });
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginActivity();
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    private void attemptRegistration(String email, String password, String name){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseCreateUserFields(finalEmail, finalName);
                            goToBlockActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("RegisterActivity", "createUserWithEmail:failure");
                        }
                    }
                });
    }

    /**
     * Create initial database fields for the user
     * @param email email
     * @param name userName
     */
    private void firebaseCreateUserFields(String email, String name) {
        String uid = mAuth.getCurrentUser().getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference userNameReference = mDatabase.child("users").child(uid).child("user_name");
        DatabaseReference emailReference = mDatabase.child("users").child(uid).child("email");
        DatabaseReference blockNameReference = mDatabase.child("users").child(uid).child("block").child("block_name");
        DatabaseReference organizationNameReference = mDatabase.child("users").child(uid).child("organization");

        if (name != null && !name.isEmpty()) {
            userNameReference.setValue(name);
        }
        emailReference.setValue(email);
        blockNameReference.setValue("");
        organizationNameReference.setValue("");
    }

    private void goToBlockActivity(){
        Intent intent = new Intent(this, BlockActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}