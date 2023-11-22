package com.example.eventos;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuAdmin extends AppCompatActivity  implements View.OnClickListener{

    Button verEv,perfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        verEv = findViewById(R.id.verEv);
        verEv.setOnClickListener(this);


        perfil = findViewById(R.id.profile);
        perfil.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.verEv:
                //redirecionar para a pagina de ver eventos
                startActivity(new Intent(MenuAdmin.this, ShowEventos.class));
                break;
            case R.id.profile:
                startActivity(new Intent(MenuAdmin.this, AdminActivity.class));
                break;
        }
    }
}