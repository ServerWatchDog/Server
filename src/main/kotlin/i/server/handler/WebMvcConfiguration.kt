package i.server.handler

import i.server.handler.filter.RequestJsonFilter
import i.server.handler.inject.encrypt.EncryptResolver
import i.server.handler.inject.page.RestPageResolver
import i.server.handler.inject.security.AuthorityInterceptor
import i.server.handler.inject.security.SessionResolver
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Profiles
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration(
    private val authorityInterceptor: AuthorityInterceptor,
    private val encryptResolver: EncryptResolver,
    private val restPageResolver: RestPageResolver,
    private val sessionResolver: SessionResolver,
    private val jsonFilter: RequestJsonFilter,
    private val context: ApplicationContext,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authorityInterceptor)
            .addPathPatterns("/api/**")
            .addPathPatterns("/view/**")
            .addPathPatterns("/agent/**")
        super.addInterceptors(registry)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(encryptResolver)
        resolvers.add(restPageResolver)
        resolvers.add(sessionResolver)
        super.addArgumentResolvers(resolvers)
    }

    @Bean
    fun jsonFilterRegister(): FilterRegistrationBean<*> {
        return FilterRegistrationBean<RequestJsonFilter>().apply {
            filter = jsonFilter
            addUrlPatterns("/*")
            setName("requestJsonFilter")
            isEnabled = true
            order = 1
        }
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        if (context.environment.acceptsProfiles(Profiles.of("dev"))) {
            registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("*")
        }
        super.addCorsMappings(registry)
    }
}
