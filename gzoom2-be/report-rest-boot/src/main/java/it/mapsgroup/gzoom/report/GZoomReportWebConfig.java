package it.mapsgroup.gzoom.report;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class GZoomReportWebConfig  {

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//       //http.authorizeRequests().antMatchers("/").permitAll();
//        http
//            .authorizeRequests()
//            .antMatchers("/").authenticated() // These urls are allowed by any authenticated user
//            .and()
//            .httpBasic();
//        http.csrf().disable();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        //http.httpBasic(withDefaults());
//        http.authorizeHttpRequests(authorizeHttpRequests ->
//                authorizeHttpRequests
//                        .requestMatchers("/").authenticated());
        return http.build();
    }
}
