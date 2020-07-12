
package com.kioferta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kioferta.model.Oferta;

import java.util.ArrayList;
import java.util.List;

public class StoriesActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    public List<Oferta> listaOfertas= new ArrayList<Oferta>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        Intent intent = getIntent();
        ImageView logo = (ImageView) findViewById(R.id.logo);


        //Recuperei a string da outra activity
        String fornecedor = intent.getStringExtra("Fornecedor");

        if (fornecedor.equals("Muffato")) {
            logo.setImageDrawable(this.getResources().getDrawable(R.drawable.muffato));
            //setBackground(this.getResources().getDrawable(R.drawable.muffato));
        }else if(fornecedor.equals("Guguy")) {
            logo.setImageDrawable(this.getResources().getDrawable(R.drawable.guguy));
        }else if(fornecedor.equals("Cancao")){
            logo.setImageDrawable(this.getResources().getDrawable(R.drawable.cancao));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Query ofertasFornecedor = databaseReference.child("ofertas").orderByChild("fornecedor").equalTo(fornecedor);
        Log.d("TAG00, ","Retorno: " +ofertasFornecedor.toString());
        ofertasFornecedor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }
    private void carregaOfertas(){
        ListView lstviewOfertas = (ListView) findViewById(R.id.listOfertas);
        ListaOfertaAdapter adapter = new ListaOfertaAdapter(this,listaOfertas);
        lstviewOfertas.setAdapter(adapter);
    }
}
