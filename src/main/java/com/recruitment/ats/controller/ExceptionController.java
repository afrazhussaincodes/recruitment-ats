package com.recruitment.ats.controller;
import com.recruitment.ats.model.Application; import com.recruitment.ats.service.ExceptionService; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/exceptions") @CrossOrigin(origins="*")
public class ExceptionController {
 private final ExceptionService s; public ExceptionController(ExceptionService s){this.s=s;}
 @GetMapping public List<Application> getExceptions(){return s.getExceptionApplications();}
}