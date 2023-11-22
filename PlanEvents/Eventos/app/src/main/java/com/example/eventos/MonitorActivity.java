package com.example.eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MonitorActivity extends AppCompatActivity implements View.OnClickListener{
    Button sair;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private Button voltar;

    private TextView profileName, profileEmail, profileIdade, profileTelefone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        sair = findViewById(R.id.btnSair);
        sair.setOnClickListener(this);

        voltar = findViewById(R.id.btnVoltar);
        voltar.setOnClickListener(this);

        profileName = findViewById(R.id.titleName);
        profileEmail = findViewById(R.id.profileEmail);
        profileIdade = findViewById(R.id.profileIdade);
        profileTelefone = findViewById(R.id.profileTelemovel);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String nome = userProfile.nome;
                    String email = userProfile.email;
                    String idade = userProfile.idade;
                    String telefone = userProfile.telefone; ;
                    profileName.setText(nome);
                    profileEmail.setText(email);
                    profileIdade.setText(idade);
                    profileTelefone.setText(telefone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MonitorActivity.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSair:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MonitorActivity.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;

            case R.id.btnVoltar:
                finish();
                break;
        }
    }
}