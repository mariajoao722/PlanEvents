package com.example.eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditarLugar extends AppCompatActivity implements View.OnClickListener{

    private String newString,evento;

    private EditText tarefa,decricao,penalizacao,pontos;
    private TextView nome,localizacao;
    private CheckBox obrigatorio;
    private Button atualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_lugar);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tarefa = findViewById(R.id.txtTarefa);
        decricao = findViewById(R.id.txtDesc);
        penalizacao = findViewById(R.id.txtPenalizacao);
        pontos = findViewById(R.id.txtPontos);
        nome = findViewById(R.id.txtName);
        localizacao = findViewById(R.id.txtLocalizacao);
        obrigatorio = findViewById(R.id.cbObrigatorio);

        atualizar = findViewById(R.id.btnUpdate);
        atualizar.setOnClickListener(this);

        localizacao.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        newString = null;
        evento = null;
        if (extras != null) {
            newString = extras.getString("nome"); // nome do lugar
            evento = extras.getString("evento"); // nome do evento
        }


        FirebaseDatabase.getInstance().getReference().child("Eventos").child(evento).child("Lugares").child(newString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Lugar lugar = snapshot.getValue(Lugar.class);

                if (lugar != null) {
                    String tarefaLugar = lugar.tarefa;
                    String descricaoLugar = lugar.descricao;
                    String penalizacaoLugar = lugar.penalizacao;
                    String pontosLugar = lugar.pontos;
                    String nomeLugar = lugar.nomel;
                    String localizacaoLugar = lugar.localizacao;
                    String obrigatorioLugar = lugar.obrigatorio;

                    tarefa.setText(tarefaLugar);
                    decricao.setText(descricaoLugar);
                    penalizacao.setText(penalizacaoLugar);
                    pontos.setText(pontosLugar);
                    nome.setText(nomeLugar);
                    localizacao.setText(localizacaoLugar);
                    if (obrigatorioLugar.equals("1")){
                        obrigatorio.setChecked(true);
                    }else{
                        obrigatorio.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditarLugar.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUpdate:
                String obrigatorioLugar = "";
                if (obrigatorio.isChecked()) {
                    obrigatorioLugar = "1";
                }else{
                    obrigatorioLugar = "0";
                }

                FirebaseDatabase.getInstance().getReference().child("Eventos").child(evento).child("Lugares").child(newString).child("descricao").setValue(decricao.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Eventos").child(evento).child("Lugares").child(newString).child("tarefa").setValue(tarefa.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Eventos").child(evento).child("Lugares").child(newString).child("penalizacao").setValue(penalizacao.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Eventos").child(evento).child("Lugares").child(newString).child("pontos").setValue(pontos.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Eventos").child(evento).child("Lugares").child(newString).child("obrigatorio").setValue(obrigatorioLugar);
                FirebaseDatabase.getInstance().getReference().child("Eventos").child(evento).child("Lugares").child(newString).child("localizacao").setValue(localizacao.getText().toString());

                // voltar para atividade anterior
                finish();

                Toast.makeText(EditarLugar.this, "Lugar atualizado com sucesso!", Toast.LENGTH_LONG).show();


                break;
            case R.id.txtLocalizacao:
                break;
        }

    }
}