package pl.sportevents.matchresultsapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Competitor {
    private String id;
    private String name;
    private String countryCode;
    private String abbreviation;
    private String qualifier;
    private String gender;

}
