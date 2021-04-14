package ai.aimachineserver

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth // enable in memory based authentication with a user named
            // "user" and "admin"
            .inMemoryAuthentication()
            .withUser("user").password("{noop}password").roles("USER").and()
            .withUser("admin").password("{noop}password").roles("USER", "ADMIN")
    }

    // Expose the UserDetailsService as a Bean
    @Bean
    @Throws(Exception::class)
    override fun userDetailsServiceBean(): UserDetailsService? {
        return super.userDetailsServiceBean()
    }
}
