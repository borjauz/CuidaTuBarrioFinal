package com.example.cuidatubarriofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {

    EditText editDni, editContrasena, editUsuario;
    Button buttonLogin, buttonRegistrarse;
    private Connection connection = null;
    private String dni, contrasena, usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
        }
        editDni = findViewById(R.id.editTextDni);
        editContrasena = findViewById(R.id.editTextContrasena);
        editUsuario = findViewById(R.id.editTextUsuario);

        buttonLogin = findViewById(R.id.buttonAcceder);
        buttonRegistrarse = findViewById(R.id.buttonRegistro);

        getData();

        buttonLogin.setOnClickListener(v -> {
            login();
        });

        buttonRegistrarse.setOnClickListener(v -> {
            registro();
        });
    }

    private void registro() {
        if(checkDni(dni)){
            if (checkData(contrasena, usuario)) {
                if(checkInDataBase(dni, usuario, "")){
                    registerUser(dni, usuario, contrasena);
                }
            }
        }
    }

    private void login() {
        nextActivity();
        /*
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

    //IMPLEMENTAR
    private boolean checkInDataBase(String dni, String usuario, String contrasena){
        return true;
    }

    private void registerUser(String dni, String usuario, String contrasena){

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