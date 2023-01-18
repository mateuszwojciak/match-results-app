package pl.sportevents.matchresultsapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.sportevents.matchresultsapp.controller.SportEventController;
import pl.sportevents.matchresultsapp.exception.InvalidNumberOfRecords;
import pl.sportevents.matchresultsapp.model.*;
import pl.sportevents.matchresultsapp.service.SportEventService;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

@WebMvcTest(SportEventController.class)
public class SportEventControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
    void shouldGetEvents() throws Exception {
        when(sportEventService.getEvents()).thenReturn(eventResponseList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/sport-event/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)));
    }

    @Test
    void shouldGetTopProbabilityRecords_status200() throws Exception {
        when(sportEventService.getTopEventsByProbabilityValue(2)).thenReturn(eventResponseList.subList(0, 2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/sport-event/info/top/{numOfEvents}", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }

    @Test
    void getTopProbabilityRecords_shouldThrowException() throws Exception {
        when(sportEventService.getTopEventsByProbabilityValue(5))
                .thenThrow(new InvalidNumberOfRecords("Given parameter is not valid. Max possible value is: 4"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/sport-event/info/top/{numOfEvents}", 5)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void shouldGetUniqueNames() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/sport-event/info/team-names/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void shouldGetUniqueNamesForCompetition() throws Exception {
        Set<String> testSet = new HashSet<>();
        testSet.add("name");
        when(sportEventService.uniqueTeamNamesForGivenCompetition("name"))
                .thenReturn(testSet);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/sport-event/info/team-names/{competitionName}", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void getUniqueNamesForCompetition_shouldThrowException() throws Exception {
        Set<String> testSet = new HashSet<>();
        when(sportEventService.uniqueTeamNamesForGivenCompetition("notExistingCompetition"))
                .thenReturn(testSet);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/sport-event/info/team-names/{competitionName}", "notExistingCompetition")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
    }
}
