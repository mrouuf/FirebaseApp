package com.example.firebaseapp.model;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseapp.MainActivity;
import com.example.firebaseapp.R;

import java.util.ArrayList;
import java.util.List;

public class KaryawanAdapter extends RecyclerView.Adapter<KaryawanAdapter.KaryawanHolder> {
    private Context context;
    private ArrayList<Karyawan> list;
    FirebaseDataListener listener;

    public KaryawanAdapter(Context context, ArrayList<Karyawan> list) {
        this.context = context;
        this.list = list;
        this.listener = (MainActivity) context;
    }

    @NonNull
    @Override
    public KaryawanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlist_karyawan, parent, false);
        return new KaryawanAdapter.KaryawanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KaryawanHolder holder, final int position) {
        final Karyawan karyawan = list.get(position);
        holder.setListkaryawan(karyawan);

        holder.linearLayoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.linearLayoutCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Toast.makeText(context, "Clicked long", Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.setTitle("Select");
                dialog.show();

                Button editButton = (Button) dialog.findViewById(R.id.buttonEdit);
                Button deleteButton = (Button) dialog.findViewById(R.id.buttonDelete);

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        context.startActivity(MainActivity.getActIntent((Activity)
                        context).putExtra("data", list.get(position)));
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        listener.onDelete(list.get(position), position);
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public interface FirebaseDataListener{
        void onDelete(Karyawan karyawan, int position);
    }

    public class KaryawanHolder extends RecyclerView.ViewHolder {
        public Karyawan listkaryawan;
        public LinearLayout linearLayoutCard;
        private TextView namaK, divisiK, gajiK;

        public KaryawanHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutCard = itemView.findViewById(R.id.layout_card_karyawan);
            namaK = itemView.findViewById(R.id.idNama);
            divisiK = itemView.findViewById(R.id.idDivisi);
            gajiK = itemView.findViewById(R.id.idGaji);
        }

        public void setListkaryawan(Karyawan listmodel) {
            this.listkaryawan = listmodel;

            this.namaK.setText(listmodel.getNama_karyawan());
            this.divisiK.setText(listmodel.getDivisi_karyawan());
            this.gajiK.setText(String.valueOf(listmodel.getGaji_karyawan()));
        }
    }
}
