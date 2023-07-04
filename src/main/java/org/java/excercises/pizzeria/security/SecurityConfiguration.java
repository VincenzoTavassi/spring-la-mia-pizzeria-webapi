package org.java.excercises.pizzeria.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    DatabaseUserDetailsService userDetailsService() {
        return new DatabaseUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
//                .requestMatchers(HttpMethod.POST).hasAuthority("ADMIN")
                .requestMatchers("pizza/create/**").hasAuthority("ADMIN")
                .requestMatchers("pizza/edit/**").hasAuthority("ADMIN")
                .requestMatchers("pizza/delete/**").hasAuthority("ADMIN")
                .requestMatchers("offers/edit/**").hasAuthority("ADMIN")
                .requestMatchers("offers/create/**").hasAuthority("ADMIN")
                .requestMatchers("ingredients/**").hasAuthority("ADMIN")
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/**").hasAnyAuthority("USER", "ADMIN")
                .and().formLogin()
                .and().logout();
        http.csrf().disable();
        return http.build();
    }
}
