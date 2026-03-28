package com.orderflow.orderflow_api.services;

import com.orderflow.orderflow_api.models.Role;
import com.orderflow.orderflow_api.models.Roles;
import com.orderflow.orderflow_api.models.User;
import com.orderflow.orderflow_api.payload.AuthenticationResult;
import com.orderflow.orderflow_api.payload.UserDTO;
import com.orderflow.orderflow_api.payload.UserListResponse;
import com.orderflow.orderflow_api.repositories.RoleRepository;
import com.orderflow.orderflow_api.repositories.UserRepository;
import com.orderflow.orderflow_api.security.jwt.JwtUtils;
import com.orderflow.orderflow_api.security.request.LoginRequest;
import com.orderflow.orderflow_api.security.request.SignupRequest;
import com.orderflow.orderflow_api.security.response.MessageResponse;
import com.orderflow.orderflow_api.security.response.UserInfoResponse;
import com.orderflow.orderflow_api.security.services.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<MessageResponse> register(SignupRequest request) {
        if(userRepository.existsByUsername(request.getUsername())){
            return ResponseEntity.badRequest()
                    .body( new MessageResponse("Error: Username already exists"));
        }

        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest()
                    .body( new MessageResponse("Error: Email already exists"));
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                encoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName()

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
        return ResponseEntity.ok(new MessageResponse("User was registered successfully."));
    }

    @Override
    public UserInfoResponse getUserDetails(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        UserInfoResponse userResponse = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
        return userResponse;
    }

    @Override
    public ResponseCookie logOutUser() {
        return jwtUtils.getCleanJwtCookie();
    }

    @Override
    public AuthenticationResult login(LoginRequest request) {
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
            authentication = null;
            //return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(), jwtCookie.toString(), userDetails.getUsername(), userDetails.getEmail(), roles);

        return new AuthenticationResult(response, jwtCookie);
    }

    @Override
    public UserListResponse getAllUsers(String keyword, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder) {
        Sort sortByAndOrder =  sortOrder.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        PageRequest pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Specification<User> specification = Specification.allOf(List.of());

        if(keyword != null && !keyword.isEmpty()){
            specification = specification.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + keyword.toLowerCase() + "%"));
        }

        Page<User> pageUsers = userRepository.findAll(specification, pageDetails);

        List<User> usersPage = pageUsers.getContent();

        List<UserDTO> userDTOs = usersPage.stream().map(user -> {
            return modelMapper.map(user, UserDTO.class);
        }).collect(Collectors.toList());

        UserListResponse userListResponse = new UserListResponse();
        userListResponse.setContent(userDTOs);
        userListResponse.setPageNumber(pageUsers.getNumber());
        userListResponse.setPageSize(pageUsers.getSize());
        userListResponse.setTotalPages(pageUsers.getTotalPages());
        userListResponse.setTotalElements(pageUsers.getTotalElements());
        userListResponse.setLastPage(pageUsers.isLast());
        userListResponse.setTimestamp(LocalDateTime.now(ZoneId.of("UTC")));
        return userListResponse;
    }
}
