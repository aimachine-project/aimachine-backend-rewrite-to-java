package ai.aimachineserver

import ai.aimachineserver.userlogin.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val passwordEncoder: PasswordEncoder
) : WebSecurityConfigurerAdapter() {


    @Autowired
    private lateinit var userDetailsService: UserDetailsServiceImpl

    @Autowired
    private lateinit var authProvider: AuthProvider

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .authenticationProvider(authProvider)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
    }

    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers("/api/users/**").access("hasAnyRole('USER')")
            .and().httpBasic()
//            .antMatchers("/", "/**").permitAll()
            .and().cors()
            .and().csrf().disable()
////            .formLogin().disable()
    }
}
