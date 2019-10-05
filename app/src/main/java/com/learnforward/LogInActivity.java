package com.learnforward;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.learnforward.Models.BackdropItem;
import com.learnforward.helper.HelperMethods;
import com.learnforward.helper.PrefManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity {
    private ArrayList<BackdropItem> mViewPagerList;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog  pDialog;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private SliderAdapter sliderAdapter;
    private Uri profileImageUri;
    private String username;
    @BindView(R.id.viewPager)
    ViewPager viewpager;
    @BindView(R.id.indicator)
    TabLayout indicator;
    @BindView(R.id.tv_other_email)
    TextView tvOtherEmail;
    @BindView(R.id.tv_terms)
    TextView tvTerms;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        mAuth = FirebaseAuth.getInstance();

        ButterKnife.bind(this);


        mViewPagerList = new ArrayList<>();
        mViewPagerList.add(new BackdropItem(R.drawable.backdrop));
        mViewPagerList.add(new BackdropItem(R.drawable.backdrop));
        mViewPagerList.add(new BackdropItem(R.drawable.backdrop));
        mViewPagerList.add(new BackdropItem(R.drawable.backdrop));
        mViewPagerList.add(new BackdropItem(R.drawable.backdrop));

        sliderAdapter = new SliderAdapter(LogInActivity.this, mViewPagerList);
        viewpager.setPageMargin(HelperMethods.pix(LogInActivity.this, 12));
        viewpager.setAdapter(sliderAdapter);
        indicator.setupWithViewPager(viewpager, true);

        tvOtherEmail.setPaintFlags(tvOtherEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvTerms.setPaintFlags(tvTerms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(LogInActivity.this, gso);

        LinearLayout btnGoogle = findViewById(R.id.btn_google);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog = new ProgressDialog(LogInActivity.this);
                pDialog.setMessage("Please wait");
                pDialog.show();
                signIn(); //Start Sign in with Google
            }
        });

        TextView tvFacultyLogin = findViewById(R.id.tv_faculty_login);
        tvFacultyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this, FacultyLoginActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check if the user is signed in (non-null) and update the UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //Toast.makeText(LogInActivity.this, "User is already logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LogInActivity.this, NewMainActivity.class)); //Start Main Activity
            finish(); //Finish this activity
        }
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google sign in was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    profileImageUri = account.getPhotoUrl();
                    username = account.getDisplayName();
                    firebaseAuthWithGoogle(account);
                }

                Toast.makeText(LogInActivity.this, "Google Sign in successful ", Toast.LENGTH_SHORT).show();
            }catch (ApiException e){
                //Google Sign in failed update the UI accordingly
                if (pDialog.isShowing()) pDialog.hide();
                Toast.makeText(LogInActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                Log.w("LoginActivity", "Google Sign in Failed", e.getCause());
            }
        }
    }


    // [START auth_with_google]
    private void firebaseAuthWithGoogle(@NonNull GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            PrefManager prefManager = new PrefManager(getApplicationContext());

                            if (pDialog.isShowing()) pDialog.hide();
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LogInActivity.this, NewMainActivity.class);
                            prefManager.setUserData(username, profileImageUri);
                            //if (user != null) Toast.makeText(LogInActivity.this, "Authentication Success" + user.toString(), Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());

                            Toast.makeText(LogInActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    @OnClick (R.id.tv_other_email)
    public void tvOtherEmail(){
        startActivity(new Intent(LogInActivity.this, SignupEmailPswrdActivity.class));
    }
}
