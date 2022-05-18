package com.ascii274.jobarcelona22.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/jobarcelona/test").permitAll()
                .antMatchers(HttpMethod.POST, "/jobarcelona/login").permitAll()
                .antMatchers(HttpMethod.POST, "/jobarcelona/signup").permitAll()
//                .antMatchers(HttpMethod.GET, "/jobarcelona/users")
                .anyRequest().authenticated();
    }

}


