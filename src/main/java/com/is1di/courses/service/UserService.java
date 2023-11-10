package com.is1di.courses.service;

import com.is1di.courses.entity.Student;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    public boolean allowed(JwtAuthenticationToken token, String role) {
        return token.getAuthorities().stream().anyMatch(t -> t.getAuthority().equals("ROLE_"+role));
    }

    public String sub(JwtAuthenticationToken token) {
        return token.getTokenAttributes().get("sub").toString();
    }

    public String fullName(JwtAuthenticationToken token) {
        return token.getTokenAttributes().get("full_name").toString();
    }

    public String direction(JwtAuthenticationToken token) {
        return token.getTokenAttributes().get("direction").toString();
    }

    public String group(JwtAuthenticationToken token){
        return token.getTokenAttributes().get("group").toString();
    }

    public Student toStudent(JwtAuthenticationToken token) {
        return new Student(sub(token),fullName(token),direction(token),group(token),new ArrayList<>());
    }
}
