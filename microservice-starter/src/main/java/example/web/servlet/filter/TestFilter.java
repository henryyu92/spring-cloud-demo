package example.web.servlet.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Filter run before and after servlet
 */
public class TestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponseWrapper respWrapper = new HttpServletResponseWrapper((HttpServletResponse) response);

        String requestURI = req.getRequestURI();
        if (requestURI.contains("/addSession") || requestURI.contains("/removeSession")) {
            chain.doFilter(request, response);
        } else {
            respWrapper.sendRedirect("/online");
        }
    }

    @Override
    public void destroy() {

    }
}
