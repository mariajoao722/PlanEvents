package com.example.eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetalhesLugar extends AppCompatActivity implements View.OnClickListener{

    TextView nomeLugar, localizacaoLugar, descricaoLugar, tarefaLugar,pontosLugar,penalizacaoLugar, obrigatorioLugar;
    Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_lugar);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle extras = getIntent().getExtras();
        String newString = null;
        String newString2 = null;
        if (extras != null) {
            newString = extras.getString("nome"); // nome do lugar
            newString2 = extras.getString("evento"); // nome do evento
        }

        nomeLugar = findViewById(R.id.titleName);
        localizacaoLugar = findViewById(R.id.profileLoc);
        descricaoLugar = findViewById(R.id.profileDesc);
        tarefaLugar = findViewById(R.id.profileTarefa);
        pontosLugar = findViewById(R.id.profilePontos);
        penalizacaoLugar = findViewById(R.id.profilePenalizacao);
        obrigatorioLugar = findViewById(R.id.profileObrigatorio);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(this);

        nomeLugar.setText(newString);

        FirebaseDatabase.getInstance().getReference("Eventos").child(newString2).child("Lugares").child(newString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Lugar lugar = snapshot.getValue(Lugar.class);

                if (lugar != null) {
                    String localizacao = lugar.localizacao;
                    String descricao = lugar.descricao;
                    String tarefa = lugar.tarefa;
                    String pontos = lugar.pontos;
                    String penalizacao = lugar.penalizacao;
                    String obrigatorio = lugar.obrigatorio;

                    localizacaoLugar.setText(localizacao);
                    descricaoLugar.setText(descricao);
                    tarefaLugar.setText(tarefa);
                    pontosLugar.setText(pontos);
                    penalizacaoLugar.setText(penalizacao);
                    obrigatorioLugar.setText(obrigatorio);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetalhesLugar.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnVoltar:
                finish();
                break;
        }
    }
}