package com.recruitment.ats.controller;
import com.recruitment.ats.model.*; import com.recruitment.ats.service.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/dashboard") @CrossOrigin(origins="*")
public class DashboardController {
 private final DashboardService dashboard; private final ApplicationService apps; private final ExceptionService exceptions;
 public DashboardController(DashboardService d,ApplicationService a,ExceptionService e){dashboard=d;apps=a;exceptions=e;}
 @GetMapping("/summary") public DashboardSummary summary(){return dashboard.getSummary();}
 @GetMapping("/status/{status}") public List<Application> status(@PathVariable String status){return apps.getByStatus(status);}
 @GetMapping("/exceptions") public List<Application> exceptions(){return exceptions.getExceptionApplications();}
}