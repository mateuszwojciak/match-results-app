package pl.sportevents.matchresultsapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sportevents.matchresultsapp.exception.InvalidNumberOfRecords;
import pl.sportevents.matchresultsapp.exception.JsonFileUploadException;
import pl.sportevents.matchresultsapp.exception.ResourceNotFoundException;
import pl.sportevents.matchresultsapp.model.EventResponse;
import pl.sportevents.matchresultsapp.service.SportEventService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sport-event")
public class SportEventController {

    private final SportEventService sportEventService;

    @Autowired
    public SportEventController(SportEventService sportEventService) {
        this.sportEventService = sportEventService;
    }

    @GetMapping("/all")
    public List<EventResponse> getAllEvents() throws JsonFileUploadException {
        return sportEventService.getEvents();
    }

    @GetMapping("/info/top/{numOfEvents}")
    public List<EventResponse> getTopProbabilityRecords(@PathVariable("numOfEvents") int numOfEvents) throws JsonFileUploadException {
        try {
            return sportEventService.getTopEventsByProbabilityValue(numOfEvents);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidNumberOfRecords("Given parameter is not valid. Max possible value is: " +
                    getAllEvents().size() + ". Error message: " + e);
        }
    }

    @GetMapping("/info/team-names/all")
    public List<String> getUniqueTeamNames() throws JsonFileUploadException {

        return sportEventService.uniqueTeamNames()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @GetMapping("/info/team-names/{competitionName}")
    public List<String> getUniqueNamesForCompetition(@PathVariable("competitionName") String competitionName)
            throws JsonFileUploadException {
        List<String> results = sportEventService.uniqueTeamNamesForGivenCompetition(competitionName)
                .stream()
                .sorted()
                .toList();

        if (results.isEmpty())
            throw new ResourceNotFoundException("Provided competition name not found. Verify name and try again.");

        return sportEventService.uniqueTeamNamesForGivenCompetition(competitionName)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
