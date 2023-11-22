package com.example.eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class AdminActivity extends AppCompatActivity  implements View.OnClickListener{
    Button sair,volta;
    TextView nome,email,telefone,idade;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        nome = findViewById(R.id.titleName);
        email = findViewById(R.id.profileEmail);
        telefone = findViewById(R.id.profileTelemovel);
        idade = findViewById(R.id.profileIdade);

        sair = findViewById(R.id.btnSair);
        sair.setOnClickListener(this);

        volta = findViewById(R.id.btnVoltar);
        volta.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String nome1  = userProfile.nome;
                    String email1 = userProfile.email;
                    String idade1 = userProfile.idade;
                    String telefone1 = userProfile.telefone; ;
                    nome.setText(nome1);
                    email.setText(email1);
                    idade.setText(idade1);
                    telefone.setText(telefone1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminActivity.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSair:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AdminActivity.this, Login.class);
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