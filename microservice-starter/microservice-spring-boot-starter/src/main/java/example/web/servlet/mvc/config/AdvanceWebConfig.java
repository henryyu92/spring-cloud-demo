package example.web.servlet.mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

/**
 *@EnableWebMvc imports DelegatingWebMvcConfiguration, which:
 *
 * - Provides default Spring configuration for Spring MVC applications
 * - Detects and delegates to WebMvcConfigurer implementations to customize that configuration.
 *
 * can remove @EnableWebMvc and extend directly from DelegatingWebMvcConfiguration instead of implementing WebMvcConfigurer
 */
@Configuration
public class AdvanceWebConfig extends DelegatingWebMvcConfiguration {
}
