package com.example.cuidatubarriofinal;

import android.Manifest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cuidatubarriofinal.dto.IncidenciaDTO;
import com.example.cuidatubarriofinal.task.IncidenciaTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class Incidencia extends AppCompatActivity {

    private EditText descripcion;
    private Button botFoto, botIncidencia, botForo;
    private String dni;
    private Bitmap imagenTomada;
    byte[] imagenFinal;

    private double latitud, longitud;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSION_CAMERA = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencia);

        descripcion = findViewById(R.id.editTextDescripcion);

        botForo = findViewById(R.id.buttonForo);
        botFoto = findViewById(R.id.buttonFoto);
        botIncidencia = findViewById(R.id.buttonIncidencia);

        Intent intent = getIntent();
        dni = intent.getStringExtra("dni");


        botForo.setOnClickListener(v -> {
            nextActivity();
        });

        botFoto.setOnClickListener(v -> {
            checkCameraPermission();
        });

        botIncidencia.setOnClickListener(v -> {
            registrarIncidencia();
        });
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            abrirCamara();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_PERMISSION_CAMERA);
            checkCameraPermission();
        }
    }

    private void abrirCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imagenTomada = (Bitmap) extras.get("data");
            galleryAddPic(imagenTomada);
        }
    }

    private void galleryAddPic(Bitmap imagen) {
        File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "IMG_" + timeStamp + ".jpg";
        File imageFile = new File(storage, fileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            imagen.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScan.setData(Uri.fromFile(imageFile));
            sendBroadcast(mediaScan);

            Toast.makeText(this, "imagen guardada", Toast.LENGTH_SHORT);
        } catch (Exception e) {

        }
    }

    public void bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        imagenFinal = stream.toByteArray();
    }

    private void registrarIncidencia() {
        UbicacionUtil ubicacion = new UbicacionUtil(this);
        ubicacion.obtenerUbicacion(this);
        LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "no hay permisos", Toast.LENGTH_SHORT).show();
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(locationManager!=null){
            latitud = location.getLatitude();
            longitud =  location.getLongitude();
        }

        Log.d("longitud", String.valueOf(longitud));
        Log.d("latitud", String.valueOf(latitud));
        bitmapToByteArray(imagenTomada);
        IncidenciaDTO incidenciaDTO = new IncidenciaDTO(dni, descripcion.getText().toString(), latitud, longitud, imagenFinal);
        IncidenciaTask incidenciaTask = new IncidenciaTask();
        try {
            Boolean result = incidenciaTask.execute(incidenciaDTO).get();
            if(!result){
                Toast.makeText(this, "Error, incidencia no registrada", Toast.LENGTH_SHORT).show();
            }else{
                descripcion.setText("");
                Toast.makeText(this, "Incidencia registrada", Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    private void nextActivity() {
        Intent intent = new Intent(this, Foro.class);
        intent.putExtra("dni", dni);
        startActivity(intent);
    }
}