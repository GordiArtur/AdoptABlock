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

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // UI Setup
        mEmailView = findViewById(R.id.registrationEmail);
        mPasswordView = findViewById(R.id.registrationPassword);
        Button mRegistrationSubmitButton = findViewById(R.id.registrationSubmitButton);
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
                    attemptRegistration(mEmailView.getText().toString(), mPasswordView.getText().toString());
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    private void attemptRegistration(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            goToBlockActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("RegisterActivity", "createUserWithEmail:failure");
                        }
                    }
                });
    }

    private void goToBlockActivity(){
        Intent intent = new Intent(this, BlockActivity.class);
        startActivity(intent);
        finish();
    }
}