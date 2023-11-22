package com.example.eventos;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<Eventos,MainAdapter.myViewHolder> {

    private String userID;
    private FirebaseUser user;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String formattedHora, formattedMin;
    int hora2, minuto2;
    public MainAdapter(@NonNull FirebaseRecyclerOptions<Eventos> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Eventos model) {
        holder.nome.setText(model.getNome());
        holder.custo.setText(model.getCusto() + "€");
        holder.data.setText(model.getData());
        holder.hora.setText(model.getHora_inicio() + "h");
        holder.descricao.setText(model.getDescricao());
        holder.duracao.setText(model.getDuracao() + "h");
        holder.localizacao.setText(model.getLocalizacao());
        String aux = model.getN_inscritos();
        holder.n_inscritos.setText(model.getN_inscritos());
        holder.n_maximo.setText(aux+"/"+model.getN_maximo()+" inscritos");


        Glide.with(holder.img.getContext()).load(model.getImage()).placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark).circleCrop()
                .error(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal).into(holder.img);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1900)
                        .create();


                View view = dialogPlus.getHolderView();

                TextView nome = view.findViewById(R.id.txtName);
                EditText custo = view.findViewById(R.id.txtCusto);
                TextView data1 = view.findViewById(R.id.txtData);
                TextView hora = view.findViewById(R.id.txtHora);
                EditText descricao1 = view.findViewById(R.id.txtDesc);
                EditText duracao = view.findViewById(R.id.txtDuracao);
                TextView localizacao = view.findViewById(R.id.txtLoc);
                EditText n_maximo = view.findViewById(R.id.txtParticipantes);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);
                nome.setText(model.getNome());
                custo.setText(model.getCusto());
                data1.setText(model.getData());
                hora.setText(model.getHora_inicio());
                descricao1.setText(model.getDescricao());
                duracao.setText(model.getDuracao());
                localizacao.setText(model.getLocalizacao());
                n_maximo.setText(model.getN_maximo());

                data1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int ano = cal.get(Calendar.YEAR);
                        int mes = cal.get(Calendar.MONTH);
                        int dia = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                holder.nome.getContext(),
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                mDateSetListener,
                                ano,mes,dia);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.show();
                    }
                });
                mDateSetListener = new DatePickerDialog.OnDateSetListener() { // Calendário
                    @Override
                    public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                        mes = mes + 1;
                        String formattedMes = "";
                        String formattedDia = "";
                        if (mes < 10){
                            DecimalFormat decimalFormat = new DecimalFormat("00");
                            formattedMes = decimalFormat.format(mes);
                        }else {
                            formattedMes = String.valueOf(mes);
                        }

                        if (dia < 10) {
                            DecimalFormat decimalFormat = new DecimalFormat("00");
                            formattedDia = decimalFormat.format(dia);
                        }
                        else {
                            formattedDia = String.valueOf(dia);
                        }

                        String data = formattedDia + "/" + formattedMes + "/" + ano;
                        data1.setText(data);
                    }
                };

                hora.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(android.widget.TimePicker timePicker, int shora, int sminuto) {
                                int hora1 = shora;
                                int minuto = sminuto;

                                formattedMin = "";
                                formattedHora= "";

                                if (minuto < 10) {
                                    DecimalFormat decimalFormat = new DecimalFormat("00");
                                    formattedMin = decimalFormat.format(minuto);
                                }
                                else {
                                    formattedMin = String.valueOf(minuto);
                                }

                                if (hora1 < 10) {
                                    DecimalFormat decimalFormat = new DecimalFormat("00");
                                    formattedHora = decimalFormat.format(hora1);
                                }
                                else {
                                    formattedHora = String.valueOf(hora1);
                                }

                                hora2 = Integer.parseInt(formattedHora);
                                minuto2 = Integer.parseInt(formattedMin);


                                String hora_inicio = formattedHora + ":" + formattedMin;
                                hora.setText(hora_inicio);
                            }
                        };
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                holder.nome.getContext(),
                                mTimeSetListener,
                                hora2,minuto2,true);
                        timePickerDialog.show();
                    }
                });


                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("nome",nome.getText().toString());
                        map.put("custo",custo.getText().toString());
                        map.put("data",data1.getText().toString());
                        map.put("hora_inicio",hora.getText().toString());
                        map.put("descricao",descricao1.getText().toString());
                        map.put("duracao",duracao.getText().toString());
                        map.put("localizacao",localizacao.getText().toString());
                        map.put("n_maximo",n_maximo.getText().toString());

                        user = FirebaseAuth.getInstance().getCurrentUser();
                        userID = user.getUid();

                        FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Eventos").child(getRef(position).getKey())
                                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.nome.getContext(), "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.nome.getContext(), "Erro ao atualizar dados!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();

                                    }
                                });
                        FirebaseDatabase.getInstance().getReference().child("Eventos").child(getRef(position).getKey()).updateChildren(map);
                    }
                });

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.nome.getContext());
                builder.setTitle("Tem a certeza que pretende eliminar este evento?");
                builder.setMessage("Esta ação não pode ser desfeita!");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        userID = user.getUid();
                        FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Eventos").child(getRef(position).getKey()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Eventos").child(getRef(position).getKey()).removeValue();
                        Toast.makeText(holder.nome.getContext(), "Evento eliminado com sucesso!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });


                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.nome.getContext(), "Evento não eliminado!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                builder.show();

            }
        });

        holder.btnDetalhes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.nome.getContext(), DetalhesEvento.class);
                intent.putExtra("nome",model.getNome());
                holder.nome.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView nome,custo,data,hora,descricao,duracao,localizacao,n_inscritos,n_maximo;

        Button btnEdit, btnDelete,btnDetalhes;

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
            n_inscritos = itemView.findViewById(R.id.n_inscritostext);
            n_maximo = itemView.findViewById(R.id.n_maxtext);

            btnEdit = itemView.findViewById(R.id.btnEditar);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDetalhes = itemView.findViewById(R.id.btnDetalhes);
        }
    }
}
