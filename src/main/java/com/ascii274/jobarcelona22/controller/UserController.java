package com.ascii274.jobarcelona22.controller;

import com.ascii274.jobarcelona22.model.User;
import com.ascii274.jobarcelona22.model.UserDto;
import com.ascii274.jobarcelona22.repository.UserDtoRepository;
import com.ascii274.jobarcelona22.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/jobarcelona")
public class UserController {
    private static final Log log = LogFactory.getLog(UserController.class);

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
                )
        );
        List<User> users= mongoTemplate.find(query,User.class);
        log.info("***** " + query + " ****");
        if(users.size()>0){
            users.get(0).setToken(getJWTToken(user));
            return users.toString();
        }else {
            log.info("***** Invalid username or password ****");
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
     * - Accesos solo admin
     */
    @GetMapping(value = "/users")
    public Mono<List<UserDto>> users(){
        List<UserDto> listUsers = userDtoRepository.findAll();
        log.info(listUsers);
        return Mono.just(listUsers);
    }

    public static boolean validarEmail(String email){
        Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password){
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if(password == null || password == ""){
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }
    private String getJWTToken(User user){
        String secretKey = "Yda47dKBrx14kTAZXATM7OH29BtDgKUY8hYqmeiSCf4=";
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
               .commaSeparatedStringToAuthorityList("ROLE_USER");
//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String token = Jwts.builder()
                .setId(user.getUsername())
                .setSubject(user.getEmail())
//                .claim("scope",user.getRol(),
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
//                .signWith(SignatureAlgorithm.HS256,secretKey.getBytes(UTF_8))
//                .signWith(key,SignatureAlgorithm.HS256)
                .signWith(SignatureAlgorithm.HS256,secretKey.getBytes())
//                .signWith(key)
                .compact();
        return "Bearer " + token;
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
