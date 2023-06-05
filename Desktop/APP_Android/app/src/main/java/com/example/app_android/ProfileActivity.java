package com.example.app_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView nav;
    private FirebaseFirestore db;
    EditText inputNome;
    EditText inputEmail;
    EditText inputNIF;
    EditText inputMatricula;
    EditText inputNumTelemovel;
    Button btnEditProfile;
    Button btnSaveProfile;
    TextView txtLogOut;

    private boolean isEditing = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = FirebaseFirestore.getInstance();
        inputNome = findViewById(R.id.textNome);
        inputEmail = findViewById(R.id.textEmail);
        inputNIF = findViewById(R.id.textNIF);
        inputMatricula = findViewById(R.id.textMatricula);
        inputNumTelemovel = findViewById(R.id.textTelemovel);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        nav = findViewById(R.id.bottomNavigationView);
        txtLogOut = findViewById(R.id.txtLogOut);


        // Carregar as informações do perfil
        loadProfileData();

        btnEditProfile.setOnClickListener(view -> {
            if (!isEditing) {
                // Ativar o modo de edição
                setInputFieldsEnabled(true);
                btnEditProfile.setVisibility(View.GONE);
                btnSaveProfile.setVisibility(View.VISIBLE);
                isEditing = true;
            }
        });

        btnSaveProfile.setOnClickListener(view -> {
            if (isEditing) {
                // Desativar o modo de edição
                setInputFieldsEnabled(false);
                btnEditProfile.setVisibility(View.VISIBLE);
                btnSaveProfile.setVisibility(View.GONE);
                isEditing = false;

                // Salvar as informações atualizadas no Firestore
                saveProfileData();
            }
        });


        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    Toast.makeText(ProfileActivity.this, "Menu Principal", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                } else if (itemId == R.id.profile) {
                    Toast.makeText(ProfileActivity.this, "Perfil", Toast.LENGTH_SHORT).show();
                }



                return true;
            }
        });


        txtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                Toast.makeText(ProfileActivity.this, "A sua sessão foi encerrada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setInputFieldsEnabled(boolean enabled) {
        inputNome.setEnabled(enabled);
        inputEmail.setEnabled(enabled);
        inputNIF.setEnabled(enabled);
        inputMatricula.setEnabled(enabled);
        inputNumTelemovel.setEnabled(enabled);
    }

    private void loadProfileData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DocumentReference userRef = db.collection("Users").document(uid);

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String nome = documentSnapshot.getString("Nome");
                    String email = documentSnapshot.getString("Email");
                    String nif = documentSnapshot.getString("NIF");
                    String matricula = documentSnapshot.getString("Matricula");
                    String numTelemovel = documentSnapshot.getString("Telemovel");

                    inputNome.setText(nome);
                    inputEmail.setText(email);
                    inputNIF.setText(nif);
                    inputMatricula.setText(matricula);
                    inputNumTelemovel.setText(numTelemovel);
                }
            }).addOnFailureListener(e -> {
                // Falha ao obter as informações do perfil
                Toast.makeText(ProfileActivity.this, "Falha ao carregar as informações do seu perfil", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void saveProfileData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DocumentReference userRef = db.collection("Users").document(uid);

            Map<String, Object> data = new HashMap<>();
            data.put("Nome", inputNome.getText().toString());
            data.put("Email", inputEmail.getText().toString());
            data.put("NIF", inputNIF.getText().toString());
            data.put("Matricula", inputMatricula.getText().toString());
            data.put("Telemovel", inputNumTelemovel.getText().toString());

            userRef.update(data)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ProfileActivity.this, "As informações do seu perfil foram atualizadas com sucesso", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProfileActivity.this, "Falha ao atualizar as informações do seu perfil", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
