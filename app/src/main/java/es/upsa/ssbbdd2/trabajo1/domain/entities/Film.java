package es.upsa.ssbbdd2.trabajo1.domain.entities;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@JsonbPropertyOrder(value = {"title", "episode", "url", "characters"})

public class Film
{
    private String title;
    private int episode;
    private String url;
    List <Character> characters;

}
