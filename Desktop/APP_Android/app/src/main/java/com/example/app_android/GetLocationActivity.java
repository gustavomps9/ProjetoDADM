package com.example.app_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetLocationActivity extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    TextView country, city, address, latitude, longitude;
    Button btnGetLocation;
    Button btnBackToMenu;
    private final static int REQUEST_CODE = 100;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        address = findViewById(R.id.address);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        btnGetLocation = findViewById(R.id.btnGetLocation);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);

        //Funcionalidade de voltar ao Menu Principal
        btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GetLocationActivity.this,MainActivity.class));
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
                Toast.makeText(GetLocationActivity.this, "O seu pedido de ajuda foi enviado!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getLastLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        Geocoder geocoder = new Geocoder(GetLocationActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            latitude.setText("Latitude: " +addresses.get(0).getLatitude());
                            longitude.setText("Longitude: " +addresses.get(0).getLongitude());
                            address.setText("Morada: " +addresses.get(0).getAddressLine(0));
                            city.setText("Cidade: " +addresses.get(0).getLocality());
                            country.setText("País: " +addresses.get(0).getCountryName());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                    }
                }
            });

        }else{
            aksPermission();
        }
    }

    private void aksPermission() {
        ActivityCompat.requestPermissions(GetLocationActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                Toast.makeText(this, "É necessária a permissão", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}