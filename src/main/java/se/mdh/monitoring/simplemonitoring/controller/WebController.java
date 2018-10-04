package se.mdh.monitoring.simplemonitoring.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.mdh.monitoring.simplemonitoring.service.MonitoringService;

@Controller
@RequestMapping(value = "/monitoring")
public class WebController {

  @Value("${nagios.enviroment:Okänd}")
  String nagiosEnviroment;

  private final MonitoringService monitoringService;

  @Autowired
  public WebController(MonitoringService monitoringService) {
    this.monitoringService = monitoringService;
  }

  @GetMapping("")
  public String list(Model model) {
    Map<String, String> statusMap = monitoringService.getStatusMap();
    model.addAttribute("statusmap", statusMap);
    model.addAttribute("enviroment", nagiosEnviroment);
    return "list";
  }

}
