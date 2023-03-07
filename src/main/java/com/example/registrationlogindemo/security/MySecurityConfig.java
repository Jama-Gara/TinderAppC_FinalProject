package com.example.registrationlogindemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

//  public MySecurityConfig(CreateUsersOnce creator) {
//    creator.create();
//  }

//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//
//    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//    http
//        .authorizeRequests()
//        .antMatchers("/auth/login/**").permitAll()
//
////        .antMatchers("/auth/**").permitAll()
////        .antMatchers("/resources/**").permitAll()
////        .antMatchers("/guest/**").permitAll()
////        .antMatchers("/home/**").authenticated()
////        .antMatchers("/admin/**").hasAnyRole("ADMIN")
////        .antMatchers("/me/**").hasAnyRole("USER")
////        .antMatchers("/news/**").hasAnyRole("ADMIN", "USER")
////        .anyRequest().authenticated();
//  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
            .withUser("user").password("{noop}password").roles("USER")
            .and()
            .withUser("admin").password("{noop}password").roles("USER", "ADMIN");
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/**").permitAll()
            .antMatchers("/users").hasRole("ADMIN")
            .antMatchers("/login").permitAll()
            .antMatchers("/register").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .failureUrl("/login?error=true")
            .loginProcessingUrl("/authenticate")
            .defaultSuccessUrl("/mainpage")
            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login")
            .and()
            .csrf().disable();
  }


  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

}
