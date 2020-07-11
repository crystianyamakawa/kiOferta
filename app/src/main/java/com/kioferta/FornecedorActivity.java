package com.kioferta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kioferta.model.Oferta;

import java.util.ArrayList;
import java.util.List;

public class FornecedorActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    public List<Oferta> listaOfertas= new ArrayList<Oferta>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fornecedor);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaOfertas.clear();
                Integer i = 0;
                Long c = Long.valueOf(0);
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    Oferta offer = new Oferta();
                    String chave = (String) messageSnapshot.child("chave").getValue();
                    String fornecedor = (String) messageSnapshot.child("fornecedor").getValue();
                    String setor = (String) messageSnapshot.child("setor").getValue();
                    String titulo = (String) messageSnapshot.child("titulo").getValue();
                    String descricao = (String) messageSnapshot.child("descricao").getValue();
                    i= i+1;
                    offer.setId(Long.valueOf(i));
                    offer.setChave(chave);
                    offer.setFornecedor(fornecedor);
                    offer.setSetor(setor);
                    offer.setTitulo(titulo);
                    offer.setDescricao(descricao);

                    listaOfertas.add(offer);

                    String name = (String) messageSnapshot.child("name").getValue();
                    String message = (String) messageSnapshot.child("message").getValue();
                }
                carregaOfertas();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
    private void carregaOfertas(){
        ListView lstviewOfertas = (ListView) findViewById(R.id.listOfertas);
        ListaOfertaAdapter adapter = new ListaOfertaAdapter(this,listaOfertas );
        lstviewOfertas.setAdapter(adapter);
    }
    public void carregaNovaOferta(View view){
        Intent intent = new Intent(this, NovaOfertaActivity.class);
        startActivity(intent);

    }
    }