package com.example.curfewdelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    Marker myMarker;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle("Choose your location");


        locationManager = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(this, LoginActivity.class));
        }



        latLng = new LatLng(7.8731,80.7718);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        Log.e("tag","init");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        Button doneBut = (Button) findViewById(R.id.doneBut);
        doneBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                Log.e("TAG", extras.getString("name"));
                if(extras == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                    builder.setMessage("Something went wrong please restart the app")
                            .setTitle(getString(R.string.app_name))
                            .setNegativeButton("Restart", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(MapsActivity.this, LoginActivity.class));
                                }
                            }).show();
                }else {
                    String name = extras.getString("name");
                    String phone = extras.getString("phone");
                    String lat = String.valueOf(latLng.latitude);
                    String lang = String.valueOf(latLng.longitude);
                    saveInSP(name,phone,lat,lang);

                    pushToDB(name,phone,lat,lang);

                    Intent d = new Intent(MapsActivity.this, CheckListActivity.class);
                    d.putExtra("name",name);
                    d.putExtra("phone",phone);
                    d.putExtra("lat",lat);
                    d.putExtra("lang",lang);
                    startActivity(d);
                }
            }
        });
    }

    private void pushToDB(String name, String phone, String lat, String lang) {
        //TODO
    }

    private void saveInSP(String name, String phone, String lat, String lang) {
        SharedPreferences sharedpreferences = getSharedPreferences("curfewDelivery", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.putString("lat", lat);
        editor.putString("lang", lang);
        editor.apply();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setMyLocationEnabled(true);
        Log.e("tag","map ready");

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {

            }

            @Override
            public void onMarkerDragEnd(Marker arg0) {
                latLng = arg0.getPosition();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
            }

            @Override
            public void onMarkerDrag(Marker arg0) {

            }
        });
    }





    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mMap.clear();

            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate =CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(18.0f).build());

            MarkerOptions options = new MarkerOptions().position(latLng).draggable(true).title("Long press to drag the Marker");

            mMap.getUiSettings().setRotateGesturesEnabled(true);
            mMap.animateCamera(cameraUpdate);
            mMap.moveCamera(cameraUpdate);

            if(myMarker == null){
                myMarker = mMap.addMarker(options);
            }
//            else {
//                myMarker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
//            }
            locationManager.removeUpdates(this);
        }



    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
