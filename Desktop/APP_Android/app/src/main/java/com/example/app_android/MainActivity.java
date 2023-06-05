package com.example.app_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    Button btnAjuda;
    RadioButton rbAvaria, rbAcidente, rbCombustivel, rbChaves, rbPneus;
    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nav = findViewById(R.id.bottomNavigationView);
        btnAjuda = findViewById(R.id.btnAjuda);

        rbAvaria = findViewById(R.id.rbAvaria);
        rbAcidente = findViewById(R.id.rbAcidente);
        rbChaves = findViewById(R.id.rbChaves);
        rbCombustivel = findViewById(R.id.rbCombustivel);
        rbPneus = findViewById(R.id.rbPneus);

        btnAjuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rbAvaria.isChecked() || rbAcidente.isChecked() || rbChaves.isChecked() || rbCombustivel.isChecked() || rbPneus.isChecked()) {
                    startActivity(new Intent(MainActivity.this,GetLocationActivity.class));
                }else{
                    Toast.makeText(MainActivity.this, "Por favor, selecione uma opção!", Toast.LENGTH_SHORT).show();
                }

            }
        });



        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    Toast.makeText(MainActivity.this, "Menu Principal", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.profile) {
                    Toast.makeText(MainActivity.this, "Perfil", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                }



                return true;
            }
        });

    }
}