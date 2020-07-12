package com.kioferta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kioferta.model.Oferta;

import java.util.ArrayList;
import java.util.List;

public class FornecedorActivity<MapController> extends AppCompatActivity implements OnMapReadyCallback {
    private DatabaseReference databaseReference;
    public List<Oferta> listaOfertas= new ArrayList<Oferta>();
    private MapView mapView;
    //private MapController mapController;
    private GoogleMap gmap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    Double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fornecedor);
        mapView = findViewById(R.id.mapView);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        Query ofertasFornecedor = databaseReference.child("ofertas").orderByChild("fornecedor").equalTo("Muffato");
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
    public void carregaNovaOferta(View view){
        Intent intent = new Intent(this, NovaOfertaActivity.class);
        startActivity(intent);

    }

    private void pedirPermissoesGPS() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else
            ativaServicoGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ativaServicoGPS();
                } else {
                    Toast.makeText(this, "Não vai funcionar!!!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    public void ativaServicoGPS(){
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    Location minhaLocalizacao = location;
                    //gmap.po
                    latitude = minhaLocalizacao.getLatitude();
                    longitude = minhaLocalizacao.getLongitude();
                    LatLng ny = new LatLng(latitude, longitude);
                    gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(ny);
                    gmap.addMarker(markerOptions);
                    Log.d("GSP","Position " +location.toString());
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.d("GSP","Status " +status);
                }

                public void onProviderEnabled(String provider) { }

                public void onProviderDisabled(String provider) { }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }catch(SecurityException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void atualizarGps(View view){
            pedirPermissoesGPS();

    }

    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        latitude = -23.090995;
        longitude = -52.474570;
        LatLng ny = new LatLng(latitude, longitude);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
        gmap.addMarker(markerOptions);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}