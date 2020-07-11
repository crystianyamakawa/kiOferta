package com.kioferta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kioferta.model.Oferta;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NovaOfertaActivity extends AppCompatActivity {

    private String currentPhotoPath;
    private static final int REQUEST_TAKE_PHOTO = 105;
    private static final int PICK_IMAGE = 200;
    //private int foto=0;

    private Oferta novaOferta = new Oferta();
    ImageView imageView1 ;
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;
    Uri  mImageUri;
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_oferta);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference("ofertas");
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    public void chamarCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.kioferta",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void chamarDialogoArquivo(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 105:{
                if(resultCode==RESULT_OK ){
                    try{
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 3;
                        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, options);

                        //Converter o bitmap em Base64 (string), que é útil para mandar a foto para um WS.
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                        byte[] binario = outputStream.toByteArray();
                        String fotoString = Base64.encodeToString(binario, Base64.DEFAULT);

                        Log.i("NovaOferta",""+fotoString.length());
                        imageView1 = (ImageView)findViewById(R.id.imgOferta);
                        imageView1.setImageBitmap(bitmap);
                        //imageView1.setBackground(null);
                        //novaOferta.setImagem(fotoString);



                    }catch (Exception i){
                        Log.e("NovaOferta",i.toString());
                    }
                }
                break;
            }
            case 200:{
                if(resultCode==RESULT_OK ){
                    try{
                        Log.e("NovaOferta", String.valueOf(data));
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 3;
                        Uri imagemSelecionada = data.getData();
                        mImageUri = imagemSelecionada;
                        InputStream input = getContentResolver().openInputStream(imagemSelecionada);
                        final Bitmap bitmap = BitmapFactory.decodeStream(input);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                        byte[] binario = outputStream.toByteArray();
                        String fotoString = Base64.encodeToString(binario, Base64.DEFAULT);
                        imageView1 = (ImageView)findViewById(R.id.imgOferta);
                        imageView1.setImageBitmap(bitmap);
                        novaOferta.setImagem(fotoString);


                        //Log.e("NovaOferta", "content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F113009/ORIGINAL/NONE/image%2Fjpeg/988067427");
                        //Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(imagemSelecionada));
                        //Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 1080, 1000, true);
                        //imageView1.setImageBitmap(bitmap);
                        //imageView1.setScaleType(ImageView.ScaleType.FIT_XY);


                       // Log.i("NovaOferta",""+ fotoString.length());

                       // imageView1.setImageBitmap(bitmap);
                    }catch (Exception i){
                        Log.e("NovaOferta",i.toString());
                    }
                }
                break;
            }
        }
    }

    public void salvarOferta(View view) {
        EditText editFornecedor = (EditText) findViewById(R.id.txFornecedor);
        EditText editSetor = (EditText) findViewById(R.id.txSetor);
        EditText editTitulo = (EditText) findViewById(R.id.txTituloOferta);
        EditText editDescricao = (EditText) findViewById(R.id.txDescricaoOferta);

        novaOferta.setFornecedor(editFornecedor.getText().toString());
        novaOferta.setSetor(editSetor.getText().toString());

        novaOferta.setTitulo(editTitulo.getText().toString());
        novaOferta.setDescricao(editDescricao.getText().toString());
        //memoriaAtividade.save();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String chave = databaseReference.child(editFornecedor.getText().toString()).child(editSetor.getText().toString()).child(novaOferta.getTitulo().toString()).push().getKey();
        novaOferta.setChave(chave);
        databaseReference.child(novaOferta.getChave().toString()).setValue(novaOferta);

        carregaArquivoStorage();

        Toast.makeText(this, "A sua oferta foi armazenada com sucesso!!", Toast.LENGTH_LONG).show();
        finish();
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void carregaArquivoStorage(){
        if (mImageUri !=null) {
            StorageReference fileReference = mStorageRef.child(novaOferta.getChave()
             + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(0);
                        }
                    }, 5000);
                    Toast.makeText(NovaOfertaActivity.this,"Upload realizado com sucesso",Toast.LENGTH_SHORT).show();
                    Upload upload = new Upload(novaOferta.getChave(),mStorageRef.getDownloadUrl().toString());
                    String uploadid = databaseReference.push().getKey();
                    databaseReference.child(uploadid).setValue(upload);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                   // Toast.makeText(this, exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
             })
             .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress =(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount() );
                        mProgressBar.setProgress((int) progress);
                        }
                    });

        }else {
            Toast.makeText(this,"Arquivo nao selecionado", Toast.LENGTH_SHORT).show();
        }
    }
}

