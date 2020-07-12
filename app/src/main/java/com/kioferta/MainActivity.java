package com.kioferta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.kioferta.model.Oferta;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private DatabaseReference databaseUploads;
    StorageReference mStorageRef;



    public List<Oferta> listaOfertas= new ArrayList<Oferta>();
    public List<Upload> mUploads = new ArrayList<Upload>();
    InterstitialAd mInterstitialAd;
    AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        databaseReference = FirebaseDatabase.getInstance().getReference("ofertas");
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

    public void carregaStories(View view){
        Intent intent = new Intent(this, StoriesActivity.class);

        switch(view.getId())
        {
            case R.id.storyMuffato :
                intent.putExtra("Fornecedor","Muffato");
                break;
            case R.id.storyGuguy :
                intent.putExtra("Fornecedor","Guguy");
                break;
            case R.id.storyCancao :
                intent.putExtra("Fornecedor","Cancao");
                break;
            default :
                // caso aconteca algum erro
                break;
        }

        startActivity(intent);

    }
    public void carregaNovoUsuario(View view){
        Intent intent = new Intent(this, NovoUsuarioActivity.class);
        startActivity(intent);

    }


    public void carregaFornecedor(View view){
        Intent intent = new Intent(this, FornecedorActivity.class);
        startActivity(intent);

    }
    private void carregaOfertas(){
        ListView lstviewOfertas = (ListView) findViewById(R.id.listOfertas);
        ListaOfertaAdapter adapter = new ListaOfertaAdapter(this,listaOfertas );
        lstviewOfertas.setAdapter(adapter);
    }

    public void pesquisaOfertas(View view){


    }

}

