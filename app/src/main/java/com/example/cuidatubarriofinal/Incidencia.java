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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Incidencia extends AppCompatActivity {

    private EditText descripcion;
    private Button botFoto, botIncidencia, botForo;
    private String usuario;

    private Bitmap imagenTomada;
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
        usuario = intent.getStringExtra("usuario");

        botForo.setOnClickListener(v -> {
            nextActivity();
        });

        botFoto.setOnClickListener(v -> {
            checkCameraPermission();
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

        try{
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            imagen.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScan.setData(Uri.fromFile(imageFile));
            sendBroadcast(mediaScan);

            Toast.makeText(this, "imagen guardada", Toast.LENGTH_SHORT);
        }catch (Exception e){

        }
    }

    private void nextActivity() {
        Intent intent = new Intent(this, Foro.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }
}