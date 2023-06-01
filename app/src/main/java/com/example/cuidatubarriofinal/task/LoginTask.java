package com.example.cuidatubarriofinal.task;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.example.cuidatubarriofinal.ObtenerIP;
import com.example.cuidatubarriofinal.dto.LoginDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressLint("StaticFieldLeak")
public class LoginTask extends AsyncTask<LoginDTO, Void, Boolean> {

    private static final String URL = ObtenerIP.IP +"/usuarios/login";

    private boolean realizado;

    public LoginTask() {
        realizado = false;
    }

    @Override
    protected Boolean doInBackground(LoginDTO... loginDTOS) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // Crear el objeto Gson
        Gson gson = new GsonBuilder().create();

        String jsonBody = gson.toJson(loginDTOS[0]);

        MediaType mediaType = MediaType.parse("application/json");

        RequestBody requestBody = RequestBody.create(jsonBody, mediaType);

        Request request = new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();

        try {
            // Enviar la solicitud y obtener la respuesta
            Response response = client.newCall(request).execute();

            // Verificar si la solicitud fue exitosa (código de respuesta 200)
            if (response.isSuccessful()) {

                Log.d("Resultado", "Realizado");
                realizado = true;
                return true;

            } else {
                System.out.println("La solicitud no fue exitosa. Código de respuesta: " + response.code());
                realizado = false;
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            realizado = false;
            return false;
        }

    }

    public boolean getRealizado() {
        return realizado;
    }
}
