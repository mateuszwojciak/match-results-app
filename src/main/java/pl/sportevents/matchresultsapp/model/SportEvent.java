package pl.sportevents.matchresultsapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SportEvent {
    private String sportEventId;
    private String startDate;
    private String sportName;
    private String competitionName;
    private String competitionId;
    private String seasonName;
    private Competitor[] competitors;
    private Venue venue;
    private double probabilityHomeTeamWinner;
    private double probabilityDraw;
    private double probabilityAwayTeamWinner;

}
