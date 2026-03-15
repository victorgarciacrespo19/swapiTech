package es.upsa.ssbbdd2.trabajo1.application.impl;

import es.upsa.ssbbdd2.trabajo1.application.JsonAdapter;
import es.upsa.ssbbdd2.trabajo1.domain.entities.Homeworld;
import es.upsa.ssbbdd2.trabajo1.domain.exceptions.StarWarsApiException;
import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;

public class PlanetJsonAdapterImpl implements JsonAdapter<Homeworld>
{
    @Override
    public Homeworld fromJson(JsonStructure json) {
        JsonObject jo = json.asJsonObject().getJsonObject("result").getJsonObject("properties");
        return Homeworld.builder()
                        .withName(jo.getString("name"))
                        .withUrl(jo.getString("url"))
                        .build();
    }
}
