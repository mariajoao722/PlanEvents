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
import com.google.firebase.database.ValueEventListener;

public class DetalhesEvento extends AppCompatActivity  implements View.OnClickListener{

    TextView nomeEvento, dataEvento, horaEvento, localizacaoEvento, descricaoEvento,custoEvento,duracaoEvento,inscritosEvento;
    Button btnLugares, btnPessoas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_evento);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle extras = getIntent().getExtras();
        String newString = null;
        if (extras != null) {
            newString = extras.getString("nome"); // nome do evento
        }

        nomeEvento = findViewById(R.id.titleName);
        dataEvento = findViewById(R.id.profileData);
        horaEvento = findViewById(R.id.profileHora);
        localizacaoEvento = findViewById(R.id.profileLoc);
        descricaoEvento = findViewById(R.id.profileDesc);
        custoEvento = findViewById(R.id.profileCusto);
        duracaoEvento = findViewById(R.id.profileDuracao);
        inscritosEvento = findViewById(R.id.profileInscritos);

        btnLugares = findViewById(R.id.btnLugares);
        btnLugares.setOnClickListener(this);

        btnPessoas = findViewById(R.id.btnPessoas);
        btnPessoas.setOnClickListener(this);


        nomeEvento.setText(newString);

        FirebaseDatabase.getInstance().getReference("Eventos").child(newString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Eventos evento = snapshot.getValue(Eventos.class);

                if (evento != null) {
                    String data = evento.data;
                    String hora = evento.hora_inicio;
                    String localizacao = evento.localizacao;
                    String descricao = evento.descricao;
                    String custo = evento.custo;
                    String duracao = evento.duracao;
                    String inscritos = evento.n_inscritos;
                    String max_inscritos = evento.n_maximo;
                    dataEvento.setText(data);
                    horaEvento.setText(hora+"h");
                    localizacaoEvento.setText(localizacao);
                    descricaoEvento.setText(descricao);
                    custoEvento.setText(custo+"€");
                    duracaoEvento.setText(duracao+"h");
                    inscritosEvento.setText(inscritos+"/"+max_inscritos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetalhesEvento.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLugares:
                Intent intent = new Intent(DetalhesEvento.this, ShowLugares.class);
                intent.putExtra("nome",nomeEvento.getText().toString());
                startActivity(intent);
                break;
            case R.id.btnPessoas:
                Intent intent2 = new Intent(DetalhesEvento.this, ShowPessoas.class);
                intent2.putExtra("nome",nomeEvento.getText().toString());
                startActivity(intent2);
                break;
        }

    }
}