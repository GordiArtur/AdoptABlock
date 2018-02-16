package com.example.gordiartur.adoptablock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button mRegistrationSubmitButton = findViewById(R.id.registrationSubmitButton);
        mRegistrationSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyFields();
                goToBlockActivity();
            }
        });
    }

    private void goToBlockActivity(){
        Intent intent = new Intent(this, BlockActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean verifyFields(){
        return true;
    }
}