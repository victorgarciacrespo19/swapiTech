package es.upsa.ssbbdd2.trabajo1.application.impl;

import es.upsa.ssbbdd2.trabajo1.application.JsonAdapter;
import es.upsa.ssbbdd2.trabajo1.domain.entities.Character;
import es.upsa.ssbbdd2.trabajo1.domain.entities.Film;

import es.upsa.ssbbdd2.trabajo1.domain.exceptions.StarWarsApiException;
import es.upsa.ssbbdd2.trabajo1.swapi.StarWarsApi;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Builder(setterPrefix = "with")
public class FilmJsonAdapterImpl implements JsonAdapter<List<Film>> {
    private CharacterJsonAdapterImpl characterAdapter;
    private StarWarsApi api;

    @Override
    public List<Film> fromJson(JsonStructure json) throws StarWarsApiException {
        JsonObject filmsJson = json.asJsonObject();
        JsonArray results = filmsJson.getJsonArray("result");
        List<Film> films = new ArrayList<>();

        System.out.println("Iniciando generación de películas...");
        for (JsonValue result : results) {

            // procesar cada película
            JsonObject jo = result.asJsonObject().getJsonObject("properties");


            // lista de personajes
            JsonArray charactersArray = jo.getJsonArray("characters");
            List<CompletableFuture<Character>> characterFutures = new ArrayList<>();

            System.out.println("Procesando pelicula: "+ jo.getString("title"));
            System.out.println("Procesando personajes....");

            for (JsonValue characterUrlValue : charactersArray) {
                String characterUrl = characterUrlValue.toString().replace("\"", "").trim();
                // Hacer las solicitudes de forma simultanea
                CompletableFuture<Character> characterFuture = CompletableFuture.supplyAsync(() -> {
                    try
                    {
                        JsonObject characterResponse = api.request(characterUrl);
                        if (characterResponse == null || characterResponse.isEmpty())
                        {
                            throw new StarWarsApiException("Respuesta no valida para URL: " + characterUrl);
                        }
                        return characterAdapter.fromJson(characterResponse);
                    } catch (Exception e) {
                        throw new RuntimeException(new StarWarsApiException(e));

                    }
                });

                characterFutures.add(characterFuture);

            }
            // Esperar a que todas las solicitudes de personajes terminen

            List<Character> characters;
            try {
                characters = characterFutures.stream()
                                             .map(CompletableFuture::join)
                                             .toList();
            } catch (Exception e) {
                throw new StarWarsApiException(e);
            }

            // Construir la película
            Film film = Film.builder()
                            .withTitle(jo.getString("title"))
                            .withEpisode(jo.getInt("episode_id"))
                            .withUrl(jo.getString("url"))
                            .withCharacters(characters)
                            .build();

            films.add(film);


        }

        System.out.println("Películas generadas correctamente.");
        return films;
    }
}