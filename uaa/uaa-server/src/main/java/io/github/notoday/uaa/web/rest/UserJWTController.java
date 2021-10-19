package io.github.notoday.uaa.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.notoday.uaa.security.AuthoritiesConstants;
import io.github.notoday.uaa.security.SecurityUtils;
import io.github.notoday.uaa.security.jwt.JWTFilter;
import io.github.notoday.uaa.security.jwt.TokenProvider;
import io.github.notoday.uaa.web.rest.vm.LoginVM;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<Identity> authenticate() {
        return ResponseEntity.ok(new Identity(
            SecurityUtils.getCurrentUserLogin().orElse(AuthoritiesConstants.ANONYMOUS),
            SecurityUtils.getCurrentUserAuthorities())
        );
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

    static class Identity {
        private String login;
        private Set<String> authorities;

        public Identity(String login, Set<String> authorities) {
            this.login = login;
            this.authorities = authorities;
        }

        public String getLogin() {
            return login;
        }

        public Identity setLogin(String login) {
            this.login = login;
            return this;
        }

        public Set<String> getAuthorities() {
            return authorities;
        }

        public Identity setAuthorities(Set<String> authorities) {
            this.authorities = authorities;
            return this;
        }
    }
}
