package com.codecool.configurations.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .requiresChannel().anyRequest().requiresSecure()
                .and()
                .authorizeRequests().antMatchers("/", "/referencia", "/tmp/**", "/aszf", "/finanszirozas", "/rolunk", "/termekek/**", "/termekek/growatt/**", "/ajanlat/**", "/static/**", "/assets/**", "/pdf**/").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/", true)
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll();
        httpSecurity.headers().frameOptions().disable();
        httpSecurity .csrf().ignoringAntMatchers("/", "/aszf","/referencia", "/finanszirozas", "/rolunk", "/ajanlat/**", "/static/**", "/assets/**", "/pdf**/");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("naposOldal").password("naposOldal1298").roles("ADMIN")
        ;
    }
}



