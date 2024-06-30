package star_wars_card_collector.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import org.json.JSONObject;

/**
 * Service class to interact with the Star Wars API (SWAPI) for fetching character information.
 */
@Service
public class SwapiService {

    private static final String SWAPI_URL = "https://swapi.dev/api/people";
    private int peopleCount;

    /**
     * Initializes the service by fetching the total count of people (characters) available in SWAPI.
     */
    @PostConstruct
    public void fetchPeopleCount() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(SWAPI_URL, String.class);
        // Parse JSON response to extract count
        JSONObject jsonObject = new JSONObject(response.getBody());
        this.peopleCount = jsonObject.getInt("count");
    }

    /**
     * Retrieves the details of a specific person (character) from SWAPI based on the given ID.
     *
     * @param id The ID of the person to retrieve.
     * @return JSONObject containing the details of the person.
     */
    public JSONObject getPersonAt(int id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(SWAPI_URL + "/" + id, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        return jsonObject;
    }

    /**
     * Retrieves the total number of people (characters) available in SWAPI.
     *
     * @return The total number of people.
     */
    public int getPeopleCount() {
        return peopleCount;
    }
}
