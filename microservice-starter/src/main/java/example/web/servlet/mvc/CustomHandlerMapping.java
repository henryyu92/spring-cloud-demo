package example.web.servlet.mvc;

import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.servlet.http.HttpServletRequest;

public class CustomHandlerMapping extends AbstractHandlerMapping {
    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        return null;
    }
}
