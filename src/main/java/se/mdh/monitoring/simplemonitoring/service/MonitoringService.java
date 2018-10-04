package se.mdh.monitoring.simplemonitoring.service;

import java.util.Map;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;
import se.mdh.monitoring.simplemonitoring.rest.RestClient;

@Service
public class MonitoringService {

  private RestClient restClient;

  public MonitoringService(RestClient restClient) {
    this.restClient = restClient;
  }

  public Map<String, Object> getStatusMap() {
    String nagiosServiceJson = restClient.getNagiosServiceJson();
    JsonParser jsonParser = JsonParserFactory.getJsonParser();
    Map<String, Object> jsonMap = jsonParser.parseMap(nagiosServiceJson);

    return jsonMap;
  }
}
