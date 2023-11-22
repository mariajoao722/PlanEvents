package com.example.eventos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ShowEventos extends AppCompatActivity {

    RecyclerView recyclerView;

    MainAdapter adapter;
    private FirebaseUser user;
    private String userID;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_eventos);
        setTitle("Eventos");

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();


        FirebaseRecyclerOptions<Eventos> options =
                new FirebaseRecyclerOptions.Builder<Eventos>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Eventos"), Eventos.class)
                        .build();

        adapter = new MainAdapter(options);
        recyclerView.setAdapter(adapter);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setColorFilter(getResources().getColor(R.color.white));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), MapsActivity2.class));
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
        FirebaseRecyclerOptions<Eventos> options =
                new FirebaseRecyclerOptions.Builder<Eventos>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Eventos").orderByChild("nome").startAt(str).endAt(str+"~"), Eventos.class)
                        .build();

        adapter = new MainAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}