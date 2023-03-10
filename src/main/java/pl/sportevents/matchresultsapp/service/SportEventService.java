package pl.sportevents.matchresultsapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.sportevents.matchresultsapp.exception.JsonFileUploadException;
import pl.sportevents.matchresultsapp.model.EventResponse;
import pl.sportevents.matchresultsapp.model.EventWrapperList;
import pl.sportevents.matchresultsapp.model.SportEvent;

import java.util.*;

@Service
public class SportEventService {

    @Value("${file.path}")
    private String filePath;

    public List<EventResponse> getEvents() throws JsonFileUploadException {
        JsonReader jsonReader = new JsonReader();
        EventWrapperList wrapper = jsonReader.readJsonFile(filePath);
        List<EventResponse> eventResponses = new ArrayList<>();
        for (SportEvent sportEvent : wrapper.getEvents()) {
            eventResponses.add(getEventResponse(sportEvent));
        }

        return eventResponses;
    }

    public EventResponse getEventResponse(SportEvent sportEvent) {
        EventResponse eventResponse = new EventResponse();
        String dateAndHourOnly = sportEvent.getStartDate().substring(0, 10) + " " +
                sportEvent.getStartDate().substring(11, 19);

        eventResponse.setEventStartDate(dateAndHourOnly);
        eventResponse.setCompetitionName(sportEvent.getCompetitionName());
        eventResponse.setHomeTeamName(sportEvent.getCompetitors().get(0).getName());
        eventResponse.setHomeTeamCountry(sportEvent.getCompetitors().get(0).getCountry());
        eventResponse.setAwayTeamName(sportEvent.getCompetitors().get(1).getName());
        eventResponse.setAwayTeamCountry(sportEvent.getCompetitors().get(1).getCountry());
        eventResponse.setTeamDetails(
                        eventResponse.getHomeTeamName() + " (" +
                        eventResponse.getHomeTeamCountry() + ") vs. " +
                        eventResponse.getAwayTeamName() + " (" +
                        eventResponse.getAwayTeamCountry() + ")");
        eventResponse.setVenueName(sportEvent
                        .getVenue() == null ? "No information provided" : sportEvent.getVenue().getName());
        eventResponse.setHighestProbableResultName(getBestProbabilityResultName(sportEvent));
        eventResponse.setHighestProbableValue(getBestProbabilityResultValue(sportEvent));
        eventResponse.setHighestProbableResult(
                        eventResponse.getHighestProbableResultName() + " (" +
                        eventResponse.getHighestProbableValue() + ")"
        );

        return eventResponse;
    }

    public List<EventResponse> getTopEventsByProbabilityValue(int numberOfRecords) throws JsonFileUploadException {
        List<EventResponse> events = getEvents();
        events.sort((e1, e2) -> Double
                .compare((e2.getHighestProbableValue()), e1.getHighestProbableValue()));

        return events.subList(0, numberOfRecords);
    }

    public String getBestProbabilityResultName(SportEvent sportEvent) {
        double homeTeam = sportEvent.getProbabilityHomeTeamWinner();
        double awayTeam = sportEvent.getProbabilityAwayTeamWinner();
        double draw = sportEvent.getProbabilityDraw();

        if (homeTeam > awayTeam && homeTeam > draw) {
            return "HOME_TEAM_WIN";
        } else if (draw > awayTeam && draw > homeTeam) {
            return "DRAW";
        } else {
            return "AWAY_TEAM_WIN";
        }
    }

    public double getBestProbabilityResultValue(SportEvent sportEvent) {
        double homeTeam = sportEvent.getProbabilityHomeTeamWinner();
        double awayTeam = sportEvent.getProbabilityAwayTeamWinner();
        double draw = sportEvent.getProbabilityDraw();

        if (homeTeam > awayTeam && homeTeam > draw) {
            return homeTeam;
        } else if (draw > awayTeam && draw > homeTeam) {
            return draw;
        } else {
            return awayTeam;
        }
    }

    public Set<String> uniqueTeamNames() throws JsonFileUploadException {
        List<EventResponse> events = getEvents();
        Set<String> names = new HashSet<>();
        for (EventResponse event : events) {
            names.add(event.getHomeTeamName());
            names.add(event.getAwayTeamName());
        }

        return names;
    }

    public Set<String> uniqueTeamNamesForGivenCompetition(String competitionName) throws JsonFileUploadException {
        List<EventResponse> names = getEvents()
                .stream()
                .filter(e -> e.getCompetitionName().equals(competitionName))
                .toList();

        Set<String> uniqueNames = new HashSet<>();
        for (EventResponse event : names) {
            uniqueNames.add(event.getHomeTeamName());
            uniqueNames.add(event.getAwayTeamName());
        }

        return uniqueNames;
    }
}
