package es.upsa.ssbbdd2.trabajo1.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Character {
    private String name;
    private String height;
    private String url;
}
