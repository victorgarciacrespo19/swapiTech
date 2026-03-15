package es.upsa.ssbbdd2.trabajo1.application;

import es.upsa.ssbbdd2.trabajo1.domain.exceptions.StarWarsApiException;
import jakarta.json.JsonArray;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;

import java.util.ArrayList;
import java.util.List;

public interface JsonAdapter<T>
{
    T fromJson(JsonStructure json) throws StarWarsApiException;
    default List<T> fromJson(JsonArray json) throws StarWarsApiException {
        List<T> list = new ArrayList<>();
        for (JsonValue itemJV : json)
        {
            JsonStructure itemJS = (JsonStructure) itemJV;
            T item = fromJson(itemJS);
            list.add(item);
        }
        return list;
    }


}
