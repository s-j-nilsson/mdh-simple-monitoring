package se.mdh.monitoring.simplemonitoring.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;
import se.mdh.monitoring.simplemonitoring.rest.RestClient;

@Service
public class MonitoringService {
  private static final Log log = LogFactory.getLog(MonitoringService.class);


  private RestClient restClient;

  @Value("${nagios.monitored.services}")
  private String[] services;

  public MonitoringService(RestClient restClient) {
    this.restClient = restClient;
  }

  public Map<String, String> getStatusMap() {
    Map<String, String> statusMap = new TreeMap<>();
    try {
      String nagiosServiceJson = restClient.getNagiosServiceJson();
      JsonParser jsonParser = JsonParserFactory.getJsonParser();
      Map<String, Object> jsonMap = jsonParser.parseMap(nagiosServiceJson);

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonRootNode = objectMapper.readTree(nagiosServiceJson);

      List<String> serviceList = Arrays.asList(services);
      for(String serviceName : serviceList) {
        List<JsonNode> values = jsonRootNode.findValues(serviceName);
        for(JsonNode serviceNode : values) {
          if(!(serviceNode instanceof MissingNode)) {
            JsonNode serviceDescription = serviceNode.findValue("service_description");
            JsonNode currentState = serviceNode.findValue("current_state");
            statusMap.put(serviceDescription.textValue() + (values.size() > 1 ? " (" + serviceNode.findValue("host_name").textValue()  + ")" : ""), currentState.textValue());
          }
          else {
            log.warn("Kunde inte hitta tjänsten med namn " + serviceName);
          }
        }
      }
    } catch(IOException e) {
      log.error("Could not parse json from Nagios", e);
    }
    return statusMap;
  }
}
