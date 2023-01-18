package pl.sportevents.matchresultsapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sportevents.matchresultsapp.model.*;
import pl.sportevents.matchresultsapp.service.JsonReader;
import pl.sportevents.matchresultsapp.service.SportEventService;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SportEventServiceTests {

    @Autowired
    private SportEventService sportEventService;

    private EventWrapperList eventWrapperList;
    private List<SportEvent> sportEventList;
    private List<Competitor> competitorList;
    private Venue venue;
    private List<EventResponse> eventResponseList;

    @BeforeEach
    public void setUp() {
        venue = new Venue("sr:venue:8329", "Elbasan Arena", 12500, "Elbasan", "Albania", "41.115875,20.091992", "ALB");
        competitorList = Arrays.asList(
                new Competitor("sr:competitor:37863", "SS Folgore Falciano Calcio", "San Marino", "SMR", "FFC", "home", "male"),
                new Competitor("sr:competitor:277829", "FC Prishtina", "Kosovo", "KOS", "PRI", "away", "male"),
                new Competitor("sr:competitor:5187", "HB Torshavn", "Faroe Islands", "FRO", "HB", "home", "male"),
                new Competitor("sr:competitor:5187", "HB Torshavn", "Faroe Islands", "FRO", "HB", "away", "male"));
        sportEventList = Arrays.asList(
                new SportEvent("sr:sport_event:27636100", "2021-06-22T18:00:00+00:00", "Soccer", "UEFA Champions League", "sr:competition:7", "UEFA Champions League 21/22", competitorList.subList(0, 2), venue, 2.5, 88.1, 9.4),
                new SportEvent("sr:sport_event:27636101", "2021-07-23T18:00:00+00:00", "Soccer", "UEFA Champions League", "sr:competition:7", "UEFA Champions League 21/22", competitorList.subList(2, 4), venue, 25.2, 12.5, 68.3),
                new SportEvent("sr:sport_event:27636102", "2021-08-20T18:00:00+00:00", "Soccer", "UEFA Champions League", "sr:competition:7", "UEFA Champions League 21/22", competitorList.subList(0, 2), venue, 47.1, 40.1, 18.9),
                new SportEvent("sr:sport_event:27636103", "2021-09-21T18:00:00+00:00", "Soccer", "UEFA Champions League", "sr:competition:7", "UEFA Champions League 21/22", competitorList.subList(1, 3), venue, 34.5, 11.3, 29.1));
        eventWrapperList = new EventWrapperList(sportEventList);
        eventResponseList = Arrays.asList(
                new EventResponse(sportEventList.get(0).getStartDate(), sportEventList.get(0).getCompetitionName(), competitorList.get(0).getName(), competitorList.get(0).getCountry(), competitorList.get(1).getName(), competitorList.get(1).getCountry(), "Competitor X vs. Competitor Y", venue.getName(), "DRAW", sportEventList.get(0).getProbabilityDraw(), "Probability value: X"),
                new EventResponse(sportEventList.get(1).getStartDate(), sportEventList.get(1).getCompetitionName(), competitorList.get(2).getName(), competitorList.get(2).getCountry(), competitorList.get(3).getName(), competitorList.get(3).getCountry(), "Competitor X vs. Competitor Y", venue.getName(), "AWAY_TEAM_WINNER", sportEventList.get(1).getProbabilityAwayTeamWinner(), "Probability value: X"),
                new EventResponse(sportEventList.get(2).getStartDate(), sportEventList.get(2).getCompetitionName(), competitorList.get(0).getName(), competitorList.get(0).getCountry(), competitorList.get(1).getName(), competitorList.get(1).getCountry(), "Competitor X vs. Competitor Y", venue.getName(), "HOME_TEAM_WINNER", sportEventList.get(2).getProbabilityHomeTeamWinner(), "Probability value: X"),
                new EventResponse(sportEventList.get(3).getStartDate(), sportEventList.get(3).getCompetitionName(), competitorList.get(1).getName(), competitorList.get(1).getCountry(), competitorList.get(2).getName(), competitorList.get(2).getCountry(), "Competitor X vs. Competitor Y", venue.getName(), "HOME_TEAM_WINNER", sportEventList.get(3).getProbabilityHomeTeamWinner(), "Probability value: X"));
    }

    @Test
    void shouldGetEventResponse() {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.readJsonFile("input.json")).thenReturn(eventWrapperList);

        EventResponse eventResponse1 = sportEventService.getEventResponse(sportEventList.get(0));
        EventResponse eventResponse2 = sportEventService.getEventResponse(sportEventList.get(3));

        Assertions.assertEquals("SS Folgore Falciano Calcio", eventResponse1.getHomeTeamName());
        Assertions.assertEquals("FC Prishtina", eventResponse1.getAwayTeamName());
        Assertions.assertEquals("FC Prishtina", eventResponse2.getHomeTeamName());
        Assertions.assertEquals("HB Torshavn", eventResponse2.getAwayTeamName());
    }

    @Test
    void shouldGetTopEventsByProbabilityValue() {
        JsonReader jsonReader = mock(JsonReader.class);

        List<EventResponse> actual1 = sportEventService.getTopEventsByProbabilityValue(1);
        List<EventResponse> actual2 = sportEventService.getTopEventsByProbabilityValue(10);

        Assertions.assertEquals(1, actual1.size());
        Assertions.assertEquals(10, actual2.size());
    }

    @Test
    void shouldGetBestProbabilityResultName() {
        String bestProbabilityResultName1 = sportEventService.getBestProbabilityResultName(sportEventList.get(0));
        String bestProbabilityResultName2 = sportEventService.getBestProbabilityResultName(sportEventList.get(1));
        String bestProbabilityResultName3 = sportEventService.getBestProbabilityResultName(sportEventList.get(2));
        String expectedDraw = "DRAW";
        String expectedAwayWin = "AWAY_TEAM_WIN";
        String expectedHomeWin = "HOME_TEAM_WIN";

        Assertions.assertEquals(expectedDraw, bestProbabilityResultName1);
        Assertions.assertEquals(expectedAwayWin, bestProbabilityResultName2);
        Assertions.assertEquals(expectedHomeWin, bestProbabilityResultName3);
    }

    @Test
    void shouldGetBestProbabilityResultValue() {
        double bestProbabilityResultValue1 = sportEventService.getBestProbabilityResultValue(sportEventList.get(0));
        double bestProbabilityResultValue2 = sportEventService.getBestProbabilityResultValue(sportEventList.get(1));
        double bestProbabilityResultValue3 = sportEventService.getBestProbabilityResultValue(sportEventList.get(2));
        double expected1 = 88.1;
        double expected2 = 68.3;
        double expected3 = 47.1;

        Assertions.assertEquals(expected1, bestProbabilityResultValue1);
        Assertions.assertEquals(expected2, bestProbabilityResultValue2);
        Assertions.assertEquals(expected3, bestProbabilityResultValue3);
    }
}
