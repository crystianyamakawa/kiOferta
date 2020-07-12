package com.kioferta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.kioferta.model.Oferta;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public class ListaOfertaAdapter extends BaseAdapter {


    Context context;
    List<Oferta> ofertas;

    StorageReference mStorageRef;

    public ListaOfertaAdapter(Context context, List<Oferta> ofertas) {
        this.context = context;
        this.ofertas = ofertas;
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");


    }

    @Override
    public int getCount() {
        return ofertas.size();
    }

    @Override
    public Object getItem(int position) {
        return ofertas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ofertas.get(position).getId();
    }


    public String getChave(int position) {
        return ofertas.get(position).getChave();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View viewoferta = LayoutInflater.from(context).inflate(R.layout.linha_oferta,parent,false );

        Oferta oferta = ofertas.get(position);
        //Upload uploadCurrent = uploads.get(position); // pegar da lista de uploads
        TextView titulo = (TextView) viewoferta.findViewById(R.id.txTituloOferta);
        TextView descricao = (TextView) viewoferta.findViewById(R.id.txDescricao);
        String chave = oferta.getChave();
        if (chave != null) {
            final ImageView imagem = (ImageView) viewoferta.findViewById(R.id.imagem);
            mStorageRef.child(chave).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                Integer imagem_carregada = 0;

                @Override
                public void onSuccess(Uri uri) {
                    //Uri downloadUri = mUploadTask.getMetadata().getDownloadUrl();
                    String url = uri.toString();
                    if (imagem_carregada == 0) {
                        Picasso.get().load(url)
                                .placeholder(R.mipmap.ic_camera_3)
                                .fit()
                                .centerInside()
                                .into(imagem);
                        imagem_carregada = 1;
                    }
                }
            });
        }
        titulo.setText(oferta.getTitulo());
        descricao.setText(oferta.getDescricao());



        return viewoferta;

    }


}

