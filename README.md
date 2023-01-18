# match-results-app
**`Java 17`** **`Spring Boot 3.0.1`** **`Maven 4.0.0`** **`Lombok`** **`JUnit 5`** **`Jackon 2.14.1`**
## About
App returning most probable match results based on JSON input file.

## Installation
##### Maven
1. Clone the repository to your local machine using https://github.com/mateuszwojciak/match-results-app.git
2. Navigate to the project directory in your terminal
3. Run mvn clean install to build the project
```
mvn clean install
```

## Start and stop the app
##### Running the app
1. Navigate to the project directory in your terminal
2. Run mvn spring-boot:run to start the application
```
mvn spring-boot:run
```
3. The application will be available at http://localhost:8080

##### Stopping the app
1. Press CTRL + C in the terminal where the application is running
2. Run mvn spring-boot:stop to stop the application
```
mvn spring-boot:stop
```

Please note that the above instructions may vary depending on the specific setup of your machine and the version of the tools you are using.

## Details
### Additional information
Assumption: applicaton for sports data company & end users want to know X most probable match results for UEFA Champions Leage season 21/22. There are 3 possible results with probability value for each match. Possible results name are HOME team win, DRAW, AWAY team win. Every team has a field "qualifier" to distinguish if it's home or away match. Every match has competitors details, including full name of teams and countries.

### Input data
In folder src/main/resources there is JSON input file with information about events, matches, competitors and venues (4 domains). The returned data is mapped in order to provide custom information like start_date, competiton_name, team_details (team 1 vs. team 2), venue (name), highest_probable_result (DRAW, AWAY_TEAM_WIN, HOME_TEAM_WIN with probability value).

### Error handling
There are 3 error handlers in the project which return custom message: InvalidNumberOfRecords, JsonFileUploadException, ResourceNotFoundException.

### Run tests
1. Navigate to the project directory in your terminal
2. Run mvn test to start executing tests
```
mvn test
```

## Endpoints
### To use match-results-app you will need to make HTTP requests to particular endpoints. The app returns data in JSON format. For sending HTTP requests you can use cURL commands or tool like Postman with interactive user interface.


### `GET /sport-event/all`

Retrieves all match events.

Example request:
```
curl --request GET 'http://localhost:8080/sport-event/all'
```

Example response (partial response provided due to size):
```
[
    {
        "start_date": "2021-06-22 18:00:00",
        "competition_name": "UEFA Champions League",
        "team_details": "SS Folgore Falciano Calcio (San Marino) vs. FC Prishtina (Kosovo)",
        "venue": "Elbasan Arena",
        "highest_probable_result": "DRAW (88.1)"
    },
    {
        "start_date": "2021-06-22 18:00:00",
        "competition_name": "UEFA Champions League",
        "team_details": "HB Torshavn (Faroe Islands) vs. Inter Club de Escaldes (Andorra)",
        "venue": "Niko Dovana Stadium",
        "highest_probable_result": "HOME_TEAM_WIN (68.9)"
    },
    {
        "start_date": "2021-06-25 18:00:00",
        "competition_name": "UEFA Champions League",
        "team_details": "FC Prishtina (Kosovo) vs. Inter Club de Escaldes (Andorra)",
        "venue": "No information provided",
        "highest_probable_result": "HOME_TEAM_WIN (68.0)"
    },
    [...]
        {
        "start_date": "2021-08-10 17:00:00",
        "competition_name": "UEFA Champions League",
        "team_details": "FC Sheriff Tiraspol (Moldova) vs. FK Crvena Zvezda Belgrade (Serbia)",
        "venue": "Sheriff Stadium",
        "highest_probable_result": "DRAW (43.1)"
    }
]
```


### `GET /sport-event/info/top/{numOfEvents}`

Returns given by user (in path variable) number of event responses with the highest probable result. Infomration include name of result (draw of which team win) and value. If given number exceeds all number of events, application returns error message with information about possible max number of events which can be returned.

Example request (status 200 OK):
```
curl --request GET 'http://localhost:8080/sport-event/info/top/3
```

Example response (status 200 OK):
```
[
    {
        "start_date": "2021-06-22 18:00:00",
        "competition_name": "UEFA Champions League",
        "team_details": "SS Folgore Falciano Calcio (San Marino) vs. FC Prishtina (Kosovo)",
        "venue": "Elbasan Arena",
        "highest_probable_result": "DRAW (88.1)"
    },
    {
        "start_date": "2021-07-06 16:00:00",
        "competition_name": "UEFA Champions League",
        "team_details": "Ferencvarosi Budapest (Hungary) vs. FC Prishtina (Kosovo)",
        "venue": "Groupama Arena",
        "highest_probable_result": "HOME_TEAM_WIN (83.7)"
    },
    {
        "start_date": "2021-07-06 17:00:00",
        "competition_name": "UEFA Champions League",
        "team_details": "FC CFR 1907 Cluj (Romania) vs. FK Borac Banja Luka (Bosnia & Herzegovina)",
        "venue": "Dr. Constantin Radulescu",
        "highest_probable_result": "HOME_TEAM_WIN (82.1)"
    }
]
```


Example request (status 400 Bad Request):
```
curl --request GET 'http://localhost:8080/sport-event/info/top/1000
```

Example response (status 400 Bad Request):
```
{
    "timestamp": "2023-01-18T18:02:45.597+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Given parameter is not valid. Max possible value is: 73. Error message: java.lang.IndexOutOfBoundsException: toIndex = 1000",
    "path": "/sport-event/info/top/1000"
}
```


### `GET /sport-event/info/team-names/all`

Retrieves list of unique team names in descending order.

Example request:
```
curl --request GET 'http://localhost:8080/sport-event/info/team-names/all'
```

Example response (partial response provided due to size):
```
[
    "AC Omonia Nicosia",
    "AS Monaco",
    "Benfica Lisbon",
    "Bodoe/Glimt",
    "CS Fola Esch",
    [...]
    "Sparta Prague",
    "Valur Reykjavik",
    "Vilnius FK Zalgiris",
    "Young Boys Bern"
]
```


### `GET /sport-event/info/team-names/{competitionName}`

Retrieves list of unique team names in descending order for given competition name. If given competition does not exists, app response with custom message.

Example request (status 200 OK):
```
curl --request GET 'http://localhost:8080/sport-event/info/team-names/UEFA%20Champions%20League'
```

Example response (status 200 OK, partial response provided due to size):
```
[
    "AC Omonia Nicosia",
    "AS Monaco",
    "Benfica Lisbon",
    "Bodoe/Glimt",
    "CS Fola Esch",
    [...]
    "Sparta Prague",
    "Valur Reykjavik",
    "Vilnius FK Zalgiris",
    "Young Boys Bern"
]
```


Example request (status 404 Not Found):
```
curl --request GET 'http://localhost:8080/sport-event/info/team-names/UEAF'
```

Example response (status 404 Not Found):
```
{
    "timestamp": "2023-01-18T18:11:49.016+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "Provided competition name not found. Verify name and try again.",
    "path": "/sport-event/info/team-names/UEAF"
}
```
