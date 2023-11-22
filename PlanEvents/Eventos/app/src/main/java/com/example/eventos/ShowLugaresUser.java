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

public class ShowLugaresUser extends AppCompatActivity {

    RecyclerView recyclerView;

    MainAdapterLugarUser adapter;

    private String newString;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lugares_user);
        setTitle("Lugares");

        recyclerView = findViewById(R.id.rv1);
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

        adapter = new MainAdapterLugarUser(options,newString);
        recyclerView.setAdapter(adapter);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setColorFilter(getResources().getColor(R.color.white));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowLugaresUser.this, MapsActivity7.class);
                intent.putExtra("nome", newString); // nome evento
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

        adapter = new MainAdapterLugarUser(options,newString);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}