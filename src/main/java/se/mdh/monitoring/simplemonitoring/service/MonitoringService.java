package se.mdh.monitoring.simplemonitoring.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.mdh.monitoring.simplemonitoring.rest.RestClient;

@Service
public class MonitoringService {
  private static final Log log = LogFactory.getLog(MonitoringService.class);

  private RestClient restClient;

  @Value("${nagios.monitored.services.filter:}")
  private String[] services;

  public MonitoringService(RestClient restClient) {
    this.restClient = restClient;
  }

  public Map<String, String> getStatusMap() {
    Map<String, String> statusMap = new TreeMap<>();
    try {
      List<String> servicesList = Arrays.asList(services);

      String nagiosServiceJson = restClient.getNagiosServiceJson();

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonRootNode = objectMapper.readTree(nagiosServiceJson);

      JsonNode servicesRootNode = jsonRootNode.get("services");

      Iterator<Map.Entry<String, JsonNode>> hostFields = servicesRootNode.fields();
      while(hostFields.hasNext()) {
        Map.Entry<String, JsonNode> hostEntry = hostFields.next();
        Iterator<Map.Entry<String, JsonNode>> serviceFields = hostEntry.getValue().fields();
        while(serviceFields.hasNext()) {
          Map.Entry<String, JsonNode> serviceEntry = serviceFields.next();

          JsonNode serviceDescription = serviceEntry.getValue().findValue("service_description");
          JsonNode currentState = serviceEntry.getValue().findValue("current_state");
          List<JsonNode> values = jsonRootNode.findValues(serviceEntry.getKey());

          if(!servicesList.isEmpty()) {
            for(String service : servicesList) {
              if(serviceEntry.getKey().toLowerCase().contains(service.toLowerCase())) {
                statusMap.put(serviceDescription.textValue() + (values.size() > 1 ? " (" + serviceEntry.getValue().findValue("host_name").textValue()  + ")" : ""), currentState.textValue());
              }
            }
          } else {
            statusMap.put(serviceDescription.textValue() + (values.size() > 1 ? " (" + serviceEntry.getValue().findValue("host_name").textValue()  + ")" : ""), currentState.textValue());
          }
        }
      }
    } catch(IOException e) {
      log.error("Could not parse json from Nagios", e);
    }
    return statusMap;
  }
}
