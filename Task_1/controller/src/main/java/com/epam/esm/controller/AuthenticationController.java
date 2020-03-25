package com.epam.esm.controller;

import com.epam.esm.dto.UserEntityDTO;
import com.epam.esm.exception.UserException;
import com.epam.esm.security.JwtRequest;
import com.epam.esm.security.JwtResponse;
import com.epam.esm.security.token.TokenUtil;
import com.epam.esm.service.impl.UserDetailsServiceImpl;
import com.epam.esm.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private UserServiceImpl userServiceImpl;
    private TokenUtil tokenUtil;
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserServiceImpl userServiceImpl,
                                    TokenUtil tokenUtil, UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userServiceImpl = userServiceImpl;
        this.tokenUtil = tokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        authenticate(authenticationRequest.getLogin(), authenticationRequest.getPassword());
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getLogin());
        String accessToken = tokenUtil.generateAccessToken(userDetails);
        String refreshToken = tokenUtil.generateRefreshToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }

    @PostMapping("/token")
    public ResponseEntity<?> refreshAccessToken(@RequestBody JwtRequest jwtRequest) {
        String username = tokenUtil.getUsernameFromToken(jwtRequest.getRefreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String accessToken = tokenUtil.generateAccessToken(userDetails);
        String refreshToken = tokenUtil.generateRefreshToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserEntityDTO> signUp(@RequestBody UserEntityDTO userEntityDTO) {
        return new ResponseEntity<>(userServiceImpl.save(userEntityDTO), HttpStatus.CREATED);
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new UserException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new UserException("Incorrect login or password", e);
        }
    }
}
