package com.example.eventos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ShowEventosUser extends AppCompatActivity {

    RecyclerView recyclerView;

    MainAdapterUser adapter;
    private FirebaseUser user;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_eventos_user);

        recyclerView = findViewById(R.id.rv1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        FirebaseRecyclerOptions<Eventos> options =
                new FirebaseRecyclerOptions.Builder<Eventos>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Eventos"), Eventos.class)
                        .build();

        adapter = new MainAdapterUser(options);
        recyclerView.setAdapter(adapter);
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

        adapter = new MainAdapterUser(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}