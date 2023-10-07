package com.ecommerce.Domain.Application.Services;

import com.ecommerce.Domain.Application.Dtos.*;
import com.ecommerce.Domain.Application.Exceptions.BadRequest;
import com.ecommerce.Domain.Application.Exceptions.NotFoundException;
import com.ecommerce.Domain.Application.Exceptions.Unathorized;
import com.ecommerce.Domain.Application.Mappers.CustomerDtoMapper;
import com.ecommerce.Domain.Application.Mappers.UserDtoMapper;
import com.ecommerce.Domain.Application.Repositories.RoleRepository;
import com.ecommerce.Domain.Application.Repositories.UserRepository;
import com.ecommerce.Domain.Domain.User;
import com.ecommerce.Domain.Infrastructure.Config.Spring.CustomUserDetails;
import com.ecommerce.Domain.Infrastructure.Config.Spring.JwtService;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.UserEntity;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers.CustomerEntityMapper;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers.RoleEntityMapper;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers.UserEntityMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final RoleEntityMapper roleEntityMapper;
    private final UserEntityMapper userEntityMapper;
    private final UserDtoMapper userDtoMapper;
    private final CustomerDtoMapper customerDtoMapper;
    private final CustomerEntityMapper customerEntityMapper;
    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    public AuthenticationResponse register(RegisterRequest body) {
        var user = UserEntity.builder()
                .firstName(body.getFirstName())
                .lastName(body.getLastName())
                .email(body.getEmail())
                .password(passwordEncoder.encode(body.getPassword()))
                .roles(Set.of(
                        this.roleEntityMapper
                                .toEntity(roleRepository
                                        .findRoleByName("USER")
                                        .orElseThrow(() -> new NotFoundException("Role not found")))))
                .build();
        userRepository.save(userEntityMapper.toDomain(user));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public LoginResponse login(LoginRequest body) {
        authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(body.getEmail(),
                                body.getPassword()));
        var user = this.userEntityMapper.toEntity(
                userRepository.findByEmail(body.getEmail()).orElseThrow());
        var userDto = UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .customer(this.customerDtoMapper.toDto
                        (this.customerEntityMapper.toDomain(user.getCustomer())))
                .build();
        var jwtToken = jwtService.generateToken(user);
        return new LoginResponse(jwtToken, userDto);

    }

    public static void checkIfAdminOrSameUser(Long id, Authentication authentication) {
        Boolean hasPermissionToGetAll =
                authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        Boolean idParamIsEqualIdUserAuthenticated =
                (id == ((CustomUserDetails) authentication.getPrincipal()).getUser().getId());

        if (!hasPermissionToGetAll && !idParamIsEqualIdUserAuthenticated) {
            throw new AccessDeniedException("403");
        }
    }

    public LoginResponse loginGoogle(Map body) {
        String credential = (String) body.get("credential");
        if(credential == null) throw new Unathorized("Not Credential!");

         Map userDetails = this.googleVerify(credential);
         String userName = (String) userDetails.get("name");
         String userEmail = (String) userDetails.get("email");

        Optional<User> user = this.userRepository.findByEmail(userEmail);
        UserEntity userEntity;
        if(user.isEmpty()){
            User userToSave = this.userRepository.save(User.builder()
                            .firstName(userName)
                            .email(userEmail)
                            .password("123")
                    .build());
            userEntity = this.userEntityMapper.toEntity(this.userRepository.save(userToSave));
        }else {
            userEntity = this.userEntityMapper.toEntity(user.get());
        }
        return LoginResponse.builder()
                .jwtToken(jwtService.generateToken(userEntity))
                .user(this.userDtoMapper.toDto(this.userEntityMapper.toDomain(userEntity)))
                .build();
    }

    private Map googleVerify(String googleToken) {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Arrays.asList(this.googleClientId))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(googleToken);

            if (idToken == null) throw new BadRequest("Bad Google Token!");

                GoogleIdToken.Payload payload = idToken.getPayload();
                return new HashMap(){{
                    put("name",  payload.get("name"));
                    put("email", payload.getEmail());
                }};

        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("error Google Login");
        }
    }
}
