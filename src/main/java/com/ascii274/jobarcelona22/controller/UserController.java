package com.ascii274.jobarcelona22.controller;

import com.ascii274.jobarcelona22.model.User;
import com.ascii274.jobarcelona22.model.UserDto;
import com.ascii274.jobarcelona22.repository.UserDtoRepository;
import com.ascii274.jobarcelona22.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

import static com.ascii274.jobarcelona22.config.ValidationEmailPassword.isValidPassword;
import static com.ascii274.jobarcelona22.config.ValidationEmailPassword.validarEmail;

@RestController
@RequestMapping(value="/jobarcelona")
public class UserController {
    private static final Log log = LogFactory.getLog(UserController.class);
    private static final String secretKey = "Yda47dKBrx14kTAZXATM7OH29BtDgKUY8hYqmeiSCf4=";

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDtoRepository userDtoRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping (value="/test")
    public String hello(){
        return "Hello testing jobarcelona 2022";
    }

    /**
     * - Busca usuario y contraseña
     * @param user
     * @return
     */
    @PostMapping (value = "/login", consumes="application/json")
    public String login(@RequestBody User user ){
        Query query = new Query();
        query.addCriteria(
                Criteria.where("").andOperator(
                        Criteria.where("username").is(user.getUsername()),
                        Criteria.where("password").is(user.getPassword())
                ));
        List<User> users= mongoTemplate.find(query,User.class);
        if(users.size()>0){
            users.get(0).setToken(getJWTToken(user));
            return users.toString();
        }else {
            return "Invalid username or password";
        }
    }

    /**
     * - Registra usuarios y añade token.
     */
    @PostMapping (value = "/signup")
    public String signup(@RequestBody User user){

        User userExist = userRepository.findByUsername(user.getUsername());
        if(!isValidPassword(user.getPassword())){
            return "La contraeña ha de tener almenos 8 caracteres, 1 mayúscula, 1 minúscula-";
        }

        if(!validarEmail(user.getEmail())){
            return "El formato del email no es válido.";
        }

        if(userExist != null){
            return "Usuario '" + user.getUsername() + "' ya existe";
        } else {
            user.setToken(getJWTToken(user));
            userRepository.save(user);
            return user.toString();
        }
    }

    /**
     * - Devuelve toda la lista de usaurios -> ok
     * - Accesos solo admin -> solo con token
     */
    @GetMapping(value = "/users")
    public Mono<List<UserDto>> users(){
        List<UserDto> listUsers = userDtoRepository.findAll();
        log.info(listUsers);
        return Mono.just(listUsers);
    }

    private String getJWTToken(User user){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");
        String token = Jwts.builder()
                .setId(user.getUsername())
                .setSubject(user.getEmail())
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .signWith(key)
                .compact();
        return "Bearer " + token;
    }
}
