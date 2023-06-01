package com.example.cuidatubarriofinal.task;

import android.os.AsyncTask;

import com.example.cuidatubarriofinal.ObtenerIP;
import com.example.cuidatubarriofinal.dto.IncidenciaDTO;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IncidenciaTask extends AsyncTask<IncidenciaDTO, Void, Boolean> {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String URL_API = ObtenerIP.IP +"/incidencias/nueva"; // URL de la API

    @Override
    protected Boolean doInBackground(IncidenciaDTO... incidencias) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        boolean exito = false;

        try {
            // Convertir IncidenciaDTO a JSON
            String json = gson.toJson(incidencias[0]);

            // Crear el cuerpo de la solicitud HTTP
            RequestBody body = RequestBody.create(json, JSON);

            // Crear la solicitud HTTP POST
            Request request = new Request.Builder()
                    .url(URL_API)
                    .post(body)
                    .build();

            // Enviar la solicitud HTTP
            Response response = client.newCall(request).execute();

            // Verificar el código de respuesta
            if (response.isSuccessful()) {
                exito = true;
            }

            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return exito;
    }

}