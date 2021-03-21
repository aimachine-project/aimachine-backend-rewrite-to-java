package ai.aimachineserver

import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Bean
fun corsConfigurer(): WebMvcConfigurer {
    return object : WebMvcConfigurer {
        override fun addCorsMappings(registry: CorsRegistry) {
            registry
                .addMapping("/**")
                .allowedOrigins("https://aimachine-backend.herokuapp.com")
                .exposedHeaders(
                    "Access-Control-Allow-Headers", "Access-Control-Allow-Origin",
                    "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin",
                    "Cache-Control", "Content-Type"
                )
                .allowCredentials(true)
        }
    }
}
