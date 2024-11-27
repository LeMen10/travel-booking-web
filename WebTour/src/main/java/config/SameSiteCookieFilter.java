package config;
import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

public class SameSiteCookieFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        chain.doFilter(request, response); // Xử lý request bình thường

        // Thêm SameSite vào header của Set-Cookie
        for (String header : httpResponse.getHeaders("Set-Cookie")) {
            httpResponse.setHeader("Set-Cookie", header + "; SameSite=Strict");
        }
    }
}
