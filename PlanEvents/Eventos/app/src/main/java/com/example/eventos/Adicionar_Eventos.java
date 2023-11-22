package com.example.eventos;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.DecimalFormat;
import java.util.Calendar;

public class Adicionar_Eventos extends AppCompatActivity implements View.OnClickListener{
    private EditText ETnome,ETcusto,ETduracao,ETdescricao,ETinscritos;
    private TextView TVdata,TVhora_inicio,TVlocalizacao;
    private Button adicionar,voltar;
    private ImageView uploadimage;
    private Uri imageUri;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private DatabaseReference reference2;
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private boolean bools = false;

    String formattedHora, formattedMin;
    int hora1, minuto1;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private String loc;



    @Override
    protected void onCreate(Bundle savedInstanceState) { // Só aparece para administradores
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_eventos);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ETnome = findViewById(R.id.Nome);
        TVlocalizacao = findViewById(R.id.localizacao);

        ETcusto = findViewById(R.id.custo);
        TVhora_inicio = findViewById(R.id.hora_inicio);
        ETduracao = findViewById(R.id.duracao);
        TVdata = findViewById(R.id.data);
        ETdescricao = findViewById(R.id.descricao);
        ETinscritos = findViewById(R.id.n_inscritos);

        uploadimage = findViewById(R.id.uploadimage);

        adicionar = findViewById(R.id.adicionar_ev);
        adicionar.setOnClickListener(this);

        voltar = findViewById(R.id.voltar_atras);
        voltar.setOnClickListener(this);

        TVdata.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        loc = null;
        if (extras != null) {
            loc = extras.getString("origem"); // localização do evento
        }

        TVlocalizacao.setText(loc);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() { // Calendário
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                mes = mes + 1;
                String formattedMes = "" ;
                String formattedDia = "" ;

                if (mes < 10) {
                    DecimalFormat decimalFormat = new DecimalFormat("00");
                    formattedMes = decimalFormat.format(mes);
                }
                else {
                    formattedMes = String.valueOf(mes);
                }

                if (dia < 10) {
                    DecimalFormat decimalFormat = new DecimalFormat("00");
                    formattedDia = decimalFormat.format(dia);
                }
                else {
                    formattedDia = String.valueOf(dia);
                }

                String data = formattedDia + "/" + formattedMes + "/" + ano;
                TVdata.setText(data);
            }
        };

        TVhora_inicio.setOnClickListener(this);

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
                            Toast.makeText(Adicionar_Eventos.this, "Sem Imagem selecionada", Toast.LENGTH_SHORT).show();
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

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference2 = FirebaseDatabase.getInstance().getReference("Eventos");
        userID = user.getUid();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.adicionar_ev:
                if (imageUri != null) {
                    adicionar(imageUri);
                }
                else {
                    Toast.makeText(Adicionar_Eventos.this, "Selicione uma imagem", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.data:
                Calendar cal = Calendar.getInstance();
                int ano = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Adicionar_Eventos.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        ano,mes,dia);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
                break;

            case R.id.hora_inicio:
                TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker timePicker, int shora, int sminuto) {
                        int hora = shora;
                        int minuto = sminuto;

                        formattedMin = "";
                        formattedHora= "";

                        if (minuto < 10) {
                            DecimalFormat decimalFormat = new DecimalFormat("00");
                            formattedMin = decimalFormat.format(minuto);
                        }
                        else {
                            formattedMin = String.valueOf(minuto);
                        }

                        if (hora < 10) {
                            DecimalFormat decimalFormat = new DecimalFormat("00");
                            formattedHora = decimalFormat.format(hora);
                        }
                        else {
                            formattedHora = String.valueOf(hora);
                        }

                        hora1 = Integer.parseInt(formattedHora);
                        minuto1 = Integer.parseInt(formattedMin);

                        String hora_inicio = formattedHora + ":" + formattedMin;
                        TVhora_inicio.setText(hora_inicio);
                    }
                };


               TimePickerDialog timePickerDialog = new TimePickerDialog(
                        Adicionar_Eventos.this,
                        mTimeSetListener,
                        hora1,minuto1,true);
                timePickerDialog.show();
                break;

            case R.id.voltar_atras:
                finish();
                break;
        }
    }

    private void adicionar(Uri imageUri) {
        String localizacao = TVlocalizacao.getText().toString().trim();
        String custo = ETcusto.getText().toString().trim();
        String nome = ETnome.getText().toString().trim();
        String hora_inicio = TVhora_inicio.getText().toString().trim();
        String duracao = ETduracao.getText().toString().trim();
        String data = TVdata.getText().toString().trim();
        String descricao = ETdescricao.getText().toString().trim();
        String inscritos = ETinscritos.getText().toString().trim();

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


        if (custo.isEmpty()) {
            ETcusto.setError("Email é necessário");
            ETcusto.requestFocus();
            return;
        }

        if (hora_inicio.isEmpty()) {
            TVhora_inicio.setError("Password é necessária");
            TVhora_inicio.requestFocus();
            return;
        }

        if (duracao.isEmpty()) {
            ETduracao.setError("Duração é necessária");
            ETduracao.requestFocus();
            return;
        }

        if (data.isEmpty()) {
            TVdata.setError("Data é necessária");
            TVdata.requestFocus();
            return;
        }

        if (descricao.isEmpty()) {
            ETdescricao.setError("Desceição é necessária");
            ETdescricao.requestFocus();
            return;
        }

        if (Integer.parseInt(duracao) > 24) {
            ETduracao.setError("Máximo de 24 horas");
            ETduracao.requestFocus();
            return;
        }

        if (inscritos.isEmpty()) {
            ETinscritos.setError("Número de inscritos é necessário");
            ETinscritos.requestFocus();
            return;
        }

        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Eventos evento = new Eventos(nome, localizacao, custo, hora_inicio, duracao, data,descricao,imageUri.toString(),"0",inscritos,userID);
                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds : snapshot.getChildren()){
                                    if(ds.getKey().equals(nome)){
                                        bools = true;
                                    }
                                }
                                if(bools){
                                    Toast.makeText(Adicionar_Eventos.this, "This name is already in use", Toast.LENGTH_SHORT).show();
                                    bools = false;
                                }else{
                                    reference2.child(nome).setValue(evento);
                                    reference.child(userID).child("Eventos").child(nome).setValue(evento);
                                    Toast.makeText(Adicionar_Eventos.this, "Added", Toast.LENGTH_SHORT).show();
                                    finish();
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