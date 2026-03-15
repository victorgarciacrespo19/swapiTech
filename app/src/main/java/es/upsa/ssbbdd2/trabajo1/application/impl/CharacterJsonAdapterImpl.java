package es.upsa.ssbbdd2.trabajo1.application.impl;

import es.upsa.ssbbdd2.trabajo1.application.JsonAdapter;
import es.upsa.ssbbdd2.trabajo1.domain.entities.Character;
import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;

public class CharacterJsonAdapterImpl implements JsonAdapter<Character> {

    @Override
    public Character fromJson(JsonStructure json) {
        JsonObject jo = json.asJsonObject().getJsonObject("result").getJsonObject("properties");
        return Character.builder()
                        .name(jo.getString("name"))
                        .height(jo.getString("height"))
                        .url(jo.getString("url"))
                        .build();
    }
}
