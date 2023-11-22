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

public class MainAdapterEvAll extends FirebaseRecyclerAdapter<Eventos,MainAdapterEvAll.myViewHolder> {

    private String userID,userName;
    private FirebaseUser user;

    public MainAdapterEvAll(@NonNull FirebaseRecyclerOptions<Eventos> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MainAdapterEvAll.myViewHolder holder, int position, @NonNull Eventos model) {
        holder.nome.setText(model.getNome());
        holder.custo.setText(model.getCusto() + "€");
        holder.data.setText(model.getData());
        holder.hora.setText(model.getHora_inicio());
        holder.descricao.setText(model.getDescricao());
        holder.duracao.setText(model.getDuracao() + "h");
        holder.localizacao.setText(model.getLocalizacao());
        String aux = model.getN_inscritos();
        holder.n_max.setText(aux+"/"+model.getN_maximo()+" part");

        Glide.with(holder.img.getContext()).load(model.getImage()).placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop()
                .error(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal).into(holder.img);

        holder.btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.nome.getContext(), MapsActivity4.class);
                intent.putExtra("nome",model.getNome());
                intent.putExtra("localizacao",model.getLocalizacao());
                holder.nome.getContext().startActivity(intent);
            }
        });

        holder.btnDetalhes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.popup_user_all))
                        .setExpanded(true,1700)
                        .create();


                View view = dialogPlus.getHolderView();

                TextView nome = view.findViewById(R.id.txtName);
                TextView custo = view.findViewById(R.id.txtCusto);
                TextView data1 = view.findViewById(R.id.txtData);
                TextView hora = view.findViewById(R.id.txtHora);
                TextView descricao1 = view.findViewById(R.id.txtDesc);
                TextView duracao = view.findViewById(R.id.txtDuracao);
                TextView localizacao = view.findViewById(R.id.txtLoc);

                Button btnInscrever = view.findViewById(R.id.btnInscrever);
                nome.setText(model.getNome());
                custo.setText(model.getCusto());
                data1.setText(model.getData());
                hora.setText(model.getHora_inicio());
                descricao1.setText(model.getDescricao());
                duracao.setText(model.getDuracao()+"h");
                localizacao.setText(model.getLocalizacao());

                dialogPlus.show();

                btnInscrever.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!aux.equals(model.getN_maximo())) {
                            int aux2 = Integer.parseInt(aux);
                            aux2++;
                            String aux3 = String.valueOf(aux2);
                            System.out.println(aux3);
                            Map<String, Object> map = new HashMap<>();
                            map.put("nome", nome.getText().toString());
                            map.put("custo", custo.getText().toString());
                            map.put("data", data1.getText().toString());
                            map.put("hora_inicio", hora.getText().toString());
                            map.put("descricao", descricao1.getText().toString());
                            map.put("duracao", duracao.getText().toString());
                            map.put("localizacao", localizacao.getText().toString());
                            map.put("image", model.getImage());

                            user = FirebaseAuth.getInstance().getCurrentUser();
                            userID = user.getUid();


                            FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Eventos").child(model.getNome()).setValue(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(holder.img.getContext(), "Inscrição feita com sucesso!", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(holder.img.getContext(), "Erro ao fazer inscrição!", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();
                                        }
                                    });
                            String id_criador = model.getId_criador();

                            FirebaseDatabase.getInstance().getReference().child("Users").child(id_criador).child("Eventos").child(model.getNome()).child("n_inscritos").setValue(aux3);

                            FirebaseDatabase.getInstance().getReference().child("Eventos").child(model.getNome()).child("n_inscritos").setValue(aux3);

                            FirebaseDatabase.getInstance().getReference("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User userProfile = snapshot.getValue(User.class);

                                    if (userProfile != null) {
                                        userName = userProfile.nome;
                                    }
                                    System.out.println(userName);
                                    FirebaseDatabase.getInstance().getReference().child("Eventos").child(model.getNome()).child("Inscritos").child(userName).child("nome").setValue(userName);
                                    FirebaseDatabase.getInstance().getReference().child("Eventos").child(model.getNome()).child("Inscritos").child(userName).child("id").setValue(userID);
                                    FirebaseDatabase.getInstance().getReference().child("Eventos").child(model.getNome()).child("Inscritos").child(userName).child("currentLoc").setValue("");

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else {
                            Toast.makeText(holder.img.getContext(), "Evento cheio!", Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();
                        }
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public MainAdapterEvAll.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_user,parent,false);
        return new MainAdapterEvAll.myViewHolder(view);
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
