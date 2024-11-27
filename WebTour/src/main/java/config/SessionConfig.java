package config;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionConfig {

    @Bean
    public ServletContextInitializer sessionCookieConfig() {
        return servletContext -> {
            servletContext.getSessionCookieConfig().setHttpOnly(true); // Kích hoạt HttpOnly
            servletContext.getSessionCookieConfig().setSecure(true);  // Chỉ cho phép trên HTTPS
        };
    }
}
