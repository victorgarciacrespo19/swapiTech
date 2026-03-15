package es.upsa.ssbbdd2.trabajo1.application.impl;

import es.upsa.ssbbdd2.trabajo1.application.JsonAdapter;
import es.upsa.ssbbdd2.trabajo1.domain.entities.Homeworld;
import es.upsa.ssbbdd2.trabajo1.domain.entities.Person;
import es.upsa.ssbbdd2.trabajo1.domain.exceptions.StarWarsApiException;
import es.upsa.ssbbdd2.trabajo1.swapi.StarWarsApi;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;
import lombok.Builder;

@Builder(setterPrefix = "with")
public class PersonJsonAdapterImpl implements JsonAdapter<Person>
{
    private PlanetJsonAdapterImpl planetJsonAdapter ;
    private StarWarsApi api;
    @Override
    public Person fromJson(JsonStructure json) throws StarWarsApiException {

        JsonObject jo = json.asJsonObject().getJsonObject("result").getJsonObject("properties");
        Homeworld homeworld;
        try {
            String urlPlanet = jo.getString("homeworld").replace("\"", "").trim();
            JsonObject planetResponse = api.request(urlPlanet);
            homeworld = planetJsonAdapter.fromJson(planetResponse);
        } catch (Exception e) {
            throw new StarWarsApiException(e);
        }


        return Person.builder()
                .withName(jo.getString("name"))
                .withGender(jo.getString("gender"))
                .withUrl(jo.getString("url"))
                .withHomeworld(homeworld)
                .build();
    }

}
