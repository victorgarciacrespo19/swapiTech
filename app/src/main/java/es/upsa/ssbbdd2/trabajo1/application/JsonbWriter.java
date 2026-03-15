package es.upsa.ssbbdd2.trabajo1.application;

import es.upsa.ssbbdd2.trabajo1.domain.exceptions.StarWarsApiException;

import java.io.File;
import java.io.IOException;

public interface JsonbWriter
{
    public void generarJson(Object data, File file) throws StarWarsApiException;
}
