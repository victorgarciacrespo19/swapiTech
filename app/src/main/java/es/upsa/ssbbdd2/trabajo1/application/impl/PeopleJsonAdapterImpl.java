package es.upsa.ssbbdd2.trabajo1.application.impl;

import es.upsa.ssbbdd2.trabajo1.application.JsonAdapter;
import es.upsa.ssbbdd2.trabajo1.domain.entities.Character;
import es.upsa.ssbbdd2.trabajo1.domain.entities.Film;
import es.upsa.ssbbdd2.trabajo1.domain.entities.People;
import es.upsa.ssbbdd2.trabajo1.domain.entities.Person;
import es.upsa.ssbbdd2.trabajo1.domain.exceptions.StarWarsApiException;
import es.upsa.ssbbdd2.trabajo1.swapi.StarWarsApi;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import lombok.Builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CompletableFuture;


@Builder (setterPrefix = "with")
public class PeopleJsonAdapterImpl implements JsonAdapter<People>
{
    private PersonJsonAdapterImpl personAdapter;
    private StarWarsApi api;

    @Override
    public People fromJson(JsonStructure json) throws StarWarsApiException {
        List<Person> people = new ArrayList<>();
        String nextPage;

        do {
            JsonObject jo = json.asJsonObject();

            // Página actual
            JsonArray charactersArray = jo.getJsonArray("results");
            List<CompletableFuture<Person>> personFutures = new ArrayList<>();
            System.out.println("Procesando personajes...");
            for (JsonValue characterValue : charactersArray) {
                String characterUrl = characterValue.asJsonObject().getString("url").replace("\"", "").trim();

                // Procesar personajes de forma simultanea
                CompletableFuture<Person> personFuture = CompletableFuture.supplyAsync(() -> {

                    try {
                        JsonObject personResponse = api.request(characterUrl);
                        if (personResponse == null || personResponse.isEmpty()) {
                            throw new StarWarsApiException("La respuesta de api no válida.");
                        }

                        return personAdapter.fromJson(personResponse);
                    }  catch (StarWarsApiException e) {
                        throw new RuntimeException(e);
                    }

                });

                personFutures.add(personFuture);
            }

            // Espera a que todas las solicitudes de la página terminen

            List<Person> peopleProcessed = personFutures.stream()
                                                        .map(CompletableFuture::join)
                                                        .toList();
            people.addAll(peopleProcessed);


            // Obtener la URL de la siguiente página
            JsonValue nextValue = jo.get("next");
            if (nextValue != null && nextValue.getValueType() == JsonValue.ValueType.STRING) {
                nextPage = jo.getString("next");
                System.out.println("Pasando a la siguiente página: " + nextPage);
                try {
                    json = api.request(nextPage);
                } catch (StarWarsApiException e) {
                    throw new StarWarsApiException();
                }
            } else {
                nextPage = null;
            }

        } while (nextPage != null);

        return People.builder()
                .withPeople(people)
                .build();
    }

}
