package example.web.servlet.initialzer;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

/**
 *
 * AbstractDispatcherServletInitializer makes it even easier to register the DispatcherServlet by overriding methods to
 * specify the servlet mapping and the location of the DispatcherServlet configuration.
 */
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }

    @Override
    protected String[] getServletMappings() {
        return new String[0];
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {

        registration.setMultipartConfig(new MultipartConfigElement("/tmp"));
    }


}
