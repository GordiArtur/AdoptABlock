package com.example.gordiartur.adoptablock;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mEmailView;
    private EditText mPasswordView;
    private TextView mErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // UI Setup
        mErrorMessage = findViewById(R.id.errorCode);
        mEmailView = findViewById(R.id.registrationEmail);
        mPasswordView = findViewById(R.id.registrationPassword);
        Button mRegistrationSubmitButton = findViewById(R.id.registrationSubmitButton);
        mRegistrationSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();
                attemptRegistration(mEmailView.getText().toString(), mPasswordView.getText().toString());
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    private void goToBlockActivity(){
        Intent intent = new Intent(this, BlockActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkFields() {
        if(mEmailView.getText() == null) {
            mErrorMessage.setText("Please enter an email");
            mErrorMessage.setVisibility(View.VISIBLE);
        } else if(mPasswordView.getText() == null) {
            mErrorMessage.setText("Please enter a password");
            mErrorMessage.setVisibility(View.VISIBLE);
        } else {
            mErrorMessage.setText("No Error");
            mErrorMessage.setVisibility(View.INVISIBLE);
        }
    }

    private void attemptRegistration(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("RegisterActivity", "createUserWithEmail:success");
                            goToBlockActivity();
                        } else {
                            mErrorMessage.setText("User Creation Failed");
                            Log.d("RegisterError", Exception.class.getName());
                            Log.d("RegisterActivity", "createUserWithEmail:failure");
                        }
                    }
                });
    }
}