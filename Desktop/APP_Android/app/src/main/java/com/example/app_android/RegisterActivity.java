package com.example.app_android;

import static android.content.ContentValues.TAG;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mAuth = FirebaseAuth.getInstance();
        final TextView alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);
        final Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registar();
            }
        });


        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

            }
        });
    }

    protected void registar() {
        mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        final EditText inputNome = findViewById(R.id.inputNome);
        final EditText inputEmail = findViewById(R.id.inputEmail);
        final EditText inputPassword = findViewById(R.id.inputPassword);
        final EditText inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        final EditText inputNIF = findViewById(R.id.inputNIF);
        final EditText inputMatricula = findViewById(R.id.inputMatricula);
        final EditText inputNumTelemovel = findViewById(R.id.inputNumTelemovel);

        final int max = 9;

        final String nome = inputNome.getText().toString().trim();
        final String email = inputEmail.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();
        final String confirmPassword = inputConfirmPassword.getText().toString().trim();
        final String nif = inputNIF.getText().toString().trim();
        final String matricula = inputMatricula.getText().toString().trim();
        final String telemovel = inputNumTelemovel.getText().toString().trim();

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Verifica se os campos estão preenchidos antes de enviar a memória para a firebase
        if (nome.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || nif.isEmpty() || matricula.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
        }
        // Verifica as condições e mostra a Toast message
        else if (!email.matches(emailPattern)) {
            //Verifica se formato do email inserido coincide com o emailPattern
            Toast.makeText(RegisterActivity.this, "Insira um email válido!", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            //Verifica se as palavras passe são iguais
            Toast.makeText(RegisterActivity.this, "As palavras passe não coincidem!", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            //Verifica se palavra passe tem 6 ou mais caracteres/numeros
            Toast.makeText(RegisterActivity.this, "Insira uma palavra passe válida!", Toast.LENGTH_SHORT).show();
        } else if (!nif.matches("[0-9]+") || nif.length() != max) {
            //Verifica se o nif é constituído apenas por número e se tem no máximo 9 digitos
            Toast.makeText(RegisterActivity.this, "Insira um NIF válido!", Toast.LENGTH_SHORT).show();
        } else if (!telemovel.matches("[0-9]+") || telemovel.length() != max) {
            //Verifica se o num de telemovel é constituído apenas por números e se tem no máximo 9 digitos (PT)
            Toast.makeText(RegisterActivity.this, "Insira um número de telemóvel válido!", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, String> docData = new HashMap<>();
            docData.put("Nome", nome);
            docData.put("NIF", nif);
            docData.put("Matricula", matricula);
            docData.put("Telemovel", telemovel);
            docData.put("Email", email);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    String uid = user.getUid();
                                    System.out.println(uid);

                                    db.collection("Users").document(uid)
                                            .set(docData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }

                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
