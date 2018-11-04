package com.community.jboss.leadmanagement;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private TextView mEmail, mPassword, mPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle(R.string.sign_up);

        mEmail = findViewById(R.id.et_email_sign_up);
        mPassword = findViewById(R.id.et_password_sign_up);
        mPassword2 = findViewById(R.id.et_repeat_password_sign_up);

        findViewById(R.id.btn_sign_up).setOnClickListener(view -> {
            if (validateForms()) {
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle(R.string.loading);
                progressDialog.show();

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        mEmail.getText().toString(), mPassword.getText().toString()
                ).addOnSuccessListener(authResult -> {
                    setResult(RESULT_OK);
                    finish();
                    progressDialog.cancel();
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(this, "Error signing up", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                });
            }
        });
    }

    boolean validateForms() {
        boolean valid = true;

        if (mEmail.getText().toString().isEmpty()) {
            mEmail.setError("Fill in");
            valid = false;
        }

        if (!mPassword.getText().toString().equals(mPassword2.getText().toString())) {
            mPassword2.setError("Passwords are different");
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

}
