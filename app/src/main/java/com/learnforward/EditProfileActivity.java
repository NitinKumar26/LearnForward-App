package com.learnforward;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.learnforward.helper.PrefManager;

public class EditProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();

        PrefManager prefManager = new PrefManager(getApplicationContext());

        String name = prefManager.getUserName();

        TextView tvName = findViewById(R.id.name);
        tvName.setText(name);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        TextView tvEmail = findViewById(R.id.email);
        if (mAuth.getCurrentUser() != null) tvEmail.setText(mAuth.getCurrentUser().getEmail());

    }
}
