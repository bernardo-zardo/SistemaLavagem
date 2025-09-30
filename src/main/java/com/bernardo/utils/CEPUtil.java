package com.bernardo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 *
 * @author Bernardo Zardo Mergen
 */
public class CEPUtil {

    public static LocalizacaoJson buscarLocalizacao(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            LocalizacaoJson localizacao = gson.fromJson(response.body(), LocalizacaoJson.class);
            return localizacao;
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
