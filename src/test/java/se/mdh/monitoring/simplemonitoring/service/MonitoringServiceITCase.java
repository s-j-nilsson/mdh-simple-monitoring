package se.mdh.monitoring.simplemonitoring.service;

import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitoringServiceITCase {

  @Autowired
  private MonitoringService monitoringService;

  @Test
  public void getStatusMap() throws Exception {
    Map<String, Object> statusMap = monitoringService.getStatusMap();
    assertFalse(statusMap.keySet().isEmpty());
  }
}