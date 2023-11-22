package com.example.eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Registar extends AppCompatActivity implements View.OnClickListener{

    private EditText ETnome,ETidade,ETtelefone,ETemail,ETpassword;

    private Button registar;
    private TextView logIn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ETnome = findViewById(R.id.Nome);
        ETpassword = findViewById(R.id.password);
        ETidade = findViewById(R.id.idade);
        ETtelefone = findViewById(R.id.telefone);
        ETemail = findViewById(R.id.email);

        progressBar = findViewById(R.id.progressBar);

        logIn = findViewById(R.id.login);
        logIn.setOnClickListener(this);

        registar = findViewById(R.id.registar);
        registar.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.login:
               System.out.println("Login");
               startActivity(new Intent(this, Login.class));
               break;
           case R.id.registar:
                registarUser();
               break;
       }
    }

    private void registarUser() {
        String email = ETemail.getText().toString().trim();
        String password = ETpassword.getText().toString().trim();
        String nome = ETnome.getText().toString().trim();
        String idade = ETidade.getText().toString().trim();
        String telefone = ETtelefone.getText().toString().trim();

        if (nome.isEmpty()) {
            ETnome.setError("1º e ultimo nome são necessários");
            ETnome.requestFocus();
            return;
        }

        if (idade.isEmpty()) {
            ETidade.setError("Idade é necessária");
            ETidade.requestFocus();
            return;
        }

        if (Integer.parseInt(idade) < 18) {
            ETidade.setError("Tem de ser maior de idade");
            ETidade.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            ETemail.setError("Email é necessário");
            ETemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ETemail.setError("Por favor introduza um email válido");
            ETemail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            ETpassword.setError("Password é necessária");
            ETpassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            ETpassword.setError("Password tem de ter pelo menos 6 caracteres");
            ETpassword.requestFocus();
            return;
        }

        if (telefone.isEmpty()) {
            ETtelefone.setError("Telefone é necessário");
            ETtelefone.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(nome, idade,telefone, email,0,0);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Registar.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(Registar.this, Login.class));
                                    } else {
                                        Toast.makeText(Registar.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else {
                    Toast.makeText(Registar.this, "Este email já está associado a uma conta! Utilize outro email.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

}
