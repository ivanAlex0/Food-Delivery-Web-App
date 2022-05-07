package foodPanda.security;

import foodPanda.filter.CustomAuthorizationFilter;
import foodPanda.filter.CustomerAuthenticationFilter;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST, "/admin/register").permitAll();
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST, "/admin/auth").permitAll();
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST, "/customer/register").permitAll();
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST, "/customer/auth").permitAll();
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, "/admin/fetchZones").permitAll();
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, "/customer/refreshToken").permitAll();
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, "/admin/refreshToken").permitAll();

        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET,"/admin/**").hasAnyAuthority("ADMIN");
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST,"/admin/**").hasAnyAuthority("ADMIN");
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST,"/customer/**").hasAnyAuthority("USER");
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET,"/customer/**").hasAnyAuthority("USER");

        httpSecurity.authorizeRequests().anyRequest().authenticated();

        httpSecurity.addFilter(new CustomerAuthenticationFilter(authenticationManagerBean()));
        httpSecurity.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
