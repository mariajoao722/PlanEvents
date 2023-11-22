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

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapterUser extends FirebaseRecyclerAdapter<Eventos,MainAdapterUser.myViewHolder> {


    public MainAdapterUser(@NonNull FirebaseRecyclerOptions<Eventos> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Eventos model) {
        holder.nome.setText(model.getNome());
        holder.custo.setText(model.getCusto() + "€");
        holder.data.setText(model.getData());
        holder.hora.setText(model.getHora_inicio() + "h");
        holder.descricao.setText(model.getDescricao());
        holder.duracao.setText(model.getDuracao() + "");
        holder.localizacao.setText(model.getLocalizacao());
        holder.n_max.setText("");

        Glide.with(holder.img.getContext()).load(model.getImage()).placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop()
                .error(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal).into(holder.img);

        holder.btnDetalhes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.nome.getContext(), DetalhesEventoUser.class);
                intent.putExtra("nome",model.getNome());
                holder.nome.getContext().startActivity(intent);
            }
        });

        holder.btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.nome.getContext(), MapsActivity4.class);
                intent.putExtra("nome",model.getNome());
                intent.putExtra("localizacao",model.getLocalizacao());
                holder.nome.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_user,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView nome,custo,data,hora,descricao,duracao,localizacao,n_max;

        Button btnDetalhes,btnMapa;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img1);
            nome = itemView.findViewById(R.id.nametext);
            custo = itemView.findViewById(R.id.custotext);
            data = itemView.findViewById(R.id.datatext);
            hora = itemView.findViewById(R.id.horatext);
            descricao = itemView.findViewById(R.id.descricaotext);
            duracao = itemView.findViewById(R.id.duracaotext);
            localizacao = itemView.findViewById(R.id.localizacaotext);
            n_max = itemView.findViewById(R.id.n_maxtext);

            btnDetalhes= itemView.findViewById(R.id.btnDetalhes);
            btnMapa = itemView.findViewById(R.id.btnMapa);

        }
    }
}
