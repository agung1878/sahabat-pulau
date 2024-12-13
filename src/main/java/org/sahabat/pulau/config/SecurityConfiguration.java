package org.sahabat.pulau.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Autowired
    private DataSource dataSource;


    private static final String SQL_ROLE
            ="select u.username as username, csp.permission_value as authority " +
            " from c_security_user u " +
            " inner join c_security_role_permission p on u.id_role  = p.id_role " +
            " inner join c_security_permission csp on csp.id = p.id_permission " +
            " where u.username = ?";

    private static final String SQL_LOGIN
            = "select u.username as username,p.user_password as password, u.active as active "
            + "from c_security_user u "
            + "inner join c_security_user_password p on p.id_user = u.id "
            + "where username = ?";

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(SQL_LOGIN);
        manager.setAuthoritiesByUsernameQuery(SQL_ROLE);
        return manager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin_panel/**").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/admin_panel/dashboard",true)
                )
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .logout(logout -> logout
                .logoutUrl("/logout") // Specify the logout URL (optional)
                .logoutSuccessUrl("/") // Redirect to "/" after logout
                .invalidateHttpSession(true) // Invalidate the session
                .deleteCookies("JSESSIONID") // Delete cookies
        );;

        return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}