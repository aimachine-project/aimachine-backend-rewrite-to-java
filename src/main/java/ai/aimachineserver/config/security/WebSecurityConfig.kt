package ai.aimachineserver.config.security

import ai.aimachineserver.application.CustomUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val userDetailsService: CustomUserDetailsService
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider())
    }

    @Bean
    protected fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration(
            "/**",
            CorsConfiguration()
                .applyPermitDefaultValues()
                .also {
                    it.allowedOriginPatterns = mutableListOf("*")
                    it.allowCredentials = true
                }
        )
        return source
    }

    @Autowired
    private lateinit var authenticationEntryPoint: AuthenticationEntryPoint

    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers("/api/users/create").permitAll()
            .antMatchers("/api/users/self").hasAnyRole("USER", "ADMIN")
            .antMatchers("/api/users/**").hasAnyRole("ADMIN")
            .antMatchers("/api/login").hasAnyRole("USER", "ADMIN")
            .antMatchers("/", "/**").permitAll()
            .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint)
            .and().cors().configurationSource(corsConfigurationSource())
            .and().csrf().disable()
            .formLogin().disable()
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessUrl("/api")
    }
}
