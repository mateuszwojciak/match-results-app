package pl.sportevents.matchresultsapp;

import org.junit.jupiter.api.Test;
import pl.sportevents.matchresultsapp.exception.JsonFileUploadException;
import pl.sportevents.matchresultsapp.model.EventWrapperList;
import pl.sportevents.matchresultsapp.service.JsonReader;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTests {
    @Test
    public void getFileWithValidFilePath() throws JsonFileUploadException {
        JsonReader jsonReader = new JsonReader();
        EventWrapperList result = jsonReader.readJsonFile("src/main/resources/input.json");
        assertNotNull(result);
        assertTrue(result instanceof EventWrapperList);
    }

    @Test
    public void getFileWithInvalidFilePath() {
        JsonReader jsonReader = new JsonReader();
        assertThrows(JsonFileUploadException.class, () -> jsonReader.readJsonFile("this/is/invalid/path/input.json"));
    }
}
