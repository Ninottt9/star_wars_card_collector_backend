package star_wars_card_collector.service;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class SwapiService {

    private static final String SWAPI_URL = "https://swapi.dev/api/people";
    private int peopleCount;

    @PostConstruct
    public void fetchPeopleCount() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(SWAPI_URL, String.class);
        // Parse JSON response to extract results array
        JSONObject jsonObject = new JSONObject(response.getBody());
        // Parse JSON response to extract count
        this.peopleCount = jsonObject.getInt("count");
    }

    public JSONObject getPersonAt(int id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(SWAPI_URL + "/" + id, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        return jsonObject;
    }


    public int getPeopleCount() {
        return peopleCount;
    }
}
