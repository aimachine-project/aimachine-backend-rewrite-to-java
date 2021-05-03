package ai.aimachineserver

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
@Profile("dev")
class WebSecurityConfigDev : WebSecurityConfigurerAdapter() {

    @Bean
    fun encoder() = BCryptPasswordEncoder()

    @Throws(java.lang.Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers("/").permitAll()

        http
            .headers().frameOptions().disable()

        http
            .cors().and().csrf().disable()
            .formLogin().disable()
    }
}
