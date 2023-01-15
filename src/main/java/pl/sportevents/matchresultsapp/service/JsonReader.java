package pl.sportevents.matchresultsapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.sportevents.matchresultsapp.model.EventWrapperList;

import java.io.File;
import java.io.IOException;

class JsonReader {
    EventWrapperList readJsonFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filePath);

        return objectMapper.readValue(file, EventWrapperList.class);
    }
}
