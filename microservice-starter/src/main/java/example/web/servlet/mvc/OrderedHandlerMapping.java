package example.web.servlet.mvc;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;


public class OrderedHandlerMapping implements HandlerMapping, Ordered {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        String uri = request.getRequestURI();

        return null;
    }
}
