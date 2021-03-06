package com.identicum.demoapi.spring;

import java.util.Arrays;
import java.util.List;

import com.identicum.demoapi.opa.voter.OPAVoter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${opa.url:http://opa:8181/v1/data}")
    private String opaUrl;

    @Value("${opa.bundle:http/authz}")
    private String opaBundle;

    @Value("${opa.rule:allow}")
    private String opaRule;
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().authorizeRequests().anyRequest().authenticated().accessDecisionManager(accessDecisionManager());
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        StringBuilder opaUrlBuilder = 
            new StringBuilder(opaUrl).append("/").append(opaBundle).append("/").append(opaRule);
        log.debug("Configuring with OPA endpoint: {}", opaUrlBuilder.toString());
        
        List<AccessDecisionVoter<? extends Object>> decisionVoters = 
            Arrays.asList(new OPAVoter(opaUrlBuilder.toString()));
        return new UnanimousBased(decisionVoters);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
            }
        };
    }

}