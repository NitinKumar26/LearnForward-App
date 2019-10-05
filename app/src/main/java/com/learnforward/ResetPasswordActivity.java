package com.learnforward;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @BindView(R.id.email)
    EditText edTvEmail;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
    }

    @OnClick (R.id.btn_reset_password_now)
    public void resetPassword(){
        String email = edTvEmail.getText().toString().trim();
        if (!email.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            Toast.makeText(ResetPasswordActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick (R.id.btn_back)
    public void btnBAck(){
        finish();
    }
}
