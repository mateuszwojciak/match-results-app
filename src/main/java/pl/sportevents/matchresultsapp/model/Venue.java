package pl.sportevents.matchresultsapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Venue {
    private String id;
    private String name;
    private int capacity;
    private String cityName;
    private String countryName;
    private String mapCoordinates;
    private String countryCode;

}
