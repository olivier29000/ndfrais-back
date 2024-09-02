package com.ol.chronoshare.services;

import com.ol.chronoshare.JwtTokenUtil;
import com.ol.chronoshare.model.User;
import com.ol.chronoshare.model.exceptions.ChronoshareException;
import com.ol.chronoshare.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.expires_in}")
    private Integer EXPIRES_IN;

    @Value("${jwt.cookie}")
    private String TOKEN_COOKIE;

    @Value("${jwt.secret}")
    private String secret;



    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new ChronoshareException("aucun compte avec l\'email " + email + " n\'existe");
        }
        User user = optionalUser.get();
        List<GrantedAuthority> roles = new ArrayList<>();
        user.getRoleList().forEach(rol -> {
            roles.add(new SimpleGrantedAuthority(rol.toString()));
        });
        UserDetails ud = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, roles);
        return ud;
    }

}
