package com.example.eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResponderUser extends AppCompatActivity implements View.OnClickListener {

    private String nomeEv, nomeLugar;

    private TextView txtNomeLugar,txtDescricao,txtTarefa;
    private EditText edtResposta;
    private Button btnSubmeter;

    private FirebaseUser user;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_user);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        txtNomeLugar = findViewById(R.id.titleName);
        txtDescricao = findViewById(R.id.Descricaotxt);
        txtTarefa = findViewById(R.id.Tarefatxt);
        edtResposta = findViewById(R.id.Respostatxt);
        btnSubmeter = findViewById(R.id.btnSubmeter);
        btnSubmeter.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        Bundle extras = getIntent().getExtras();
        nomeLugar = null;
        nomeEv = null;
        if (extras != null) {
            nomeLugar = extras.getString("nome"); // nome do lugar
            nomeEv = extras.getString("evento"); // nome do evento
        }

        FirebaseDatabase.getInstance().getReference("Eventos").child(nomeEv).child("Inscritos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("id").getValue() != null && ds.child("id").getValue().toString().equals(userID)) {
                        DataSnapshot respostaSnapshot = ds.child("Respostas").child(nomeLugar).child("resposta");
                        if (respostaSnapshot.exists()) {
                            String resposta = respostaSnapshot.getValue(String.class);
                            if (resposta != null) {
                                edtResposta.setText(resposta);
                            } else {
                                edtResposta.setText("");
                            }
                        } else {
                            edtResposta.setText("");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        txtNomeLugar.setText(nomeLugar);

        FirebaseDatabase.getInstance().getReference("Eventos").child(nomeEv).child("Lugares").child(nomeLugar).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Lugar lugar = snapshot.getValue(Lugar.class);

                if (lugar != null) {
                    String descricao = lugar.descricao;
                    String tarefa = lugar.tarefa;

                    txtDescricao.setText(descricao);
                    txtTarefa.setText("Tarefa: "+tarefa);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ResponderUser.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmeter:
                FirebaseDatabase.getInstance().getReference("Eventos").child(nomeEv).child("Inscritos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            if (ds.child("id").getValue().toString().equals(userID)) {
                                FirebaseDatabase.getInstance().getReference("Eventos").child(nomeEv).child("Inscritos").child(ds.getKey()).child("Respostas").child(nomeLugar).child("resposta").setValue(edtResposta.getText().toString());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                finish();
                break;
        }
    }
}