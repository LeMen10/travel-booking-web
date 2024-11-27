package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class SameSiteConfig {

    @Bean
    public FilterRegistrationBean<SameSiteCookieFilter> sameSiteFilter() {
        FilterRegistrationBean<SameSiteCookieFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SameSiteCookieFilter());
        registrationBean.addUrlPatterns("/*"); // Áp dụng cho tất cả các request
        return registrationBean;
    }
}
