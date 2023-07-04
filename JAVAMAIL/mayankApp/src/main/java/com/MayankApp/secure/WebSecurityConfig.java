package com.MayankApp.secure;

import com.MayankApp.entity.UserDetails;
import com.MayankApp.service.CustomUserDtlsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import static org.springframework.security.authorization.AuthenticatedAuthorizationManager.authenticated;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

//    public static final String[] ENDPOINTS_WHITELIST = {
//            "/",
//            "/login",
//            "/user/main",
//            "/logout",
//            "admin/users",
//            "admin/update/**",
//            "admin/delete/**",
//            "user/verify",
//    };
    UserDetails u;
    @Bean
    public BCryptPasswordEncoder getPassword(){
        return new BCryptPasswordEncoder();
    }
//    @Bean
    /*public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(request ->
                        request.
                                requestMatchers(ENDPOINTS_WHITELIST).permitAll()
                                .anyRequest().authenticated())
                .csrf().disable()
                .formLogin(form -> form
                        .loginPage(LOGIN_URL)
                        .loginProcessingUrl("/doLogin")
                        .failureUrl(LOGIN_FAIL_URL)
                        .usernameParameter(USERNAME)
                        .passwordParameter(PASSWORD)
                        .defaultSuccessUrl(DEFAULT_SUCCESS_URL))

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .invalidSessionUrl("/logout/?expired")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true))

                .logout(logout -> logout
                .logoutUrl(LOGOUT_URL)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl(LOGIN_URL + "?logout"));
        return http.build();
    }

     */
//        @Bean
//        UserDetailsService userDetailsService() {
//            var admin = User.builder()
//                    .username("mankubhaiji@gmail.com")
//                    .password("{noop}pass")
//                    .roles("USER","ADMIN")
//                    .build();
//        var user = User.builder()
//                .username(u.getName())
//                .password(u.getPassword())
//                .roles("USER")
//                .build();
//
//            return new InMemoryUserDetailsManager(admin);
//        }
    @Bean
    public DaoAuthenticationProvider daoProvider(){
        DaoAuthenticationProvider dao=new DaoAuthenticationProvider();
        dao.setUserDetailsService(getUserDetailsService());
        dao.setPasswordEncoder(getPassword());
        return dao;
    }

    @Bean
    public UserDetailsService getUserDetailsService(){
        return new CustomUserDtlsService();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests().requestMatchers("/","/login","/user/verify").
                permitAll().anyRequest().authenticated().and().formLogin().loginPage("/login").
                loginProcessingUrl("/dologin").defaultSuccessUrl("/user/main").
                and().csrf().disable();
        http.authenticationProvider(daoProvider()).sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        return  http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}


