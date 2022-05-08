package foodPanda.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import foodPanda.exception.InvalidInputException;
import foodPanda.model.User;
import foodPanda.repository.AdministratorRepository;
import foodPanda.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdministratorRepository administratorRepository;

    /**
     * The method is used by Spring Security to load a User by its username and grant its authority roles
     *
     * @param username The username that is searched
     * @return An instance of a UserDetails class that contains the User data
     * @throws UsernameNotFoundException Whenever some bad input is sent or no User is found for a certain username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new RuntimeException("No User found for email: " + username)
        );

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (administratorRepository.findByUser(user).isPresent())
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        else authorities.add(new SimpleGrantedAuthority("USER"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    /**
     * The method provides the user with a new accessToken whenever it expires by using the already provided refreshToken
     *
     * @param request  The HTTP request
     * @param response The HTTP response
     * @throws IOException Whenever something didn't work well - request writing / token generation
     */
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorisationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorisationHeader != null && authorisationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorisationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                User user = userRepository.findByEmail(username).orElseThrow(
                        () -> new InvalidInputException("No user found for email: " + username)
                );
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (administratorRepository.findByUser(user).isPresent())
                    authorities.add(new SimpleGrantedAuthority("ADMIN"));
                else authorities.add(new SimpleGrantedAuthority("USER"));
                String accessToken = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
                LOGGER.info("New Access Token generated for user with email: " + user.getEmail() + " and roles: " + authorities);
            } catch (Exception e) {
                LOGGER.error("Error logging in -> {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getWriter(), error);
            }
        } else {
            throw new RuntimeException("Refresh token missing!");
        }

    }
}
