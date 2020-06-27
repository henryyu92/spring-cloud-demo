package example.web.security.servlet.filterchain;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class CustomSecurityFilterChain implements SecurityFilterChain {

    private List<Filter> filters;

    private RequestMatcher requestMatcher;

    @Override
    public boolean matches(HttpServletRequest request) {

        return requestMatcher.matches(request);

    }

    @Override
    public List<Filter> getFilters() {
        return filters;
    }
}
