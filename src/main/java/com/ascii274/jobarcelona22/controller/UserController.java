package com.ascii274.jobarcelona22.controller;

import com.ascii274.jobarcelona22.model.User;
import com.ascii274.jobarcelona22.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.List;

@RestController
@RequestMapping(value="/jobarcelona")
public class UserController {
    private static final Log log = LogFactory.getLog(UserController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping (value="/test")
    public String hello(){
        return "Hello testing jobarcelona 2022";
    }

    @PostMapping (value = "/login", consumes="application/json")
    public Mono<List<User>> login(@RequestBody User user ){
        
        String token = getJWTToken(user.getUsername());
        Query query = new Query();
//        query.addCriteria( Criteria.where("username").is(user.getUsername()).and("password").is(user.getPassword()) );
//        query.addCriteria( Criteria.where("username").is(user.getUsername()));
        query.addCriteria(
                Criteria.where("").andOperator(
                        Criteria.where("username").is(user.getUsername()),
                        Criteria.where("password").is(user.getPassword())
                )
        );
        List<User> users= mongoTemplate.find(query,User.class);
       log.info("***** " + query);
        return Mono.just(users);
    }

    @PostMapping (value = "/signup")
    public Mono<User> signup(@RequestBody User user){
        userRepository.save(user);
        return Mono.just(user);
    }

    /**
     * - Devuelve toda la lista de usaurios
     * - Accesos solo admin
     * @return
     */
    @GetMapping(value = {"/users"})
    public Mono<List<User>> users(){
        List<User> listUsers = userRepository.findAll();
        return Mono.just(listUsers);
    }


    private String getJWTToken(String username){
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jws = Jwts.builder()
                .setSubject(username)
                .signWith(key)
                .compact();
        return jws;
    }


    //TODO delete below
/*    private String getJWTToken_2(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");
        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,secretKey.getBytes()).compact();
        return "Bearer " + token;
    }*/

}
