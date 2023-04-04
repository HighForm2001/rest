package com.ocbc_rpp.rest.config;


import com.ocbc_rpp.rest.util.CsrfTokenFilter;
import com.ocbc_rpp.rest.util.CsrfUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().ignoringRequestMatchers(request -> HttpMethod.POST.matches(request.getMethod()))
        .csrfTokenRepository(csrfTokenRepository())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                .and()
                .addFilterAfter(new CsrfTokenFilter(csrfTokenRepository()),CsrfFilter.class);
        return http.build();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return CsrfUtil.getCsrfTokenRepository();
    }


}
    //                http.csrf().disable()
//                .authorizeHttpRequests(authorize->
//                authorize.requestMatchers(HttpMethod.POST).authenticated()
//                        .requestMatchers(HttpMethod.GET).permitAll())
//                .formLogin()
