package com.ol.chronoshare.services;

import com.ol.chronoshare.JwtTokenUtil;
import com.ol.chronoshare.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class JwtAuthentificationService {


    @Value("${jwt.expires_in}")
    private Integer EXPIRES_IN;

    @Value("${jwt.cookie}")
    private String TOKEN_COOKIE;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    public ResponseCookie createAuthenticationToken(String username, String password ) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,  password));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseCookie.from(TOKEN_COOKIE, token).httpOnly(true)
                    .maxAge(EXPIRES_IN).path("/").build();



        }catch(DisabledException e) {
            throw new Exception();
        }

    }

    public ResponseCookie getAuthenticationToken(User user ) throws Exception {
        try {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseCookie.from(TOKEN_COOKIE, token).httpOnly(true)
                    .maxAge(EXPIRES_IN).path("/").build();
        }catch(DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        }

    }

    public String getEmailFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {

            String token = Stream.of(cookies)
                    .filter(cookie -> cookie.getName().equals(TOKEN_COOKIE))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);

            if (token != null) {
                String email = jwtTokenUtil.getUsernameFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if ( userDetails != null && jwtTokenUtil.validateToken(token, userDetails)) {
                    return email;
                }
            }
        }
        return null;
    }
//
//    private void authenticate(String username, String password) throws Exception {
//        try {
//            Authentication authentication =  ;
//        } catch (DisabledException e) {
//            throw new Exception("USER_DISABLED", e);
//        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }
//    }
}
