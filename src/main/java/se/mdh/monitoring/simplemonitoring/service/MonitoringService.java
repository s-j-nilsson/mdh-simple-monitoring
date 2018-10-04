package se.mdh.monitoring.simplemonitoring.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;
import se.mdh.monitoring.simplemonitoring.rest.RestClient;

@Service
public class MonitoringService {

  private RestClient restClient;

  @Value("${nagios.monitored.services}")
  private String[] services;

  public MonitoringService(RestClient restClient) {
    this.restClient = restClient;
  }

  public Map<String, Object> getStatusMap() throws IOException {
    String nagiosServiceJson = restClient.getNagiosServiceJson();
    JsonParser jsonParser = JsonParserFactory.getJsonParser();
    Map<String, Object> jsonMap = jsonParser.parseMap(nagiosServiceJson);

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonRootNode = objectMapper.readTree(nagiosServiceJson);


    return jsonMap;
  }
}
