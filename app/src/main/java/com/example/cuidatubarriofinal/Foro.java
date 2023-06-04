package com.example.cuidatubarriofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cuidatubarriofinal.dto.ComentarioDTO;
import com.example.cuidatubarriofinal.dto.PublicarDTO;
import com.example.cuidatubarriofinal.task.ComentarioTask;
import com.example.cuidatubarriofinal.task.ObtenerComentariosTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Foro extends AppCompatActivity {

    Button botonPublicar;
    EditText editTextComentario;
    String comentario;
    String dni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foro);

        botonPublicar=findViewById(R.id.buttonSubmit);
        editTextComentario = findViewById(R.id.editTextDescription);

        Intent intent = getIntent();
        dni = intent.getStringExtra("dni");

        List<ComentarioDTO> listaComentarios;
        try {
            listaComentarios = obtenerListaComentarios();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(listaComentarios!=null){
            ListView listView = findViewById(R.id.listViewDebates);
            ListViewAdapter adapter = new ListViewAdapter(this, listaComentarios);
            listView.setAdapter(adapter);
        }


        botonPublicar.setOnClickListener(v -> {
            comentario = editTextComentario.getText().toString();
            if (comentario != null && !comentario.isEmpty()) {
                try {
                    publicar();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(this, "Escriba su comentario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void publicar() throws ExecutionException, InterruptedException {
        PublicarDTO publicarDTO = new PublicarDTO(dni, comentario);
        ComentarioTask comentarioTask = new ComentarioTask();
        comentarioTask.execute(publicarDTO).get();
        List<ComentarioDTO> listaComentarios;
        try {
            listaComentarios = obtenerListaComentarios();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(listaComentarios!=null){
            ListView listView = findViewById(R.id.listViewDebates);
            ListViewAdapter adapter = new ListViewAdapter(this, listaComentarios);
            listView.setAdapter(adapter);
            editTextComentario.setText("");
        }
    }

    private List<ComentarioDTO> obtenerListaComentarios() throws ExecutionException, InterruptedException {
        ObtenerComentariosTask obtenerComentariosTask = new ObtenerComentariosTask();
        List<ComentarioDTO> listaComentarios = obtenerComentariosTask.execute().get();
        for(ComentarioDTO comentario :listaComentarios){
            Log.d("comentario", comentario.toString());
        }
        return listaComentarios;
    }

}