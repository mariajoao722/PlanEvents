package com.example.eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowLugares extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    RecyclerView recyclerView;

    MainAdapterLugar adapter;

    private String newString;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lugares);
        setTitle("Lugares");

        recyclerView = findViewById(R.id.rv3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Bundle extras = getIntent().getExtras();
        newString = null;
        if (extras != null) {
            newString = extras.getString("nome"); // nome evento
        }

        FirebaseRecyclerOptions<Lugar> options =
                new FirebaseRecyclerOptions.Builder<Lugar>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Eventos").child(newString).child("Lugares"), Lugar.class)
                        .build();

        adapter = new MainAdapterLugar(options,newString);
        recyclerView.setAdapter(adapter);

        floatingActionButton = findViewById(R.id.floatingActionButton1);
        floatingActionButton.setColorFilter(getResources().getColor(R.color.white));

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                int monitor = -1;
                if (userProfile != null) {
                    monitor = userProfile.is_monitor;
                }
                if (monitor == 1) {
                    floatingActionButton.setVisibility(View.GONE);
                } else {
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowLugares.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowLugares.this, MapsActivity3.class);
                intent.putExtra("nome",newString);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView =(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str){
        FirebaseRecyclerOptions<Lugar> options =
                new FirebaseRecyclerOptions.Builder<Lugar>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Eventos").child(newString).child("Lugares").orderByChild("nome").startAt(str).endAt(str+"~"), Lugar.class)
                        .build();

        adapter = new MainAdapterLugar(options,newString);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}