package com.community.jboss.leadmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.community.jboss.leadmanagement.main.MainActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private EditText mEmail, mPassword;
    private int SIGN_UP = 123;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(R.string.sign_in);
        initViews();

    }

    void initViews() {
        TextView tv = findViewById(R.id.tv_not_registered);
        tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv.setOnClickListener(this);

        mEmail = findViewById(R.id.et_email_login);
        mPassword = findViewById(R.id.et_password_login);

        findViewById(R.id.btn_sign_in).setOnClickListener(this);
        findViewById(R.id.btn_sign_in_phone).setOnClickListener(this);
        findViewById(R.id.btn_sign_in_google).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.loading);

        switch (view.getId()) {
            case R.id.tv_not_registered:
                Intent i = new Intent(this, SignUpActivity.class);
                startActivityForResult(i, SIGN_UP);
                break;
            case R.id.btn_sign_in:
                if (validateForms()) {
                    progressDialog.show();

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                            mEmail.getText().toString(), mPassword.getText().toString())
                            .addOnSuccessListener(authResult -> {
                                Intent intent = new Intent(this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                progressDialog.cancel();
                            }).addOnFailureListener(e -> {
                        e.printStackTrace();
                        Toast.makeText(this, "Error signing in", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    });
                }
                break;
            case R.id.btn_sign_in_phone:
                Intent intent = new Intent(this, LoginPhoneActivity.class);
                startActivityForResult(intent, SIGN_UP);
                break;
            case R.id.btn_sign_in_google:
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .enableAutoManage(this, this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP && resultCode == RESULT_OK) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                firebaseAuthWithGoogle(acct);
            } else {
                Toast.makeText(this, "There was a trouble signing in-Please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Authentication pass.",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    boolean validateForms() {
        boolean valid = true;

        if (mEmail.getText().toString().isEmpty()) {
            mEmail.setError("Fill in");
            valid = false;
        }

        if (mPassword.getText().toString().length() < 6) {
            mPassword.setError("Password can't be less then 6 symbols");
            valid = false;
        }

        if (mPassword.getText().toString().isEmpty()) {
            mPassword.setError("Fill in");
            valid = false;
        }

        return valid;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed.", Toast.LENGTH_SHORT).show();
    }

}
