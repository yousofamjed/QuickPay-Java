package com.progressoft.training.payments.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
    public class SecurityConfig
    {
        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
            return new InMemoryUserDetailsManager(
                    User.withUsername("juman").password(passwordEncoder.encode("123456")).roles("PAYMENTS")
                            .build(),
                    User.withUsername("Yousof").password(passwordEncoder.encode("batata"))
                            .roles("PAYMENTS","TEMPLATES")
                            .build());
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception{

            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth->
                            auth.requestMatchers("/api/payments/**").hasAnyRole("PAYMENTS")
                                    .requestMatchers("/api/template/**").hasAnyRole("TEMPLATES").anyRequest().authenticated()
                    ).httpBasic(Customizer.withDefaults());

            return http.build();
        }


    }