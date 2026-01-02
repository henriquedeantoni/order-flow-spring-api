package com.orderflow.orderflow_api.controllers;

import com.orderflow.orderflow_api.models.Role;
import com.orderflow.orderflow_api.models.Roles;
import com.orderflow.orderflow_api.models.User;
import com.orderflow.orderflow_api.repositories.RoleRepository;
import com.orderflow.orderflow_api.repositories.UserRepository;
import com.orderflow.orderflow_api.secutiry.jwt.JwtUtils;
import com.orderflow.orderflow_api.secutiry.request.LoginRequest;
import com.orderflow.orderflow_api.secutiry.request.SignupRequest;
import com.orderflow.orderflow_api.secutiry.response.UserInfoResponse;
import com.orderflow.orderflow_api.secutiry.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticationUser(@RequestBody LoginRequest request){
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException ex){
            Map<String,Object> map = new HashMap<>();
            map.put("message", "Bad Credentials");
            map.put("status", false);

            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(), jwtCookie.toString(), userDetails.getUsername(), roles);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest request){
        if(userRepository.existsByUserName(request.getUsername())){
            return ResponseEntity.badRequest()
                    .body("Username already exists");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest()
                    .body("Email already exists");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        Set<String> strRoles = request.getRoles();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null){
            Role userRole = roleRepository.findByRoleName(Roles.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(Roles.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(adminRole);
                        break;
                    case "attendance":
                        Role attendanceRole = roleRepository.findByRoleName(Roles.ROLE_ATTENDANCE)
                                .orElseThrow(() -> new RuntimeException("Erro: Role not found."));
                        roles.add(attendanceRole);
                        break;
                    case "client":
                        Role clientRole = roleRepository.findByRoleName(Roles.ROLE_CLIENT)
                                .orElseThrow(() -> new RuntimeException("Erro: Role not found."));
                        roles.add(clientRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(Roles.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
}
