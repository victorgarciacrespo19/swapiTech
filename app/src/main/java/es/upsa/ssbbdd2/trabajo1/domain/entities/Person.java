package es.upsa.ssbbdd2.trabajo1.domain.entities;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@JsonbPropertyOrder(value = {"name", "gender", "url", "homeworld"})
public class Person
{
    private String name;
    private String gender;
    private String url;
    private Homeworld homeworld;

}
