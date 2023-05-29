package com.example.cuidatubarriofinal.task;

import android.os.AsyncTask;

import com.example.cuidatubarriofinal.dto.ComentarioDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ObtenerComentariosTask extends AsyncTask<Void, Void, List<ComentarioDTO>> {

    private static final String URL = "http://192.168.1.140:8080/comentarios/lista";

    private String usuario, contrasena, dni;
    private boolean realizado;

    public ObtenerComentariosTask() {
        realizado = false;
    }


    @Override
    protected List<ComentarioDTO> doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // Crear el objeto Gson
        Gson gson = new GsonBuilder().create();
        try {
            try {
                Request request = new Request.Builder()
                        .url(URL)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Type listType = new TypeToken<List<ComentarioDTO>>() {
                    }.getType();
                    return gson.fromJson(responseBody, listType);
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;

            }
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
