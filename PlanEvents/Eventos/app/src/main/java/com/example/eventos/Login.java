package com.example.eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextemail, editTextpassword;
    private Button logIn;
    private TextView registar, passEsquecida;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        editTextemail = findViewById(R.id.email);
        editTextpassword = findViewById(R.id.password);

        progressBar = findViewById(R.id.progressBar);

        logIn = findViewById(R.id.logIn);
        logIn.setOnClickListener(this);

        registar = findViewById(R.id.signUp);
        registar.setOnClickListener(this);

        passEsquecida = findViewById(R.id.forgotpassword);
        passEsquecida.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp:
                startActivity(new Intent(this, Registar.class));
                finish();
                break;
            case R.id.logIn:
                loginUser();
                break;


                case R.id.forgotpassword:
                startActivity(new Intent(this, ResetPassword.class));
                finish();
                break;
        }
    }

    private void loginUser() {
        String email = editTextemail.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();

        if (password.isEmpty()) {
            editTextpassword.setError("Password é necessária!");
            editTextpassword.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextemail.setError("Email é necessário!");
            editTextemail.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ((task.isSuccessful())){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {
                        //redirect to user profile
                        String uid = user.getUid();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        database.getReference().child("Users").child(uid).child("is_admin").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int isAdmin = snapshot.getValue(Integer.class);
                                if (isAdmin == 1) {
                                    //Toast.makeText(Login.this, "Welcome Admin!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Login.this, MenuAdmin.class));
                                    finish();
                                }else {
                                    database.getReference().child("Users").child(uid).child("is_monitor").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int isMonitor = snapshot.getValue(Integer.class);
                                            if (isMonitor == 1) {
                                                //Toast.makeText(Login.this, "Welcome Monitor!", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(Login.this, MenuMonitor.class));
                                                finish();
                                            }else {
                                                //Toast.makeText(Login.this, "Welcome User!", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(Login.this, MenuUser.class));
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(Login.this, "Failed to get Monitor value!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Login.this, "Failed to get Admin value!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Verifique o seu email para verificar a conta!", Toast.LENGTH_LONG).show();
                    }

            }else {
                    limpar();
                    Toast.makeText(Login.this, "Falha no login! Verifique as suas credenciais", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void limpar(){
        editTextpassword.setText("");
        editTextemail.requestFocus();
    }
}