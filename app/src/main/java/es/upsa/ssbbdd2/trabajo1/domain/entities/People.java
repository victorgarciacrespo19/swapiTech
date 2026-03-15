package es.upsa.ssbbdd2.trabajo1.domain.entities;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.*;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor

public class People
{
    @Singular(value = "person")
    private List<Person> people;
}
