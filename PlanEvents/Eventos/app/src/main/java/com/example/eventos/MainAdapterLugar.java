package com.example.eventos;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.Holder;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapterLugar extends FirebaseRecyclerAdapter<Lugar,MainAdapterLugar.myViewHolder> {

    private String nomeEv;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    public MainAdapterLugar(@NonNull FirebaseRecyclerOptions<Lugar> options,String evento) {
        super(options);
        this.nomeEv = evento;

    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Lugar model) {
        holder.nome.setText(model.getNomel());
        holder.descricao.setText(model.getDescricao());
        holder.localizacao.setText(model.getLocalizacao());
        holder.pontos.setText(model.getPontos());
        holder.penalizacao.setText(model.getPenalizacao());
        holder.obrigatorio.setText(model.getObrigatorio());

        Glide.with(holder.img.getContext()).load(model.getImage()).placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop()
                .error(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal).into(holder.img);


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
                    holder.btnEditar.setVisibility(View.GONE);
                    holder.btnEliminar.setVisibility(View.GONE);
                } else {
                    holder.btnEditar.setVisibility(View.VISIBLE);
                    holder.btnEliminar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditarLugar.class);
                intent.putExtra("nome",model.getNomel());
                intent.putExtra("evento",nomeEv);
                v.getContext().startActivity(intent);
            }
        });

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.nome.getContext());
                builder.setTitle("Tem a certeza que pretende eliminar este lugar?");
                builder.setMessage("Esta ação não pode ser desfeita!");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Eventos").child(nomeEv).child("Lugares").child(getRef(position).getKey()).removeValue();
                        Toast.makeText(holder.nome.getContext(), "Lugar eliminado com sucesso!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });


                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.nome.getContext(), "Lugar não eliminado!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                builder.show();

            }
        });

        holder.btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetalhesLugar.class);
                intent.putExtra("nome",model.getNomel());
                intent.putExtra("evento",nomeEv);
                v.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lugar_item,parent,false);
        return new myViewHolder(view);

    }
    class myViewHolder extends RecyclerView.ViewHolder{

            CircleImageView img;
            TextView nome,descricao,localizacao,pontos,penalizacao,obrigatorio;

            Button btnEditar,btnEliminar,btnVer;

            public myViewHolder(@NonNull View itemView) {
                super(itemView);

                img = (CircleImageView) itemView.findViewById(R.id.img1);
                nome = (TextView) itemView.findViewById(R.id.nametext);
                descricao = (TextView) itemView.findViewById(R.id.descricaotext);
                localizacao = (TextView) itemView.findViewById(R.id.localizacaotext);
                pontos = (TextView) itemView.findViewById(R.id.pontostext);
                penalizacao = (TextView) itemView.findViewById(R.id.penalizacaotext);
                obrigatorio = (TextView) itemView.findViewById(R.id.obrigatoriotext);

                btnEditar = (Button) itemView.findViewById(R.id.btnEditar);
                btnEliminar = (Button) itemView.findViewById(R.id.btnDelete);
                btnVer = (Button) itemView.findViewById(R.id.btnDetalhes);
            }
    }
}
