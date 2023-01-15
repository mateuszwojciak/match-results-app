package pl.sportevents.matchresultsapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.sportevents.matchresultsapp.exception.JsonFileUploadException;
import pl.sportevents.matchresultsapp.model.EventWrapperList;

import java.io.File;
import java.io.IOException;

class JsonReader {
    EventWrapperList readJsonFile(String filePath) throws JsonFileUploadException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filePath);

        try {
            return objectMapper.readValue(file, EventWrapperList.class);
        } catch (IOException e) {
            throw new JsonFileUploadException(
                    "An error occurred while uploading the JSON file. Verify path or URL. Error message: " + e.getMessage());
        }
    }
}
