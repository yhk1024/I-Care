package com.example.i_care;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Sign_signup extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText emailEditText, passwordEditText;
    private Button btn_signup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_signup);

        TextView privacyPolicyText = findViewById(R.id.text_privacy_policy);
        privacyPolicyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 개인정보 처리방침 다이얼로그를 표시
                PrivacyPolicyDialog privacyPolicyDialog = new PrivacyPolicyDialog();
                privacyPolicyDialog.show(getSupportFragmentManager(), "privacyPolicyDialog");
            }
        });

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        btn_signup = findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Sign_signup.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Sign_signup.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(Sign_signup.this, "Password too short, enter minimum 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                registerUser(email, password);
            }
        });
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Sign_signup.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                        // Navigate to login or main activity

                        // 1초 뒤 뒤로 뒤로 가기
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                //딜레이 후 시작할 코드 작성
                                finish();
                            }
                        }, 1000);// 1초 정도 딜레이를 준 후 시작
                    } else {
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthWeakPasswordException e) {
                            passwordEditText.setError("Weak password.");
                            passwordEditText.requestFocus();
                        } catch (FirebaseAuthUserCollisionException e) {
                            emailEditText.setError("User already exists.");
                            emailEditText.requestFocus();
                        } catch (Exception e) {
                            Toast.makeText(Sign_signup.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}