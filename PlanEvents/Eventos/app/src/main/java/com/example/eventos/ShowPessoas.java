package com.example.eventos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class ShowPessoas extends AppCompatActivity {

    RecyclerView recyclerView;

    MainAdapterPessoas adapter;

    private String newString;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pessoas);
        setTitle("Inscritos");

        recyclerView = findViewById(R.id.rv4);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle extras = getIntent().getExtras();
        newString = null;
        if (extras != null) {
            newString = extras.getString("nome"); // nome evento
        }

        FirebaseRecyclerOptions<Inscritos> options =
                new FirebaseRecyclerOptions.Builder<Inscritos>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Eventos").child(newString).child("Inscritos"), Inscritos.class)
                        .build();

        adapter = new MainAdapterPessoas(options,newString);
        recyclerView.setAdapter(adapter);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setColorFilter(getResources().getColor(R.color.white));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity9.class);
                intent.putExtra("nome", newString);
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
        FirebaseRecyclerOptions<Inscritos> options =
                new FirebaseRecyclerOptions.Builder<Inscritos>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Eventos").child(newString).child("Inscritos").orderByChild("nome").startAt(str).endAt(str+"~"), Inscritos.class)
                        .build();

        adapter = new MainAdapterPessoas(options,newString);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}