package com.example.eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DetalheInscritos extends AppCompatActivity implements View.OnClickListener {

    private String newString,evento,ID;
    private TextView nome,email,idade,telefone,monitor;

    Button btnMonitor, btnLoc,btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_inscritos);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        nome = findViewById(R.id.titleName);
        email = findViewById(R.id.profileEmail);
        idade = findViewById(R.id.profileIdade);
        telefone = findViewById(R.id.profileTele);
        monitor = findViewById(R.id.profileMonitor);

        btnMonitor = findViewById(R.id.btnMonitor);
        btnMonitor.setOnClickListener(this);

        btnLoc = findViewById(R.id.btnLoc);
        btnLoc.setOnClickListener(this);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        newString = null;
        evento = null;
        if (extras != null) {
            newString = extras.getString("nome"); // nome do inscrito
            evento = extras.getString("evento"); // nome do evento
            ID = extras.getString("id"); // ID do inscrito
        }

        nome.setText(newString);

        FirebaseDatabase.getInstance().getReference("Users").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String email1 = userProfile.email;
                    String idade1 = userProfile.idade;
                    String telefone1 = userProfile.telefone; ;
                    String monitor1 = String.valueOf(userProfile.is_monitor);
                    email.setText(email1);
                    idade.setText(idade1);
                    telefone.setText(telefone1);
                    if (monitor1.equals("1") ) {
                        monitor.setText("Monitor");
                        btnMonitor.setVisibility(View.GONE);
                    }else {
                        monitor.setText("Não é monitor");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetalheInscritos.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMonitor:

                FirebaseDatabase.getInstance().getReference("Users").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            FirebaseDatabase.getInstance().getReference("Users").child(ID).child("is_monitor").setValue(1);
                            Toast.makeText(DetalheInscritos.this, "Monitor definido com sucesso!", Toast.LENGTH_LONG).show();
                            monitor.setText("Monitor");
                            btnMonitor.setVisibility(View.GONE);
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DetalheInscritos.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case R.id.btnLoc:
                Intent intent = new Intent(DetalheInscritos.this, MapsActivity8.class);
                intent.putExtra("nome", newString); // nome do inscrito
                intent.putExtra("evento", evento); // nome do evento
                intent.putExtra("id", ID); // ID do inscrito
                startActivity(intent);
                break;

            case R.id.btnVoltar:
                finish();
                break;
        }

    }
}