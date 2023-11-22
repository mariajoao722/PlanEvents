package com.example.eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetalhesEventoUser extends AppCompatActivity implements View.OnClickListener{
    TextView nomeEvento, dataEvento, horaEvento, localizacaoEvento, descricaoEvento,custoEvento,duracaoEvento,teste;
    Button btnComeçar,btnSubmeter;

    private String newString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_evento_user);
        // Hide the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle extras = getIntent().getExtras();
        newString = null;
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

        btnComeçar = findViewById(R.id.btnComecar);
        btnComeçar.setOnClickListener(this);

        btnSubmeter = findViewById(R.id.btnSubmeter);
        btnSubmeter.setOnClickListener(this);

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
                    dataEvento.setText(data);
                    horaEvento.setText(hora+"h");
                    localizacaoEvento.setText(localizacao);
                    descricaoEvento.setText(descricao);
                    custoEvento.setText(custo+"€");
                    duracaoEvento.setText(duracao+"h");
                    String timeString = hora;
                    String[] parts = timeString.split(":");
                    int hora3 = Integer.parseInt(parts[0]);
                    int minuto = Integer.parseInt(parts[1]);
                    int duracao2 = Integer.parseInt(duracao);
                    System.out.println("hora3: "+hora3);
                    System.out.println(Integer.parseInt(getCurrentTimeH()));
                    System.out.println("minuto: "+minuto);
                    System.out.println(Integer.parseInt(getCurrentTimeM()));
                    if(data.equals(getCurrentDate())){
                        if (hora.equals(getCurrentTime()) || (hora3 == Integer.parseInt(getCurrentTimeH()) && minuto > Integer.parseInt(getCurrentTimeM()))
                                || (  hora3 < Integer.parseInt(getCurrentTimeH()) && hora3+duracao2 >= Integer.parseInt(getCurrentTimeH()) && minuto >= Integer.parseInt(getCurrentTimeM()))){
                            btnComeçar.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetalhesEventoUser.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
            }
        });

        //teste = findViewById(R.id.textView);
       // teste.setText("Data: "+getCurrentDate()+"\nHora: "+ getCurrentTimeH() + getCurrentTimeM());

    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

    private String getCurrentTime() {
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm", Locale.getDefault());
        String strTime = mdformat.format(calendar);
        return strTime;
    }

    private String getCurrentTimeH() {
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat mdformat = new SimpleDateFormat("kk", Locale.getDefault());
        String strTime = mdformat.format(calendar);
        //String strTime = String.valueOf(calendar);
        return strTime;
    }

    private String getCurrentTimeM() {
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat mdformat = new SimpleDateFormat("mm", Locale.getDefault());
        String strTime = mdformat.format(calendar);
        return strTime;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnComecar:
                Intent serviceIntent = new Intent(this, LocationService.class);
                serviceIntent.putExtra("nome", nomeEvento.getText().toString());
                startService(serviceIntent);

                btnComeçar.setVisibility(View.INVISIBLE);

                Intent intent = new Intent(DetalhesEventoUser.this, ShowLugaresUser.class);
                intent.putExtra("nome", nomeEvento.getText().toString());
                startActivity(intent);
                break;
            case R.id.btnSubmeter:
                // Stop the LocationService
                stopService( new Intent(this, LocationService.class));
                finish();
                break;
        }
    }
}