package com.example.cuidatubarriofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuidatubarriofinal.dto.RegistroDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText editDni, editContrasena, editUsuario;
    Button buttonLogin, buttonRegistrarse;
    private Connection connection = null;
    private String dni, contrasena, usuario;

    private static final int PERMISSION_REQUEST_INTERNET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editDni = findViewById(R.id.editTextDni);
        editContrasena = findViewById(R.id.editTextContrasena);
        editUsuario = findViewById(R.id.editTextUsuario);

        buttonLogin = findViewById(R.id.buttonAcceder);
        buttonRegistrarse = findViewById(R.id.buttonRegistro);

        getData();

        // Verificar si ya se tiene el permiso
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.INTERNET},
                    PERMISSION_REQUEST_INTERNET);
        }

        buttonLogin.setOnClickListener(v -> {
            login();
        });

        buttonRegistrarse.setOnClickListener(v -> {
            try {
                registro();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void registro() throws ExecutionException, InterruptedException {
        getData();
        if(checkDni(dni)){
            if (checkData(contrasena, usuario)) {
                registerUser(dni, usuario, contrasena);
            }
        }
    }

    private void login() {
        nextActivity();
        /*
        getData();
        if(checkData(contrasena, usuario)){
            if(checkInDataBase("", usuario, contrasena)){
                nextActivity();
            }
        }else{
            Toast.makeText(this, "usuario o contraseña invalidos", Toast.LENGTH_SHORT).show();
        }
        */
    }

    private boolean checkData(String contrasena, String usuario) {
        if(contrasena != null && usuario != null && usuario!="") {
            if(contrasena.length()>5) {
                return true;
            }else{
                Toast.makeText(this, "la contraseña debe tener mas de 5 caracteres", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(this, "Usuario no creado, compruebe los datos", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private  boolean checkDni(String dni){
        if(dni != null && dni!="") {
            if (dni.matches("\\d{8}[A-Za-z]")) {
                return true;
            } else {
                Toast.makeText(this, "Formato de dni inválido", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(this, "Necesitas un dni para registrarte", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void registerUser(String dni, String usuario, String contrasena) throws ExecutionException, InterruptedException {
        RegistroTask registroTask = new RegistroTask();
        RegistroDTO registroDTO = new RegistroDTO(dni, contrasena, usuario);
        boolean realizado = registroTask.execute(registroDTO).get();
        if (realizado){
            nextActivity();
        } else {
            Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
        }
    }

    private void nextActivity(){
        Intent intent = new Intent(this, Incidencia.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    };

    private void getData() {
        dni = editDni.getText().toString().trim();
        contrasena = editContrasena.getText().toString().trim();
        usuario = editUsuario.getText().toString().trim();
    }

}

