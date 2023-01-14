package pl.sportevents.matchresultsapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    @JsonProperty("start_date")
    private String eventStartDate;

    @JsonProperty("competition_nam")
    private String competitionName;

    @JsonIgnore
    private String homeTeamName;

    @JsonIgnore
    private String homeTeamCountry;

    @JsonIgnore
    private String awayTeamName;

    @JsonIgnore
    private String awayTeamCountry;

    @JsonProperty("team_details")
    private String teamDetails;

    @JsonProperty("venue")
    private String venueName;

    @JsonIgnore
    private String highestProbableResultName;

    @JsonIgnore
    private double highestProbableValue;

    @JsonProperty("highest_probable_result")
    private String highestProbableResult;

}
