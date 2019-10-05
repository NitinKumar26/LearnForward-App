package com.learnforward;

import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginEmailPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @BindView(R.id.email)
    EditText edTvEmail;
    @BindView(R.id.password)
    EditText edTvPassword;
    @BindView(R.id.sign_up_button)
    Button signUpButton;
    @BindView(R.id.btn_reset_password)
    Button resetPswrdBtn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @OnClick (R.id.btn_reset_password)
    public void resetPassword(){
        startActivity(new Intent(LoginEmailPasswordActivity.this, ResetPasswordActivity.class));
    }

    @OnClick(R.id.sign_up_button)
    public void signUP(){
        String email = edTvEmail.getText().toString().trim();
        String password = edTvPassword.getText().toString().trim();
        if (!email.isEmpty() && !password.isEmpty()){
            if (password.length() < 6 ){
                Toast.makeText(LoginEmailPasswordActivity.this, "Password too short, enter minimum 6 characters.", Toast.LENGTH_SHORT).show();
            }else{
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            //Toast.makeText(LoginEmailPasswordActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginEmailPasswordActivity.this, NewMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginEmailPasswordActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }else{
            Toast.makeText(LoginEmailPasswordActivity.this, "Please enter the required details", Toast.LENGTH_SHORT).show();
        }
    }
}
