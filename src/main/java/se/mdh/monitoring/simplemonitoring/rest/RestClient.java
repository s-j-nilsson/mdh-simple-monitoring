package se.mdh.monitoring.simplemonitoring.rest;

import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RestClient {
  private static final Log log = LogFactory.getLog(RestClient.class);

  private final RestTemplate restTemplate;

  @Value("${nagios.url}")
  private String nagiosUrl;

  public RestClient(RestTemplateBuilder restTemplateBuilder,
                    @Value("${nagios.username}") String username,
                    @Value("${nagios.password}") String password) {
    this.restTemplate = restTemplateBuilder.basicAuthorization(username, password).build();
  }

  public String getNagiosServiceJson() {
    final URI uri = UriComponentsBuilder.newInstance()
        .uri(URI.create(nagiosUrl))
        .build().toUri();

    log.info("GET: " + uri);
    ResponseEntity<String> statusResponseEntity = restTemplate.getForEntity(uri, String.class);
    String json = statusResponseEntity.getBody();
    return json;
  }
}
