package com.example.eventos;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainAdapterPessoas extends FirebaseRecyclerAdapter<Inscritos,MainAdapterPessoas.myViewHolder> {
    private String nomeEv;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    public MainAdapterPessoas(@NonNull FirebaseRecyclerOptions<Inscritos> options,String evento) {
        super(options);
        this.nomeEv = evento;
    }

    @NonNull
    @Override
    public MainAdapterPessoas.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inscritos_item,parent,false);
        return new MainAdapterPessoas.myViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Inscritos model) {
        holder.nome.setText(model.getNome());
        holder.id.setText(model.getid());


        holder.btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference("Users");
                userID = user.getUid();
                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);

                        if (userProfile != null) {
                            int monitores = userProfile.is_monitor;
                        }
                        if (userProfile.is_monitor == 0) {
                            Intent intent = new Intent(v.getContext(), DetalheInscritos.class);
                            intent.putExtra("nome",model.getNome());
                            intent.putExtra("evento",nomeEv);
                            intent.putExtra("id",model.getid());
                            v.getContext().startActivity(intent);
                        } else {
                            Intent intent = new Intent(v.getContext(), DetalheInscritosMonitor.class);
                            intent.putExtra("nome",model.getNome());
                            intent.putExtra("evento",nomeEv);
                            intent.putExtra("id",model.getid());
                            v.getContext().startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(v.getContext(), "Algo deu errado!", Toast.LENGTH_LONG).show();
                    }
                });



            }
        });
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView nome,id;
        Button btnVer;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.nametext);
            id = (TextView) itemView.findViewById(R.id.idtext);
            btnVer = (Button) itemView.findViewById(R.id.btnDetalhes);
        }
    }
}
