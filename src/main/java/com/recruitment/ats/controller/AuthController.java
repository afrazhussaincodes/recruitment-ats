package com.recruitment.ats.controller;
import com.recruitment.ats.model.Candidate; import com.recruitment.ats.service.AuthService; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/auth") @CrossOrigin(origins="*")
public class AuthController {
 private final AuthService auth; public AuthController(AuthService a){auth=a;}
 @PostMapping("/register") public ResponseEntity<Map<String,Object>> register(@RequestBody Map<String,String> body){
   Candidate c=new Candidate(); c.setName(body.get("name"));c.setEmail(body.get("email"));c.setPhone(body.get("phone"));
   return ResponseEntity.status(HttpStatus.CREATED).body(auth.registerCandidate(c,body.get("password")));
 }
 @PostMapping("/login") public Map<String,Object> login(@RequestBody Map<String,String> body){return auth.login(body.get("email"),body.get("password"));}
}