package example.web.security.servlet.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 自定义登录过滤器
 */
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        try {
            return obtain(request, "username");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        try{
            return obtain(request, "password");
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private String obtain(HttpServletRequest request, String k) throws IOException {
        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        if (StringUtils.hasText(body)){
            Map map = new ObjectMapper().readValue(body, Map.class);
            return String.valueOf(map.get(k));
        }
        return null;
    }
}
