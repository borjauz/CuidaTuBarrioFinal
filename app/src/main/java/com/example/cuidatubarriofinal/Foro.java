package com.example.cuidatubarriofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.cuidatubarriofinal.dto.ComentarioDTO;
import com.example.cuidatubarriofinal.task.ComentarioTask;
import com.example.cuidatubarriofinal.task.ObtenerComentariosTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Foro extends AppCompatActivity {

    Button botonPublicar;
    EditText editTextComentario;
    String comentario;
    String usuario, dni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foro);

        botonPublicar=findViewById(R.id.buttonSubmit);

        editTextComentario = findViewById(R.id.editTextDescription);
        Intent intent = getIntent();
        usuario = intent.getStringExtra("usuario");
        dni = intent.getStringExtra("dni");

        try {
            mostrarComentarios();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
            try {
                publicar();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void publicar() throws ExecutionException, InterruptedException {
        ComentarioDTO comentarioDTO = new ComentarioDTO(usuario, dni, comentario);
        ComentarioTask comentarioTask = new ComentarioTask();
        comentarioTask.execute(comentarioDTO).get();
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
    }

    private void mostrarComentarios() throws ExecutionException, InterruptedException {
        ComentarioDTO comentarioDTO = new ComentarioDTO();
        ComentarioTask comentarioTask = new ComentarioTask();
        comentarioTask.execute(comentarioDTO).get();

    }

    private List<ComentarioDTO> obtenerListaComentarios() throws ExecutionException, InterruptedException {
        ObtenerComentariosTask obtenerComentariosTask = new ObtenerComentariosTask();
        List<ComentarioDTO> listaComentarios = obtenerComentariosTask.execute().get();
        return listaComentarios;
    }

}