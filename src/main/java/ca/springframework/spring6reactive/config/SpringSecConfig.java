//package ca.springframework.spring6reactive.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
///**
// * Created by jt, Spring Framework Guru.
// */
//@Configuration
//@EnableWebFluxSecurity
//public class SpringSecConfig {
//
//    @Bean
//    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
//        http.csrf().disable()
//                .authorizeExchange()
//                .anyExchange().authenticated()
//                .and()
//                .oauth2ResourceServer().jwt();
//
//        return http.build();
//    }
//}