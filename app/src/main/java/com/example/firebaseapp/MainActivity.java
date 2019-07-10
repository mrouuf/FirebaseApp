package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebaseapp.model.Karyawan;
import com.example.firebaseapp.model.KaryawanAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements KaryawanAdapter.FirebaseDataListener {
    KaryawanAdapter karyawanAdapter;
    private Button btnInput;
    private EditText inputNama, inputDivisi, inputGaji;
    private String valueNama, valueDivisi, valueGaji;

    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Karyawan> karyawanList = new ArrayList<>();

    Context context;

    private DatabaseReference database;
    private ArrayList<Karyawan> daftarKaryawan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        btnInput = (Button) findViewById(R.id.buttonInput);
        inputNama = (EditText) findViewById(R.id.namaKaryawan);
        inputDivisi = (EditText) findViewById(R.id.divisiKaryawan);
        inputGaji = (EditText) findViewById(R.id.gajiKaryawan);

        recyclerView = (RecyclerView) findViewById(R.id.itemListKaryawan);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance().getReference();

        database.child("karyawan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getBaseContext(),    "status onDataChange", Toast.LENGTH_LONG).show();
                daftarKaryawan = new ArrayList<>();

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()){
                    Karyawan karyawan = noteDataSnapshot.getValue(Karyawan.class);
                    karyawan.setKey(noteDataSnapshot.getKey());
                    daftarKaryawan.add(karyawan);
                }

                adapter = new KaryawanAdapter(MainActivity.this, daftarKaryawan);
                recyclerView.setAdapter(adapter);//coba ganti ini kalau error
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "status onCancelled", Toast.LENGTH_LONG).show();
            }
        });

        final Karyawan karyawan = (Karyawan) getIntent().getSerializableExtra("data");
        if (karyawan != null){
            inputNama.setText(karyawan.getNama_karyawan());
            inputDivisi.setText(karyawan.getDivisi_karyawan());
            inputGaji.setText(karyawan.getGaji_karyawan());
            btnInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    karyawan.setNama_karyawan(inputNama.getText().toString());
                    karyawan.setDivisi_karyawan(inputDivisi.getText().toString());
                    karyawan.setGaji_karyawan(Integer.parseInt(String.valueOf(inputGaji.getText())));

                    updateKaryawan(karyawan);
                }
            });
        } else {
            btnInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validation();
                }
            });
        }

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onDelete(Karyawan karyawan, int position) {
        if (database != null){
            database.child("karyawan").child(karyawan.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this,
                                    "Employee succsessfully deleted",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }

    private void validation(){
        valueNama = inputNama.getText().toString();
        valueDivisi = inputDivisi.getText().toString();
        valueGaji = inputGaji.getText().toString();

        boolean isEmptyText = false;

        if (TextUtils.isEmpty(valueNama)){
            isEmptyText = true;
            inputNama.setError(getString(R.string.blank));
        }
        if (TextUtils.isEmpty(valueDivisi)){
            isEmptyText = true;
            inputDivisi.setError(getString(R.string.blank));
        }
        if (TextUtils.isEmpty(valueGaji)){
            isEmptyText = true;
            inputGaji.setError(getString(R.string.blank));
        }
        if (!isEmptyText){
            daftarkanKaryawan();
            Toast.makeText(getBaseContext(), "Daftar karyawan berhasil", Toast.LENGTH_SHORT).show();
        }
    }

    private void daftarkanKaryawan() {
        submitKaryawan(new Karyawan(inputNama.getText().toString(),
                inputDivisi.getText().toString(),
                Integer.parseInt(inputGaji.getText().toString())));
        Toast.makeText(this, "Berhasil mendaftarkan karyawan, yeay !", Toast.LENGTH_SHORT).show();

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputNama.getWindowToken(), 0);
    }

    private void submitKaryawan(Karyawan karyawan) {
        database.child("karyawan").push()
                .setValue(karyawan).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                inputNama.setText("");
                inputDivisi.setText("");
                inputGaji.setText("");
                Snackbar.make(findViewById(R.id.buttonInput),"Employee successfully registered",Snackbar.LENGTH_LONG);
            }
        });
    }

    private void updateKaryawan(Karyawan karyawan){
        database.child("karyawan")
                .child(karyawan.getKey())
                .setValue(karyawan)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(findViewById(R.id.buttonInput), "Employee successfully updated !", Snackbar.LENGTH_LONG)
                                .setAction("see", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finish();
                                    }
                                }).show();

                    }
                });
    }

    public static Intent getActIntent(Activity activity){
        return new Intent(activity, MainActivity.class);
    }


}
