package com.example.eventos;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuUser extends AppCompatActivity implements View.OnClickListener{
    Button verEv,adcEv, perfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        verEv = findViewById(R.id.consultarEv);
        verEv.setOnClickListener(this);

        adcEv = findViewById(R.id.aderirEv);
        adcEv.setOnClickListener(this);

        perfil = findViewById(R.id.perfil);
        perfil.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.consultarEv:
                startActivity(new Intent(MenuUser.this, ShowEventosUser.class));
                break;
            case R.id.aderirEv:
                // redirect para os eventos todos
                startActivity(new Intent(MenuUser.this, ShowAllEventos.class));
                break;
            case R.id.perfil:
                startActivity(new Intent(MenuUser.this, ProfileActivity.class));
                break;
        }
    }
}