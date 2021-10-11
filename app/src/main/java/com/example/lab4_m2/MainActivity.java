package com.example.lab4_m2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    public void startListening() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void updateLocationInfo(Location location) {

        Log.i("Location Info",location.toString());

        TextView latTextView = (TextView) findViewById(R.id.textView3);
        TextView lonTextView = (TextView) findViewById(R.id.textView4);
        TextView accTextView = (TextView) findViewById(R.id.textView);
        TextView altTextView = (TextView) findViewById(R.id.textView5);
        latTextView.setText("Latitude: " + location.getLatitude());
        lonTextView.setText("Longitude: " + location.getLongitude());
        accTextView.setText("Accuracy: " + location.getAccuracy());
        altTextView.setText("Altitude: " + location.getAltitude());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            String address = "Could not find adress";
            List<Address> listAdresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);

            if (listAdresses != null && listAdresses.size() > 0) {

                Log.i("PlaceInfo",listAdresses.get(0).toString());
                address = "Address: \n";
                if (listAdresses.get(0).getSubThoroughfare() != null){
                    address += listAdresses.get(0).getSubThoroughfare() + " ";
                }
                if (listAdresses.get(0).getThoroughfare() != null){
                    address += listAdresses.get(0).getThoroughfare() + "\n";
                }
                if (listAdresses.get(0).getLocality() != null){
                    address += listAdresses.get(0).getLocality() + "\n";
                }
                if (listAdresses.get(0).getPostalCode() != null){
                    address += listAdresses.get(0).getPostalCode() + "\n";
                }
                if (listAdresses.get(0).getCountryName() != null) {
                    address += listAdresses.get(0).getCountryName() + "\n";
                }
            }

            TextView addressTextView = (TextView) findViewById(R.id.textView6);
            addressTextView.setText(address);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }
            @Override
            public void onStatusChanged(String s,int i, Bundle bundle){

            }
            @Override
            public void onProviderEnabled(String s){

            }
            @Override
            public void onProviderDisabled(String s){

            }
        };

        if (Build.VERSION.SDK_INT < 23){
            startListening();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null){
                    updateLocationInfo(location);
                }
            }
        }
    }
}