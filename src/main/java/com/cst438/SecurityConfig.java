package com.cst438;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.cst438.service.UserDetailsServiceImpl;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Autowired
    private AuthEntryPoint exceptionHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/students").authenticated()
                .antMatchers(HttpMethod.GET, "/student/{email}").permitAll()
                .antMatchers(HttpMethod.POST, "/student").authenticated()
                .antMatchers(HttpMethod.DELETE, "/student/{student_id}").authenticated()
                .antMatchers(HttpMethod.PUT, "/student/{student_id}").authenticated()
                .antMatchers(HttpMethod.GET, "/schedule").authenticated()
                .antMatchers(HttpMethod.POST, "/schedule/course/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/schedule/{enrollment_id}").authenticated()
                .antMatchers(HttpMethod.GET, "/h2-console/**", "/favicon.ico").permitAll()
                .antMatchers(HttpMethod.POST, "/h2-console/**").permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling()
                .authenticationEntryPoint(exceptionHandler).and()
                .addFilterBefore(authenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(false);
        config.applyPermitDefaultValues();

        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception  {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws
            Exception {
        return authenticationManager();
    }
}