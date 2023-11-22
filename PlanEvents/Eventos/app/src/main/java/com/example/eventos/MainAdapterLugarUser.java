package com.example.eventos;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapterLugarUser extends FirebaseRecyclerAdapter<Lugar,MainAdapterLugarUser.myViewHolder> {

    private String nomeEv;

    public MainAdapterLugarUser(@NonNull FirebaseRecyclerOptions<Lugar> options, String evento) {
        super(options);
        this.nomeEv = evento;

    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Lugar model) {
        holder.nome.setText(model.getNomel());
        holder.loc.setText(model.getLocalizacao());

        Glide.with(holder.img.getContext()).load(model.getImage()).placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop()
                .error(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal).into(holder.img);

        holder.btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirecionar para pagina de resposta
                Intent intent = new Intent(v.getContext(), ResponderUser.class);
                intent.putExtra("nome",model.getNomel()); // nome lugar
                intent.putExtra("evento",nomeEv); // nome evento

                v.getContext().startActivity(intent);
            }
        });

        holder.btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapsActivity5.class);
                intent.putExtra("nome",model.getNomel());
                intent.putExtra("localizacao",model.getLocalizacao());
                v.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lugar_item_user,parent,false);
        return new myViewHolder(view);
    }



    class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView nome,loc;

        Button btnMapa,btnVer;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (CircleImageView) itemView.findViewById(R.id.img1);
            nome = (TextView) itemView.findViewById(R.id.nametext);
            loc = (TextView) itemView.findViewById(R.id.loctext);

            btnMapa = (Button) itemView.findViewById(R.id.btnMapa);

            btnVer = (Button) itemView.findViewById(R.id.btnDetalhes);
        }
    }
}
