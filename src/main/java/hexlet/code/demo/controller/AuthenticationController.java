package hexlet.code.demo.controller;

import hexlet.code.demo.dto.AuthRequestDTO;

import hexlet.code.demo.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JWTUtils jwtUtils;

    @PostMapping("/login")
    public String auth(@RequestBody AuthRequestDTO dto) {
        var authentication = new UsernamePasswordAuthenticationToken(
                dto.getUsername(),
                dto.getPassword());

        authenticationManager.authenticate(authentication);

        return jwtUtils.generateToken(dto.getUsername());
    }
}
