package com.example.app_android;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassScreenPage extends AppCompatActivity {

    TextView backToLogin;
    Button btnGetLink;

    EditText email;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_screen_page);

        email = findViewById(R.id.inputNumTelemovel);
        backToLogin = findViewById(R.id.backToLogin);
        btnGetLink = findViewById(R.id.btnGetLink);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPassScreenPage.this,LoginActivity.class));
            }
        });

        btnGetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().length() > 0) {
                    String emailAddress = email.getText().toString();

                    mAuth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPassScreenPage.this, "Verifique o seu email", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ForgotPassScreenPage.this, "O link não foi enviado", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(ForgotPassScreenPage.this, "Por favor, insira o seu email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
