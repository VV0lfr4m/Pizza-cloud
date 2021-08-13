package pizza.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("customUserDetailsServiceImpl")
    private UserDetailsService userDetailsService;


    //for jdbc
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A);
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/design","/orders","/cart")
                .hasRole("USER")

                .antMatchers("/","/**")
                .permitAll()

                //default
                .and()
                .formLogin()
                .loginPage("/login")

                .and()
                .logout()
                .logoutSuccessUrl("/");
    }

}
