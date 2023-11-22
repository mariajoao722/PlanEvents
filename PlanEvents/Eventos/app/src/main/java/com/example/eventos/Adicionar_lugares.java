package com.example.eventos;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Adicionar_lugares extends AppCompatActivity implements View.OnClickListener{


    private EditText ETnome,ETdesricao,ETtarefa,ETpenalizacao,ETpontos;
    private TextView TVlocalizacao;
    private CheckBox CBobrigatorio;
    private Button adicionar,voltar;
    private ImageView uploadimage;
    private Uri imageUri;

    private DatabaseReference reference;
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private boolean bools = false;

    private String newString,loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_lugares);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ETnome = findViewById(R.id.Nome);
        TVlocalizacao = findViewById(R.id.localizacao);
        ETdesricao = findViewById(R.id.descricao);
        ETtarefa = findViewById(R.id.tarefa);
        ETpenalizacao = findViewById(R.id.penalizacao);
        ETpontos = findViewById(R.id.pontos);
        CBobrigatorio = findViewById(R.id.obrigatorio);

        uploadimage = findViewById(R.id.uploadimage);

        adicionar = findViewById(R.id.adicionar_lg);
        adicionar.setOnClickListener(this);

        voltar = findViewById(R.id.voltar_atras);
        voltar.setOnClickListener(this);

        reference = FirebaseDatabase.getInstance().getReference("Eventos");

        Bundle extras = getIntent().getExtras();
        newString = null;
        loc = null;
        if (extras != null) {
            newString = extras.getString("nome"); // nome evento
            loc = extras.getString("origem"); // localizacao lugar
        }

        TVlocalizacao.setText(loc);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imageUri = data.getData();
                            uploadimage.setImageURI(imageUri);
                        }else {
                            Toast.makeText(Adicionar_lugares.this, "Sem Imagem selecionada", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK); // Intent para escolher imagem
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.adicionar_lg:
                if (imageUri != null) {
                    adicionar(imageUri);
                }
                else {
                    Toast.makeText(Adicionar_lugares.this, "Selicione uma imagem", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.voltar_atras:
                finish();
                break;
        }
    }

    private void adicionar( Uri imageUri) {
        //falta imagem
        String localizacao = TVlocalizacao.getText().toString().trim();
        String descricao = ETdesricao.getText().toString().trim();
        String nome = ETnome.getText().toString().trim();
        String tarefa = ETtarefa.getText().toString().trim();
        String penalizacao = ETpenalizacao.getText().toString().trim();
        String pontos = ETpontos.getText().toString().trim();
        String obrigatorio = "";

        if (CBobrigatorio.isChecked()) {
            obrigatorio = "1";
        }else{
            obrigatorio = "0";
        }

        if (nome.isEmpty()) {
            ETnome.setError("Nome é necessário");
            ETnome.requestFocus();
            return;
        }

        if (localizacao.isEmpty()) {
            TVlocalizacao.setError("Localização é necessária");
            TVlocalizacao.requestFocus();
            return;
        }


        if (descricao.isEmpty()) {
            ETdesricao.setError("Email é necessário");
            ETdesricao.requestFocus();
            return;
        }

        if (tarefa.isEmpty()) {
            ETtarefa.setError("Password é necessária");
            ETtarefa.requestFocus();
            return;
        }

        if (penalizacao.isEmpty()) {
            ETpenalizacao.setError("Penalização é necessária");
            ETpenalizacao.requestFocus();
            return;
        }

        if (pontos.isEmpty()) {
            ETpontos.setError("Pontos são necessários");
            ETpontos.requestFocus();
            return;
        }


        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        String finalObrigatorio = obrigatorio;
        imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Lugar lugar = new Lugar( nome,localizacao, descricao, tarefa, penalizacao, pontos, finalObrigatorio,imageUri.toString());
                        reference.child(newString).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds : snapshot.getChildren()){
                                    if(ds.getKey().equals(nome)){
                                        bools = true;
                                    }
                                    if(bools){
                                        Toast.makeText(Adicionar_lugares.this, "This name is already in use", Toast.LENGTH_SHORT).show();
                                        bools = false;
                                    }else{
                                        reference.child(newString).child("Lugares").child(nome).setValue(lugar);
                                        Toast.makeText(Adicionar_lugares.this, "Added", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        });



    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }
}