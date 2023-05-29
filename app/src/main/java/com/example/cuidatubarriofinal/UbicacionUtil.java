package com.example.cuidatubarriofinal;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class UbicacionUtil {

    private final static int REQUEST_LOCATION_PERMISSION = 123;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitud;
    private double longitud;

    private Activity activity;

    public UbicacionUtil(Activity activity) {
        this.activity = activity;
    }

    public void obtenerUbicacion(Context context) {
        // Verificar los permisos de ubicación
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            solicitarPermisosUbicacion();
            return;
        }

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Definir el listener de ubicación
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitud = location.getLatitude();
                longitud = location.getLongitude();
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
        };

        // Registrar el listener para recibir actualizaciones de ubicación
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void detenerObtencionUbicacion() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void solicitarPermisosUbicacion() {
        // Verificar si los permisos ya han sido otorgados
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Los permisos no han sido otorgados, solicitarlos
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }
}