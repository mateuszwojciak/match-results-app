package pl.sportevents.matchresultsapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Venue {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("capacity")
    private int capacity;

    @JsonProperty("city_name")
    private String cityName;

    @JsonProperty("country_name")
    private String countryName;

    @JsonProperty("map_coordinates")
    private String mapCoordinates;

    @JsonProperty("country_code")
    private String countryCode;

}
