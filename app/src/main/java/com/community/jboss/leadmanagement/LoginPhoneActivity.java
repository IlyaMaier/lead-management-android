package com.community.jboss.leadmanagement;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginPhoneActivity extends AppCompatActivity {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private EditText mPhone, mVerification;
    private String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);

        setTitle(R.string.sign_in_with_phone_number);

        mPhone = findViewById(R.id.et_number_login);
        mVerification = findViewById(R.id.et_verification_login);

        findViewById(R.id.btn_send_sms).setOnClickListener(view -> {
            if (validateForm())
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        mPhone.getText().toString(),
                        2,
                        TimeUnit.MINUTES,
                        this,
                        mCallbacks);
        });

        findViewById(R.id.btn_sign_in_phone_activity).setOnClickListener(view -> {
            if (validateFormVerification()) {
                PhoneAuthCredential phoneAuthCredential =
                        PhoneAuthProvider.getCredential(mVerificationId, mVerification.getText().toString());
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                e.printStackTrace();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    boolean validateForm() {
        if (mPhone.getText().toString().isEmpty()) {
            mPhone.setError("Fill in");
            return false;
        }
        return true;
    }

    boolean validateFormVerification() {
        if (mVerification.getText().toString().isEmpty()) {
            mVerification.setError("Fill in");
            return false;
        }
        return true;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.loading);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            mVerification.setError("Invalid code.");
                        }
                    }
                    progressDialog.cancel();
                });
    }

}
