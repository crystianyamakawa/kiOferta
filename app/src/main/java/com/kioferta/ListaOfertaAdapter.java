package com.kioferta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kioferta.model.Oferta;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class ListaOfertaAdapter extends BaseAdapter {
    Context context;
    List<Oferta> ofertas;

    public ListaOfertaAdapter(Context context, List<Oferta> ofertas) {
        this.context = context;
        this.ofertas = ofertas;
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
        TextView titulo = viewoferta.findViewById(R.id.txTituloOferta);
        TextView descricao = viewoferta.findViewById(R.id.txDescricao);
        ImageView imagem = viewoferta.findViewById(R.id.imagem);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
//        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, options);

        titulo.setText(oferta.getTitulo());
        descricao.setText(oferta.getDescricao());
        String imagemoferta = oferta.getImagem();
        if (imagemoferta!=null) {
            byte[] binario = Base64.decode(imagemoferta, Base64.DEFAULT);
            InputStream inputStream = new ByteArrayInputStream(binario);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imagem.setImageBitmap(bitmap);
        }else {
            imagem.setImageBitmap(null);
        }
            ;
        return viewoferta;

    }
}
