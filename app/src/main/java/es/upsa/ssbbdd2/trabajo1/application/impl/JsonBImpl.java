package es.upsa.ssbbdd2.trabajo1.application.impl;

import es.upsa.ssbbdd2.trabajo1.application.JsonbWriter;
import es.upsa.ssbbdd2.trabajo1.domain.exceptions.StarWarsApiException;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonBImpl implements JsonbWriter
{

    @Override
    public void generarJson(Object data, File file) throws StarWarsApiException
    {
        Jsonb jsonb = createJsonb();
        try (FileWriter fw = new FileWriter(file))
        {
            jsonb.toJson(data, fw);
        } catch (IOException e) {
            throw new StarWarsApiException();
        }

    }
    private Jsonb createJsonb()
    {
        JsonbConfig config = new JsonbConfig();
        config.withFormatting(true);
        return JsonbBuilder.newBuilder()
                           .withConfig( config )
                           .build();
    }
}
