package es.upsa.ssbbdd2.trabajo1;

import es.upsa.ssbbdd2.trabajo1.application.impl.*;
import es.upsa.ssbbdd2.trabajo1.domain.entities.Film;
import es.upsa.ssbbdd2.trabajo1.domain.entities.People;
import es.upsa.ssbbdd2.trabajo1.domain.exceptions.StarWarsApiException;
import es.upsa.ssbbdd2.trabajo1.swapi.StarWarsApi;
import es.upsa.ssbbdd2.trabajo1.swapi.impl.StarWarsApiImpl;
import jakarta.json.JsonObject;
import java.io.File;

import java.util.List;


public class Main
{
    public static void main(String[] args) throws Exception
    {
        try ( StarWarsApi swapi = new StarWarsApiImpl() )
        {
           generateFilms(swapi);
           generatePeople(swapi);
        }
    }

    static void generateFilms(StarWarsApi swapi) throws StarWarsApiException
    {
        FilmJsonAdapterImpl filmAdapter = FilmJsonAdapterImpl.builder()
                                                             .withCharacterAdapter(new CharacterJsonAdapterImpl())
                                                             .withApi(swapi)
                                                             .build();
        JsonObject filmsJson = swapi.request(StarWarsApi.FILMS_URL);
        List<Film> films = filmAdapter.fromJson(filmsJson);

        for (Film film : films)
        {
            System.out.println(film);
        }

        // generar json de peliculas con jsonb
        JsonBImpl jsonWriter = new JsonBImpl();
        File file = new File("films.json");
        jsonWriter.generarJson(films, file);
        System.out.println("\nFichero films.json generado correctamente\n");

    }

    static void generatePeople(StarWarsApi swapi) throws StarWarsApiException
    {
        PeopleJsonAdapterImpl peopleAdapter= PeopleJsonAdapterImpl.builder()
                                                                  .withPersonAdapter(PersonJsonAdapterImpl.builder()
                                                                                                          .withPlanetJsonAdapter(new PlanetJsonAdapterImpl())
                                                                                                          .withApi(swapi)
                                                                                                          .build())
                                                                  .withApi(swapi)
                                                                  .build();
        JsonObject peopleJson = swapi.request( StarWarsApi.PEOPLE_URL );
        People people= peopleAdapter.fromJson(peopleJson);

        System.out.println(people);

        JsonBImpl jsonWriter = new JsonBImpl();
        File file = new File("people.json");
        jsonWriter.generarJson(people, file);
        System.out.println("\n\nFichero people.json generado correctamente\n");
    }

}
